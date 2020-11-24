/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.file.filesystems;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.UserInfo;
import com.mirth.connect.connectors.file.FileSystemConnectionOptions;
import com.mirth.connect.connectors.file.SftpSchemeProperties;
import com.mirth.connect.connectors.file.filters.RegexFilenameFilter;

public class SftpConnection implements FileSystemConnection {

    public class SftpFileInfo implements FileInfo {

        String thePath;
        ChannelSftp.LsEntry theFile;

        public SftpFileInfo(String path, ChannelSftp.LsEntry theFile) {
            this.thePath = path;
            this.theFile = theFile;
        }

        public long getLastModified() {
            /*
             * The time returned in in seconds, so we need to convert it to milliseconds. See
             * MIRTH-1913.
             */
            return Long.valueOf(theFile.getAttrs().getMTime()) * 1000L;
        }

        public String getName() {
            return theFile.getFilename();
        }

        /** Gets the absolute pathname of the file */
        public String getAbsolutePath() {

            return getParent() + "/" + getName();
        }

        public String getCanonicalPath() throws IOException {
            throw new UnsupportedOperationException();
        }

        /** Gets the absolute pathname of the directory holding the file */
        public String getParent() {
            return this.thePath;
        }

        public long getSize() {
            return theFile.getAttrs().getSize();
        }

        public boolean isDirectory() {
            return theFile.getAttrs().isDir();
        }

        public boolean isFile() {
            SftpATTRS attrs = theFile.getAttrs();
            return !attrs.isDir() && !attrs.isLink();
        }

        public boolean isReadable() {
            return true;
            // return (theFile.getAttrs().getPermissions() & MASK) != 0;
        }

        @Override
        public void populateSourceMap(Map<String, Object> sourceMap) {}
    }

    private static transient Log logger = LogFactory.getLog(SftpConnection.class);

    /** The JSch SFTP client instance */
    private ChannelSftp client = null;
    private Session session = null;
    private String lastDir = null;
    private Properties configuration;
    private boolean isPasswordAuth = true;

    public SftpConnection(String host, int port, FileSystemConnectionOptions fileSystemOptions, int timeout) throws Exception {

        JSch jsch = new JSch();
        configuration = new Properties();

        try {
            SftpSchemeProperties sftpSchemeProperties = (SftpSchemeProperties) fileSystemOptions.getSchemeProperties();
            isPasswordAuth = sftpSchemeProperties.isPasswordAuth();

            String knownHostsFile = sftpSchemeProperties.getKnownHostsFile();
            if (StringUtils.isNotEmpty(knownHostsFile)) {
                jsch.setKnownHosts(knownHostsFile);
            }

            configuration.put("StrictHostKeyChecking", sftpSchemeProperties.getHostChecking());

            if (sftpSchemeProperties.isKeyAuth()) {
                String keyLocation = sftpSchemeProperties.getKeyFile();
                String keyPassphrase = sftpSchemeProperties.getPassPhrase();

                if (StringUtils.isNotEmpty(keyPassphrase)) {
                    jsch.addIdentity(keyLocation, keyPassphrase);
                } else {
                    jsch.addIdentity(keyLocation);
                }
            }

            Map<String, String> configurationSettings = sftpSchemeProperties.getConfigurationSettings();
            if (MapUtils.isNotEmpty(configurationSettings)) {
                for (Map.Entry<String, String> configEntry : configurationSettings.entrySet()) {
                    configuration.put(configEntry.getKey(), configEntry.getValue());
                }
            }

            String username = fileSystemOptions.getUsername();
            if (port > 0) {
                session = jsch.getSession(username, host, port);
            } else {
                session = jsch.getSession(username, host);
            }

            session.setTimeout(timeout);

            if (isPasswordAuth) {
                UserInfo userInfo = new SftpUserInfo(fileSystemOptions.getPassword());
                session.setUserInfo(userInfo);
            }

            if (!configuration.isEmpty()) {
                session.setConfig(configuration);
            }

            session.connect(timeout);

            Channel channel = session.openChannel("sftp");
            channel.connect();
            client = (ChannelSftp) channel;
        } catch (Exception e) {
            destroy();
            throw e;
        }
    }

    @Override
    public List<FileInfo> listFiles(String fromDir, String filenamePattern, boolean isRegex, boolean ignoreDot) throws Exception {
        lastDir = fromDir;
        FilenameFilter filenameFilter;

        if (isRegex) {
            filenameFilter = new RegexFilenameFilter(filenamePattern);
        } else {
            filenameFilter = new WildcardFileFilter(filenamePattern.trim().split("\\s*,\\s*"));
        }

        cwd(fromDir);

        @SuppressWarnings("unchecked")
        Vector<ChannelSftp.LsEntry> entries = client.ls(".");
        List<FileInfo> files = new ArrayList<FileInfo>(entries.size());

        for (Iterator<ChannelSftp.LsEntry> iter = entries.iterator(); iter.hasNext();) {
            ChannelSftp.LsEntry entry = iter.next();

            if (!entry.getAttrs().isDir() && !entry.getAttrs().isLink()) {
                if (((filenameFilter == null) || filenameFilter.accept(null, entry.getFilename())) && !(ignoreDot && entry.getFilename().startsWith("."))) {
                    files.add(new SftpFileInfo(fromDir, entry));
                }
            }
        }

        return files;
    }

    @Override
    public List<String> listDirectories(String fromDir) throws Exception {
        List<String> directories = new ArrayList<String>();

        cwd(fromDir);

        @SuppressWarnings("unchecked")
        Vector<ChannelSftp.LsEntry> entries = client.ls(".");

        for (Iterator<ChannelSftp.LsEntry> iter = entries.iterator(); iter.hasNext();) {
            ChannelSftp.LsEntry entry = iter.next();

            if (entry.getAttrs().isDir() || entry.getAttrs().isLink()) {
                directories.add(new SftpFileInfo(fromDir, entry).getAbsolutePath());
            }
        }

        return directories;
    }

    @Override
    public boolean exists(String file, String path) {
        try {
            cwd(path);
            return client.ls(".").contains(file);
        } catch (Exception e) {
            return false;
        }
    }

    private void cwd(String path) throws Exception {
        client.cd(URLDecoder.decode(path, Charset.defaultCharset().name()));
    }

    @Override
    public boolean canRead(String readDir) {
        try {
            lastDir = readDir;
            cwd(readDir);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canWrite(String writeDir) {
        try {
            lastDir = writeDir;
            cwd(writeDir);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InputStream readFile(String file, String fromDir, Map<String, Object> sourceMap) throws Exception {
        lastDir = fromDir;
        cwd(fromDir);
        return client.get(file);
    }

    /** Must be called after readFile when reading is complete */
    @Override
    public void closeReadFile() throws Exception {
        // nothing
    }

    @Override
    public boolean canAppend() {

        return true;
    }

    @Override
    public void writeFile(String file, String toDir, boolean append, InputStream is, long contentLength, Map<String, Object> connectorMap) throws Exception {
        lastDir = toDir;
        cdmake(toDir);
        int mode = 0;
        if (append)
            mode = ChannelSftp.APPEND;
        else
            mode = ChannelSftp.OVERWRITE;

        client.put(is, file, mode);
    }

    @Override
    public void delete(String file, String fromDir, boolean mayNotExist) throws Exception {
        cwd(fromDir);
        try {
            client.rm(file);
        } catch (Exception e) {
            if (!mayNotExist) {
                throw e;
            }
        }
    }

    private void cdmake(String dir) throws Exception {

        try {
            cwd(dir);
        } catch (Exception e) {
            String toDir = dir;
            if (toDir.startsWith("/")) {
                toDir = toDir.substring(1);
                client.cd("/");
            }
            String[] dirs = toDir.split("/");
            if (dirs.length > 0) {
                for (int i = 0; i < dirs.length; i++) {
                    try {
                        client.cd(dirs[i]);
                    } catch (Exception ex) {
                        logger.debug("Making directory: " + dirs[i]);
                        client.mkdir(dirs[i]);
                        client.cd(dirs[i]);
                    }
                }
            }
        }
    }

    @Override
    public void move(String fromName, String fromDir, String toName, String toDir) throws Exception {
        cwd(fromDir); // start in the read directory
        // Create any missing directories in the toDir path
        cdmake(toDir);

        try {
            client.rm(toName);
        } catch (Exception e) {
            logger.info("Unable to delete destination file");
        }

        cwd(fromDir); // move to the read directory
        client.rename(fromName.replaceAll("//", "/"), (toDir + "/" + toName).replaceAll("//", "/"));
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public void disconnect() {}

    // **************************************************
    // Lifecycle methods

    @Override
    public void activate() {
        // Nothing
    }

    @Override
    public void passivate() {
        // Nothing
    }

    @Override
    public void destroy() {
        if (client != null) {
            client.quit();
        }

        if (session != null) {
            session.disconnect();
        }
    }

    @Override
    public boolean isValid() {
        if (lastDir == null) {
            return client.isConnected();
        } else {
            return client.isConnected() && canRead(lastDir);
        }
    }
}