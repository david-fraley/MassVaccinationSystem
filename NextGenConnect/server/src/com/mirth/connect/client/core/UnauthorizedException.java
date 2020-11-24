/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.core;

public class UnauthorizedException extends ClientException {

    private Object response;

    public UnauthorizedException(String message) {
        this(message, null);
    }

    public UnauthorizedException(String message, Object response) {
        super(message);
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }
}
