/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.util;

import junit.framework.Assert;

import org.junit.Test;

public class Pre22PasswordCheckerTests {

    @Test
    public void testCheckPassword() throws Exception {
        String salt = "Np+FZYzu4M0=";
        String hash = "NdgB6ojoGb/uFa5amMEyBNG16mE=";
        String migratedSaltHash = "SALT_" + salt + hash;

        Assert.assertTrue(Pre22PasswordChecker.checkPassword("admin", migratedSaltHash));
        Assert.assertFalse(Pre22PasswordChecker.checkPassword("foo", migratedSaltHash));
        Assert.assertFalse(Pre22PasswordChecker.checkPassword("", migratedSaltHash));
    }
}
