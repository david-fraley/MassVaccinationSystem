/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.file.filesystems;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * The interface that must be implemented by a file system for it to be usable by the File
 * connector.
 */
public interface FileSystemConnection {

    /**
     * Gets a List of FileInfo for the files located in the specified folder with names matching the
     * specified pattern.
     * 
     * @param fromDir
     *            The directory (folder) to be searched for files.
     * @param filenamePattern
     *            The pattern file names must match to be included. The exact syntax of a
     *            namePattern may vary between FileSystems.
     * @param isRegex
     *            If the pattern should be interpreted as a regular expression.
     * @param ignoreDot
     *            If files starting with . should be ignored
     * @return A List of FileInfo for the files located in the specified folder with names matching
     *         the specified pattern.
     * @throws Exception
     */
    public List<FileInfo> listFiles(String fromDir, String filenamePattern, boolean isRegex, boolean ignoreDot) throws Exception;

    public List<String> listDirectories(String fromDir) throws Exception;

    /**
     * Test if the file exists.
     * 
     * @param file
     *            The name of the file.
     * @param path
     *            The full path of the directory containing the file.
     */
    public boolean exists(String file, String path) throws Exception;

    /**
     * Constructs and returns an InputStream to read the contents of the specified file in the
     * specified directory.
     * 
     * @param file
     *            The name of the file to be read, with no path information.
     * @param fromDir
     *            The full path of the directory containing the file.
     * @param sourceMap
     *            The source map associated with the message, if applicable. The subclass may inject
     *            its own values here in addition to reading the file contents. May be null.
     * @return An InputStream that reads the contents of the file.
     * @throws Exception
     */
    public InputStream readFile(String file, String fromDir, Map<String, Object> sourceMap) throws Exception;

    /** Must be called after readFile when reading is complete */
    public void closeReadFile() throws Exception;

    /** Tests if this connection can append to an output file. */
    public boolean canAppend();

    /**
     * Write a message to the specified file.
     * 
     * @param file
     *            The name of the file to be written, with no path information.
     * @param toDir
     *            The full path of the directory containing the file.
     * @param append
     *            True if the file should be appended to if it already exists, false if the file
     *            should be truncated first.
     * @param message
     *            The message to be written.
     * @param contentLength
     *            The length of the message to be written, in bytes.
     * @param connectorMap
     *            The connector map associated with the message, if applicable. The subclass may
     *            inject its own values here in addition to writing the file contents. May be null.
     * @throws Exception
     */
    public void writeFile(String file, String toDir, boolean append, InputStream message, long contentLength, Map<String, Object> connectorMap) throws Exception;

    /**
     * Removes the specified file from the specified directory.
     * 
     * @param file
     *            The name of the file to be deleted, with no path information.
     * @param fromDir
     *            The full path of the directory containing the file.
     * @param mayNotExist
     *            True iff it is not an error for the file to be missing.
     * @throws Exception
     */
    public void delete(String file, String fromDir, boolean mayNotExist) throws Exception;

    /**
     * Moves the specified file from the specified directory to a potentially different name and/or
     * directory.
     * 
     * @param fromName
     *            The current name of the file to be moved or renamed, with no path information.
     * @param fromDir
     *            The full path of the directory containing the file to be moved or renamed.
     * @param toName
     *            The new name for the file, with no path information.
     * @param toDir
     *            The new directory to contain the file.
     * @throws Exception
     */
    public void move(String fromName, String fromDir, String toName, String toDir) throws Exception;

    /** Tests if this connection is in fact connected */
    public boolean isConnected();

    /** Forcibly disconnect this connection */
    public void disconnect();

    // **************************************************
    // Lifecycle methods

    /** Activate the connection */
    public void activate();

    /** Deactivate the connection */
    public void passivate();

    /** Destroy the connection */
    public void destroy();

    /** Test if the connection is valid */
    public boolean isValid();

    /** Test if can read from specified directory **/
    public boolean canRead(String readDir);

    /** Test if can write to specified directory **/
    public boolean canWrite(String writeDir);
}