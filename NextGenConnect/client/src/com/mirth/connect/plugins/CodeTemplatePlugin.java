/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins;

import java.util.List;
import java.util.Map;

import com.mirth.connect.model.codetemplates.CodeTemplate;

public abstract class CodeTemplatePlugin extends ClientPlugin {

    public CodeTemplatePlugin(String name) {
        super(name);
    }

    public abstract Map<String, List<CodeTemplate>> getReferenceItems();
}