/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.model.datatype;

import java.io.Serializable;

public class DataTypePropertyDescriptor implements Serializable {

    private Object value;
    private String displayName;
    private String description;
    private PropertyEditorType editorType;
    private Object[] options;

    public DataTypePropertyDescriptor(Object value, String displayName, String description, PropertyEditorType editorType) {
        this(value, displayName, description, editorType, null);
    }

    public DataTypePropertyDescriptor(Object value, String displayName, String description, PropertyEditorType editorType, Object[] options) {
        this.value = value;
        this.displayName = displayName;
        this.description = description;
        this.editorType = editorType;
        this.setOptions(options);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PropertyEditorType getEditorType() {
        return editorType;
    }

    public void setEditorType(PropertyEditorType editorType) {
        this.editorType = editorType;
    }

    public Object[] getOptions() {
        return options;
    }

    public void setOptions(Object[] options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DataTypePropertyDescriptor) {
            DataTypePropertyDescriptor descriptor = (DataTypePropertyDescriptor) object;

            if (value.equals(descriptor.getValue()) && displayName.equals(descriptor.getDisplayName()) && description.equals(descriptor.getDescription()) && editorType.equals(descriptor.getEditorType())) {
                return true;
            }
        }

        return false;
    }

}
