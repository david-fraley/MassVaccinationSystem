/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.migration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.tuple.MutablePair;

import com.mirth.connect.model.util.MigrationException;

public class Migrate3_2_2 extends Migrator implements ConfigurationMigrator {
    @Override
    public void migrate() throws MigrationException {}

    @Override
    public void migrateSerializedData() throws MigrationException {}

    @Override
    public Map<String, Object> getConfigurationPropertiesToAdd() {
        Map<String, Object> propertiesToAdd = new LinkedHashMap<String, Object>();

        propertiesToAdd.put("server.includecustomlib", new MutablePair<Object, String>(true, "Determines whether libraries in the custom-lib directory will be included on the server classpath.\nTo reduce potential classpath conflicts you should create Resources and use them on specific channels/connectors instead, and then set this value to false."));

        return propertiesToAdd;
    }

    @Override
    public String[] getConfigurationPropertiesToRemove() {
        return null;
    }

    @Override
    public void updateConfiguration(PropertiesConfiguration configuration) {}
}
