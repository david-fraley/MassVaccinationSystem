/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Color;
import java.util.Calendar;

import javax.swing.ImageIcon;

import com.mirth.connect.model.ServerSettings;
import com.mirth.connect.util.MirthSSLUtil;

/**
 * A class of static variables that need to be referenced from multiple locations.
 */
public class PlatformUI {

    public static Frame MIRTH_FRAME;
    public static ImageIcon BACKGROUND_IMAGE;
    public static String ENVIRONMENT_NAME;
    public static String SERVER_NAME;
    public static String SERVER_URL;
    public static String SERVER_ID;
    public static String SERVER_TIMEZONE;
    public static Calendar SERVER_TIME;
    public static String SERVER_DATABASE;
    public static String USER_NAME;
    public static String CLIENT_VERSION;
    public static String SERVER_VERSION;
    public static String BUILD_DATE;
    public static Color DEFAULT_BACKGROUND_COLOR = ServerSettings.DEFAULT_COLOR;
    public static String[] HTTPS_PROTOCOLS = MirthSSLUtil.DEFAULT_HTTPS_CLIENT_PROTOCOLS;
    public static String[] HTTPS_CIPHER_SUITES = MirthSSLUtil.DEFAULT_HTTPS_CIPHER_SUITES;
    public static String[] SERVER_HTTPS_SUPPORTED_PROTOCOLS = MirthSSLUtil.getSupportedHttpsProtocols();
    public static String[] SERVER_HTTPS_ENABLED_CLIENT_PROTOCOLS = MirthSSLUtil.DEFAULT_HTTPS_CLIENT_PROTOCOLS;
    public static String[] SERVER_HTTPS_ENABLED_SERVER_PROTOCOLS = MirthSSLUtil.DEFAULT_HTTPS_SERVER_PROTOCOLS;
    public static String[] SERVER_HTTPS_SUPPORTED_CIPHER_SUITES = MirthSSLUtil.getSupportedHttpsCipherSuites();
    public static String[] SERVER_HTTPS_ENABLED_CIPHER_SUITES = MirthSSLUtil.DEFAULT_HTTPS_CIPHER_SUITES;
}
