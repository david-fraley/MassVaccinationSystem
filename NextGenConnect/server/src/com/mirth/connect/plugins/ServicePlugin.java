/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins;

import java.util.Map;
import java.util.Properties;

import com.mirth.connect.model.ExtensionPermission;

public interface ServicePlugin extends ServerPlugin {
    public void init(Properties properties);

    public void update(Properties properties);

    /**
     * Returns the default properties for this plugin, or an empty Properties if there are none.
     * 
     * @return
     */
    public Properties getDefaultProperties();

    /**
     * Returns permissions for this plugin so they can be initialized on startup.
     * 
     * @return
     */
    public ExtensionPermission[] getExtensionPermissions();
    
    /**
     * Returns a map of strings to example objects for use in populating swagger's examples.
     * 
     * @return
     */
    public Map<String, Object> getObjectsForSwaggerExamples();
}
