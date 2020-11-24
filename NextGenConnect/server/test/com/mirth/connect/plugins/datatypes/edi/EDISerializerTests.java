/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.datatypes.edi;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EDISerializerTests {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testToXml() throws Exception {
        String input = FileUtils.readFileToString(new File("tests/test-edi-input.txt"));
        String output = FileUtils.readFileToString(new File("tests/test-edi-output.xml"));
        EDISerializer serializer = new EDISerializer(new EDIDataTypeProperties().getSerializerProperties());
        Assert.assertEquals(output, serializer.toXML(input));
    }

    @Test
    public void testFromXml() throws Exception {
        String input = FileUtils.readFileToString(new File("tests/test-edi-output.xml"));
        String output = FileUtils.readFileToString(new File("tests/test-edi-input.txt"));
        EDISerializer serializer = new EDISerializer(new EDIDataTypeProperties().getSerializerProperties());
        Assert.assertEquals(output, serializer.fromXML(input));
    }

    /*
     * Checks if serializer adds missing elements when going from XML to EDI/X12.
     */
    @Test
    public void testIssue1597fromXML() throws Exception {
        String input = FileUtils.readFileToString(new File("tests/test-1597-input-missing-elements.xml"));
        String output = FileUtils.readFileToString(new File("tests/test-1597-output.txt"));
        EDISerializer serializer = new EDISerializer(new EDIDataTypeProperties().getSerializerProperties());
        Assert.assertEquals(output, serializer.fromXML(input));
    }

    @Test
    public void testIssue1597toXML() throws Exception {
        String input = FileUtils.readFileToString(new File("tests/test-1597-output.txt"));
        String output = FileUtils.readFileToString(new File("tests/test-1597-input.xml"));
        EDISerializer serializer = new EDISerializer(new EDIDataTypeProperties().getSerializerProperties());
        Assert.assertEquals(output, serializer.toXML(input));
    }
}
