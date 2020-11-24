/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

public class DefaultAuthorizationController implements AuthorizationController {

    @Override
    public void initialize() {

    }

    @Override
    public boolean checkTask(String taskGroup, String taskName) {
        return true;
    }

}
