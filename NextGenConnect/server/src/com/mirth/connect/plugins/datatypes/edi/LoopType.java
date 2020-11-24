/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.1.2-b01-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2007.02.26 at 11:38:19 PM PST
//

package com.mirth.connect.plugins.datatypes.edi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for LoopType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoopType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/>
 *         &lt;element name="usage" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
 *         &lt;element name="pos" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="segment" type="{}SegmentType"/>
 *           &lt;element name="loop" type="{}LoopType" minOccurs="0"/>
 *           &lt;element ref="{}repeat"/>
 *           &lt;element name="syntax" type="{http://www.w3.org/2001/XMLSchema}normalizedString" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="xid" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoopType", propOrder = { "name", "usage", "pos", "segmentOrLoopOrRepeat" })
public class LoopType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String name;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String usage;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger pos;
    @XmlElementRefs({ @XmlElementRef(name = "segment", type = JAXBElement.class),
            @XmlElementRef(name = "repeat", type = JAXBElement.class),
            @XmlElementRef(name = "loop", type = JAXBElement.class),
            @XmlElementRef(name = "syntax", type = JAXBElement.class) })
    protected List<JAXBElement<?>> segmentOrLoopOrRepeat;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String xid;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String type;

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the usage property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the value of the usage property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setUsage(String value) {
        this.usage = value;
    }

    /**
     * Gets the value of the pos property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getPos() {
        return pos;
    }

    /**
     * Sets the value of the pos property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setPos(BigInteger value) {
        this.pos = value;
    }

    /**
     * Gets the value of the segmentOrLoopOrRepeat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the segmentOrLoopOrRepeat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getSegmentOrLoopOrRepeat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link JAXBElement
     * }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link LoopType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement
     * }{@code <}{@link SegmentType }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getSegmentOrLoopOrRepeat() {
        if (segmentOrLoopOrRepeat == null) {
            segmentOrLoopOrRepeat = new ArrayList<JAXBElement<?>>();
        }
        return this.segmentOrLoopOrRepeat;
    }

    /**
     * Gets the value of the xid property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getXid() {
        return xid;
    }

    /**
     * Sets the value of the xid property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setXid(String value) {
        this.xid = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setType(String value) {
        this.type = value;
    }

}
