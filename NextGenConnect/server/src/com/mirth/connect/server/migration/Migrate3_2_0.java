/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.migration;

import java.util.Map;

import org.apache.commons.configuration2.PropertiesConfiguration;

import com.mirth.connect.model.util.MigrationException;

public class Migrate3_2_0 extends Migrator implements ConfigurationMigrator {
    @Override
    public void migrate() throws MigrationException {
        executeScript(getDatabaseType() + "-3.1.1-3.2.0.sql");
    }

    // @formatter:off
    @Override public void migrateSerializedData() throws MigrationException {}
    @Override public Map<String, Object> getConfigurationPropertiesToAdd() { return null; }
    @Override public String[] getConfigurationPropertiesToRemove() { return null; }
    @Override public void updateConfiguration(PropertiesConfiguration configuration) {}
    // @formatter:on
}
