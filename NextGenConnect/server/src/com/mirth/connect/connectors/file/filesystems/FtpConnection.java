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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.mirth.connect.connectors.file.FTPSchemeProperties;
import com.mirth.connect.connectors.file.FileSystemConnectionOptions;
import com.mirth.connect.connectors.file.filters.RegexFilenameFilter;

/**
 * The FileSystemConnection class for files accessed via FTP.
 */
public class FtpConnection implements FileSystemConnection {

    public class FtpFileInfo implements FileInfo {

        String thePath;
        FTPFile theFile;

        public FtpFileInfo(String path, FTPFile theFile) {
            this.thePath = path;
            this.theFile = theFile;
        }

        public long getLastModified() {
            return theFile.getTimestamp().getTimeInMillis();
        }

        public String getName() {
            return theFile.getName();
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
            return theFile.getSize();
        }

        public boolean isDirectory() {
            return theFile.isDirectory();
        }

        public boolean isFile() {
            return theFile.isFile();
        }

        public boolean isReadable() {
            return true;
            // return theFile.hasPermission(access, permission);
        }

        @Override
        public void populateSourceMap(Map<String, Object> sourceMap) {}
    }

    private static transient Log logger = LogFactory.getLog(FtpConnection.class);

    /** The apache commons FTP client instance */
    protected FTPClient client = null;

    public FtpConnection(String host, int port, FileSystemConnectionOptions fileSystemOptions, boolean passive, int timeout) throws Exception {
        this(host, port, fileSystemOptions, passive, timeout, new HaltableFTPClient());
    }

    public FtpConnection(String host, int port, FileSystemConnectionOptions fileSystemOptions, boolean passive, int timeout, FTPClient client) throws Exception {
        this.client = client;
        // This sets the timeout for read operations on data sockets. It does not affect write operations.
        client.setDataTimeout(timeout);

        // This sets the timeout for the initial connection.
        client.setConnectTimeout(timeout);

        try {
            if (port > 0) {
                client.connect(host, port);
            } else {
                client.connect(host);
            }

            // This sets the timeout for read operations on the command socket. As per JavaDoc comments, you should only call this after the connection has been opened by connect()
            client.setSoTimeout(timeout);

            if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                throw new IOException("Ftp error: " + client.getReplyCode());
            }
            if (!client.login(fileSystemOptions.getUsername(), fileSystemOptions.getPassword())) {
                throw new IOException("Ftp error: " + client.getReplyCode());
            }
            if (!client.setFileType(FTP.BINARY_FILE_TYPE)) {
                throw new IOException("Ftp error");
            }

            initialize((FTPSchemeProperties)fileSystemOptions.getSchemeProperties());

            if (passive) {
                client.enterLocalPassiveMode();
            }
        } catch (Exception e) {
            if (client.isConnected()) {
                client.disconnect();
            }
            throw e;
        }
    }

    /**
     * This method allows subclasses of FtpConnection to issue additional commands after a
     * connection is established.
     */
    protected void initialize(FTPSchemeProperties schemeProperties) throws Exception {
        if (schemeProperties != null) {
            List<String> commands = schemeProperties.getInitialCommands();
            if (CollectionUtils.isNotEmpty(commands)) {
                for (String command: commands) {
                    int result = client.sendCommand(command);
                    if (!FTPReply.isPositiveCompletion(result)) {
                        logger.error("failed to issue command " + command + " with result " + client.getReplyString());
                    }
                }
            }
        }
    }

    @Override
    public List<FileInfo> listFiles(String fromDir, String filenamePattern, boolean isRegex, boolean ignoreDot) throws Exception {
        FilenameFilter filenameFilter;

        if (isRegex) {
            filenameFilter = new RegexFilenameFilter(filenamePattern);
        } else {
            filenameFilter = new WildcardFileFilter(filenamePattern.trim().split("\\s*,\\s*"));
        }

        if (!cwd(fromDir)) {
            logger.error("listFiles.changeWorkingDirectory: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }

        FTPFile[] files = client.listFiles();
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            logger.error("listFiles.listFiles: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }

        if (files == null || files.length == 0) {
            return new ArrayList<FileInfo>();
        }

        List<FileInfo> v = new ArrayList<FileInfo>(files.length);

        for (int i = 0; i < files.length; i++) {
            if ((files[i] != null) && files[i].isFile()) {
                if ((filenameFilter == null || filenameFilter.accept(null, files[i].getName())) && !(ignoreDot && files[i].getName().startsWith("."))) {
                    v.add(new FtpFileInfo(fromDir, files[i]));
                }
            }
        }
        return v;
    }

    @Override
    public List<String> listDirectories(String fromDir) throws Exception {
        List<String> directories = new ArrayList<String>();

        if (!cwd(fromDir)) {
            logger.error("listFiles.changeWorkingDirectory: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }

        FTPFile[] ftpDirectories = client.listDirectories();
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            logger.error("listFiles.listFiles: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }

        for (FTPFile directory : ftpDirectories) {
            if (directory != null) {
                directories.add(new FtpFileInfo(fromDir, directory).getAbsolutePath());
            }
        }

        return directories;
    }

    @Override
    public boolean exists(String file, String path) {
        try {
            FTPFile[] files = client.listFiles(path + "/" + file);
            return ((files != null) && (files.length == 1));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean canRead(String readDir) {
        try {
            return cwd(readDir);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean canWrite(String writeDir) {
        try {
            return cwd(writeDir);
        } catch (IOException e) {
            return false;
        }
    }

    // See MIRTH-1873
    private boolean cwd(String destDir) throws IOException {
        return client.changeWorkingDirectory(destDir);
    }

    @Override
    public InputStream readFile(String file, String fromDir, Map<String, Object> sourceMap) throws Exception {
        if (!cwd(fromDir)) {
            logger.error("readFile.changeWorkingDirectory: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }

        return client.retrieveFileStream(file);
    }

    /** Must be called after readFile when reading is complete */
    @Override
    public void closeReadFile() throws Exception {
        if (!client.completePendingCommand()) {
            logger.error("closeReadFile.completePendingCommand: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }
    }

    @Override
    public boolean canAppend() {

        return true;
    }

    @Override
    public void writeFile(String file, String toDir, boolean append, InputStream is, long contentLength, Map<String, Object> connectorMap) throws Exception {
        cdmake(toDir);

        if (append) {
            client.appendFile(file, is);
        } else {
            client.storeFile(file, is);
        }

        // have to close it since append or store don't close the stream
        is.close();

        // check the reply code to verify success
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            logger.error("writeFile: " + client.getReplyString());
            throw new IOException(client.getReplyString());
        }
    }

    @Override
    public void delete(String file, String fromDir, boolean mayNotExist) throws Exception {
        if (!cwd(fromDir)) {
            if (!mayNotExist) {
                logger.error("delete.changeWorkingDirectory: " + client.getReplyCode() + "-" + client.getReplyString());
                throw new IOException("Ftp error: " + client.getReplyCode());
            } else {
                return;
            }
        }

        boolean deleteSucceeded = client.deleteFile(file);
        if (!deleteSucceeded) {
            if (!mayNotExist) {
                logger.error("delete.deleteFile: " + client.getReplyCode() + "-" + client.getReplyString());
                throw new IOException("Ftp error: " + client.getReplyCode());
            }
        }
    }

    private void cdmake(String dir) throws Exception {
        if (!cwd(dir)) {
            if (!client.makeDirectory(dir)) {
                String tempDir = dir;

                if (tempDir.startsWith("/")) {
                    // strip the first forward slash
                    tempDir = tempDir.substring(1);

                    // cd into the base directory
                    if (!client.changeWorkingDirectory("/")) {
                        throw new Exception("Unable to change to destination directory: /");
                    }
                }

                String[] dirs = tempDir.split("/");

                if (dirs.length > 0) {
                    for (int i = 0; i < dirs.length; i++) {
                        if (!client.changeWorkingDirectory(dirs[i])) {
                            logger.debug("Making directory: " + dirs[i]);

                            if (!client.makeDirectory(dirs[i])) {
                                throw new Exception("Unable to make destination directory: " + dirs[i]);
                            }

                            if (!client.changeWorkingDirectory(dirs[i])) {
                                throw new Exception("Unable to change to destination directory: " + dirs[i]);
                            }
                        }
                    }
                }
            } else if (!cwd(dir)) {
                throw new Exception("Unable to change to destination directory: " + dir);
            }
        }
    }

    @Override
    public void move(String fromName, String fromDir, String toName, String toDir) throws Exception {
        cwd(fromDir); // start in the read directory
        cdmake(toDir);

        try {
            client.deleteFile(toName);
        } catch (Exception e) {
            logger.info("Unable to delete destination file");
        }

        if (!cwd(fromDir)) {
            throw new Exception("Unable to change to directory: " + fromDir.substring(1) + "/");
        }

        boolean renameSucceeded = client.rename(fromName.replaceAll("//", "/"), (toDir + "/" + toName).replaceAll("//", "/"));
        if (!renameSucceeded) {
            logger.error("move.rename: " + client.getReplyCode() + "-" + client.getReplyString());
            throw new IOException("Ftp error: " + client.getReplyCode());
        }
    }

    @Override
    public boolean isConnected() {

        if (client != null) {
            return client.isConnected();
        } else {
            return false;
        }
    }

    @Override
    public void disconnect() {
        if (client instanceof HaltableFTPClient) {
            ((HaltableFTPClient) client).closeDataSockets();
        }
        try {
            client.disconnect();
        } catch (IOException e) {
            logger.debug(e);
        }
    }

    // **************************************************
    // Lifecycle methods

    @Override
    public void activate() {

    }

    @Override
    public void passivate() {

    }

    @Override
    public void destroy() {
        try {
            client.logout();
            client.disconnect();
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    @Override
    public boolean isValid() {
        try {
            client.sendNoOp();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
