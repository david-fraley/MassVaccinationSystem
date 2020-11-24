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

import com.mirth.connect.model.util.MigrationException;

public class Migrate2_2_0 extends Migrator implements ConfigurationMigrator {
    @Override
    public void migrate() throws MigrationException {
        executeScript(getDatabaseType() + "-8-9.sql");
    }

    @Override
    public void migrateSerializedData() throws MigrationException {}

    @Override
    public Map<String, Object> getConfigurationPropertiesToAdd() {
        Map<String, Object> propertiesToAdd = new LinkedHashMap<String, Object>();
        propertiesToAdd.put("password.retrylimit", 0);
        propertiesToAdd.put("password.lockoutperiod", 0);
        propertiesToAdd.put("password.expiration", 0);
        propertiesToAdd.put("password.graceperiod", 0);
        propertiesToAdd.put("password.reuseperiod", 0);
        propertiesToAdd.put("password.reuselimit", 0);
        return propertiesToAdd;
    }

    @Override
    public String[] getConfigurationPropertiesToRemove() {
        return new String[] { "keystore.storetype", "keystore.algorithm", "keystore.alias",
                "truststore.storetype", "truststore.algorithm" };
    }

    @Override
    public void updateConfiguration(PropertiesConfiguration configuration) {}
}
