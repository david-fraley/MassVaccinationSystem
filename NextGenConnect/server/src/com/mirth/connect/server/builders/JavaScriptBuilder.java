/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.builders;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mirth.connect.client.core.ControllerException;
import com.mirth.connect.donkey.model.message.SerializationType;
import com.mirth.connect.model.Filter;
import com.mirth.connect.model.MetaData;
import com.mirth.connect.model.Rule;
import com.mirth.connect.model.Step;
import com.mirth.connect.model.Transformer;
import com.mirth.connect.model.codetemplates.CodeTemplate;
import com.mirth.connect.model.codetemplates.CodeTemplateLibrary;
import com.mirth.connect.model.codetemplates.ContextType;
import com.mirth.connect.model.util.JavaScriptConstants;
import com.mirth.connect.server.controllers.CodeTemplateController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.ExtensionController;
import com.mirth.connect.server.controllers.ScriptController;
import com.mirth.connect.server.message.DataTypeFactory;
import com.mirth.connect.util.CodeTemplateUtil;
import com.mirth.connect.util.ScriptBuilderException;

public class JavaScriptBuilder {
    private static Logger logger = Logger.getLogger(JavaScriptBuilder.class);
    private static ExtensionController extensionController = ControllerFactory.getFactory().createExtensionController();
    private static CodeTemplateController codeTemplateController = ControllerFactory.getFactory().createCodeTemplateController();

    /*
     * Generates the global JavaScript contained in all new scopes created
     */
    public static String generateGlobalSealedScript() {
        StringBuilder script = new StringBuilder();

        // add #trim() function to JavaScript String prototype
        script.append("String.prototype.trim = function() { return this.replace(/^\\s+|\\s+$/g,\"\").replace(/^\\t+|\\t+$/g,\"\"); };");

        for (MetaData metaData : extensionController.getConnectorMetaData().values()) {
            if (extensionController.isExtensionEnabled(metaData.getName()) && CollectionUtils.isNotEmpty(metaData.getUserutilPackages())) {
                for (String packageName : metaData.getUserutilPackages()) {
                    script.append("importPackage(").append(packageName).append(");\n");
                }
            }
        }

        for (MetaData metaData : extensionController.getPluginMetaData().values()) {
            if (extensionController.isExtensionEnabled(metaData.getName()) && CollectionUtils.isNotEmpty(metaData.getUserutilPackages())) {
                for (String packageName : metaData.getUserutilPackages()) {
                    script.append("importPackage(").append(packageName).append(");\n");
                }
            }
        }

        script.append("importPackage(Packages.com.mirth.connect.userutil);\n");
        script.append("importPackage(Packages.com.mirth.connect.server.userutil);\n");
        script.append("regex = new RegExp('');\n");
        script.append("xml = new XML('');\n");
        script.append("xmllist = new XMLList();\n");
        script.append("namespace = new Namespace();\n");
        script.append("qname = new QName();\n");

        /*
         * Ignore whitespace so blank lines are removed when deleting elements. This also involves
         * changing XmlProcessor.java in Rhino to account for Rhino issue 369394 and MIRTH-1405
         */
        script.append("XML.ignoreWhitespace=true;");
        // Setting prettyPrinting to true causes HL7 to break when converting back from HL7.
        script.append("XML.prettyPrinting=false;");

        return script.toString();
    }

    /*
     * Generation functions for general scripts
     */

    public static String generateScript(String channelId, String script, Set<String> scriptOptions, ContextType contextType) {
        StringBuilder builder = new StringBuilder();

        appendMapFunctions(builder);
        appendAttachmentFunctions(builder, scriptOptions);
        appendCodeTemplates(builder, channelId, contextType);
        appendMiscFunctions(builder);

        builder.append("function doScript() {\n" + script + " \n}\n");
        builder.append("doScript();\n");

        return builder.toString();
    }

    /*
     * Generation functions for specific phases
     */

    public static String generateDefaultKeyScript(String key) {
        return generateDefaultKeyScript(key, true);
    }

    public static String generateDefaultKeyScript(String key, boolean isGlobal) {
        String script = null;
        StringBuilder builder = new StringBuilder();

        if (key.equals(ScriptController.DEPLOY_SCRIPT_KEY)) {
            appendDefaultDeployScript(builder, isGlobal);
            script = builder.toString();
        } else if (key.equals(ScriptController.UNDEPLOY_SCRIPT_KEY)) {
            appendDefaultUndeployScript(builder, isGlobal);
            script = builder.toString();
        } else if (key.equals(ScriptController.PREPROCESSOR_SCRIPT_KEY)) {
            appendDefaultPreprocessorScript(builder, isGlobal);
            script = builder.toString();
        } else if (key.equals(ScriptController.POSTPROCESSOR_SCRIPT_KEY)) {
            appendDefaultPostprocessorScript(builder, isGlobal);
            script = builder.toString();
        }

        return script;
    }

    public static String generateFilterTransformerScript(Filter filter, Transformer transformer) throws ScriptBuilderException {
        logger.debug("generating script");

        StringBuilder builder = new StringBuilder();

        SerializationType inboundSerializationType = DataTypeFactory.getSerializationType(transformer.getInboundDataType(), transformer.getInboundProperties(), true);

        switch (inboundSerializationType) {
            case JSON:
                builder.append("msg = JSON.parse(connectorMessage.getTransformedData());\n");
                break;

            case XML:
                // Turn the inbound message into an E4X XML object
                builder.append("msg = new XML(connectorMessage.getTransformedData());\n");

                // Set the default namespace if there is one left on the root node, otherwise set it to ''.
                builder.append("if (msg.namespace('') != undefined) { default xml namespace = msg.namespace(''); } else { default xml namespace = ''; }\n");
                break;

            case RAW:
                builder.append("if (connectorMessage.getProcessedRawData() != null) { msg = new String(connectorMessage.getProcessedRawData()); } else { msg = new String(connectorMessage.getRawData()); } \n");
                break;
        }

        // Turn the outbound template into an E4X XML object, if there is one
        if (StringUtils.isNotBlank(transformer.getOutboundTemplate())) {
            SerializationType templateSerializationType = DataTypeFactory.getSerializationType(transformer.getOutboundDataType(), transformer.getOutboundProperties(), true);

            switch (templateSerializationType) {
                case JSON:
                    builder.append("tmp = JSON.parse(template);\n");
                    break;

                case XML:
                    builder.append("tmp = new XML(template);\n");
                    break;

                case RAW:
                    builder.append("tmp = template;\n");
                    break;
            }
        }

        // Append doFilter() function
        appendFilterScript(builder, filter);
        // Append doTransform() function
        appendTransformerScript(builder, transformer, false);
        // Append doFilter/doTransform execution
        builder.append("if (doFilter() == true) { doTransform(); return true; } else { return false; }");

        return builder.toString();
    }

    public static String generateResponseTransformerScript(Transformer transformer) throws ScriptBuilderException {
        logger.debug("Generating response transformer script...");

        StringBuilder builder = new StringBuilder();

        SerializationType inboundSerializationType = DataTypeFactory.getSerializationType(transformer.getInboundDataType(), transformer.getInboundProperties(), true);

        switch (inboundSerializationType) {
            case JSON:
                // Turn the inbound message into a JavaScript object
                builder.append("msg = JSON.parse(connectorMessage.getResponseTransformedData());\n");
                break;

            case XML:
                // Turn the inbound message into an E4X XML object
                builder.append("msg = new XML(connectorMessage.getResponseTransformedData());\n");

                // Set the default namespace if there is one left on the root node, otherwise set it to ''.
                builder.append("if (msg.namespace('') != undefined) { default xml namespace = msg.namespace(''); } else { default xml namespace = ''; }\n");
                break;

            case RAW:
                builder.append("msg = new String(response.getMessage()) \n");
                break;
        }

        if (StringUtils.isNotBlank(transformer.getOutboundTemplate())) {
            SerializationType templateSerializationType = DataTypeFactory.getSerializationType(transformer.getOutboundDataType(), transformer.getOutboundProperties(), true);

            switch (templateSerializationType) {
                case JSON:
                    // Turn the outbound template into a JavaScript object, if there is one
                    builder.append("tmp = JSON.parse(template);\n");
                    break;

                case XML:
                    // Turn the outbound template into an E4X XML object, if there is one
                    builder.append("tmp = new XML(template);\n");
                    break;

                case RAW:
                    builder.append("tmp = template;\n");
                    break;
            }
        }

        // Append doTransform() function
        appendTransformerScript(builder, transformer, true);
        // Append doTransform() execution
        builder.append("doTransform();");

        return builder.toString();
    }

    /*
     * General functions used by the generation methods to build scripts
     */

    private static void appendDefaultDeployScript(StringBuilder builder, boolean isGlobal) {
        if (isGlobal) {
            builder.append(JavaScriptConstants.DEFAULT_GLOBAL_DEPLOY_SCRIPT);
        } else {
            builder.append(JavaScriptConstants.DEFAULT_CHANNEL_DEPLOY_SCRIPT);
        }
    }

    private static void appendDefaultUndeployScript(StringBuilder builder, boolean isGlobal) {
        if (isGlobal) {
            builder.append(JavaScriptConstants.DEFAULT_GLOBAL_UNDEPLOY_SCRIPT);
        } else {
            builder.append(JavaScriptConstants.DEFAULT_CHANNEL_UNDEPLOY_SCRIPT);
        }
    }

    private static void appendDefaultPreprocessorScript(StringBuilder builder, boolean isGlobal) {
        if (isGlobal) {
            builder.append(JavaScriptConstants.DEFAULT_GLOBAL_PREPROCESSOR_SCRIPT);
        } else {
            builder.append(JavaScriptConstants.DEFAULT_CHANNEL_PREPROCESSOR_SCRIPT);
        }
    }

    private static void appendDefaultPostprocessorScript(StringBuilder builder, boolean isGlobal) {
        if (isGlobal) {
            builder.append(JavaScriptConstants.DEFAULT_GLOBAL_POSTPROCESSOR_SCRIPT);
        } else {
            builder.append(JavaScriptConstants.DEFAULT_CHANNEL_POSTPROCESSOR_SCRIPT);
        }
    }

    protected static void appendFilterScript(StringBuilder builder, Filter filter) throws ScriptBuilderException {
        List<Rule> enabledElements = filter.getEnabledElements();
        logger.debug("building javascript filter: enabled rule count=" + enabledElements.size());

        if (enabledElements.isEmpty()) {
            logger.debug("filter is empty, setting to accept all messages");
            builder.append("function doFilter() { phase[0] = 'filter'; return true; }");
        } else {
            for (ListIterator<Rule> iter = enabledElements.listIterator(); iter.hasNext();) {
                Rule rule = iter.next();
                builder.append("function filterRule" + iter.nextIndex() + "() {\n" + rule.getScript(true) + "\n}");
            }

            builder.append("function doFilter() { phase[0] = 'filter'; return (");

            // call each of the above functions in a big boolean expression
            for (ListIterator<Rule> iter = enabledElements.listIterator(); iter.hasNext();) {
                Rule rule = iter.next();
                String operator = "";

                if (Objects.equals(rule.getOperator(), Rule.Operator.AND)) {
                    operator = " && ";
                } else if (Objects.equals(rule.getOperator(), Rule.Operator.OR)) {
                    operator = " || ";
                }

                builder.append(operator + "(filterRule" + iter.nextIndex() + "() == true)");
            }

            builder.append("); }\n");
        }
    }

    protected static void appendTransformerScript(StringBuilder builder, Transformer transformer, boolean response) throws ScriptBuilderException {
        List<Step> enabledElements = transformer.getEnabledElements();
        logger.debug("building javascript transformer: enabled step count=" + enabledElements.size());

        // Set the phase and also reset the logger to transformer (it was filter before)
        builder.append("function doTransform() {");

        if (!response) {
            builder.append(" phase[0] = 'transformer'; logger = Packages.org.apache.log4j.Logger.getLogger(phase[0]);");
        }

        builder.append("\n\n\n");

        for (Step step : enabledElements) {
            logger.debug("adding step: " + step.getName());
            builder.append(step.getScript(true) + "\n");
        }

        builder.append("if ('xml' === typeof msg) {\n");
        builder.append("    if (msg.hasSimpleContent()) {\n");
        builder.append("        msg = msg.toXMLString();\n");
        builder.append("    }\n");
        builder.append("} else if ('undefined' !== typeof msg && msg !== null) {\n");
        builder.append("    var toStringResult = Object.prototype.toString.call(msg);\n");
        builder.append("    if (toStringResult == '[object Object]' || toStringResult == '[object Array]') {\n");
        builder.append("        msg = JSON.stringify(msg);\n");
        builder.append("    }\n");
        builder.append("}\n");

        builder.append("if ('xml' === typeof tmp) {\n");
        builder.append("    if (tmp.hasSimpleContent()) {\n");
        builder.append("        tmp = tmp.toXMLString();\n");
        builder.append("    }\n");
        builder.append("} else if ('undefined' !== typeof tmp && tmp !== null) {\n");
        builder.append("    var toStringResult = Object.prototype.toString.call(tmp);\n");
        builder.append("    if (toStringResult == '[object Object]' || toStringResult == '[object Array]') {\n");
        builder.append("        tmp = JSON.stringify(tmp);\n");
        builder.append("    }\n");
        builder.append("}\n");

        builder.append("}\n");
    }

    private static void appendMiscFunctions(StringBuilder builder) {
        // Script used to check for existence of segment
        builder.append("function validate(mapping, defaultValue, replacement) {\n");
        builder.append("    var result = mapping;\n");
        builder.append("    if ((result == undefined) || (result.toString().length == 0)) {\n");
        builder.append("        if (defaultValue == undefined) {\n");
        builder.append("            defaultValue = '';\n");
        builder.append("        }\n");
        builder.append("        result = defaultValue;\n");
        builder.append("    }\n");
        builder.append("    if ('string' === typeof result || result instanceof java.lang.String || 'xml' === typeof result) {\n");
        builder.append("        result = new java.lang.String(result.toString());\n");
        builder.append("        if (replacement != undefined) {\n");
        builder.append("            for (var i = 0; i < replacement.length; i++) { ");
        builder.append("                var entry = replacement[i];\n");
        builder.append("                result = result.replaceAll(entry[0], entry[1]);\n");
        builder.append("            }\n");
        builder.append("        }\n");
        builder.append("    }\n");
        builder.append("    return result;\n");
        builder.append("}\n");

        // Helper function to create segments
        builder.append("function createSegment(name, msgObj, index) {");
        builder.append("if (arguments.length == 1) { return new XML('<' + name + '></' + name + '>'); };");
        builder.append("if (arguments.length == 2) { index = 0; };");
        builder.append("msgObj[name][index] = new XML('<' + name + '></' + name + '>');");
        builder.append("return msgObj[name][index];");
        builder.append("}\n");

        // Helper function to create segments after specified field
        builder.append("function createSegmentAfter(name, segment) {");
        builder.append("var msgObj = segment;");
        builder.append("while (msgObj.parent() != undefined) { msgObj = msgObj.parent(); }");
        builder.append("msgObj.insertChildAfter(segment[0], new XML('<' + name + '></' + name + '>'));");
        builder.append("return msgObj.child(segment[0].childIndex() + 1);");
        builder.append("}\n");

        // Helper function to get the length of an XMLList or array
        builder.append("function getArrayOrXmlLength(obj) {\n");
        builder.append("    if (typeof obj == 'xml' || obj instanceof java.lang.String) {\n");
        builder.append("        return obj.length();\n");
        builder.append("    } else if (typeof obj != 'undefined' && obj != null) {\n");
        builder.append("        return obj.length || 0;\n");
        builder.append("    }\n");
        builder.append("    return 0;\n");
        builder.append("}\n");

        // Helper function to create a new String but leave undefined/null values alone
        builder.append("function newStringOrUndefined(value) {\n");
        builder.append("    if ('undefined' !== typeof value && value != null) {\n");
        builder.append("        value = new String(value);\n");
        builder.append("    }\n");
        builder.append("    return value;\n");
        builder.append("}\n");

        // Helper function to create a new Boolean but leave undefined/null values alone
        builder.append("function newBooleanOrUndefined(value) {\n");
        builder.append("    if ('undefined' !== typeof value && value != null) {\n");
        builder.append("        value = new Boolean(value);\n");
        builder.append("    }\n");
        builder.append("    return value;\n");
        builder.append("}\n");

        // Helper function to create a new Number but leave undefined/null values alone
        builder.append("function newNumberOrUndefined(value) {\n");
        builder.append("    if ('undefined' !== typeof value && value != null) {\n");
        builder.append("        value = new Number(value);\n");
        builder.append("    }\n");
        builder.append("    return value;\n");
        builder.append("}\n");

        /*
         * Since we use a sealed shared scope everywhere, importClass won't be available. To allow
         * this to still work for migration, we override importClass to call importPackage instead.
         */
        builder.append("importClass = function() {\n");
        builder.append("    logger.error('The importClass method has been deprecated and will soon be removed. Please use importPackage or the fully-qualified class name instead.');\n");
        builder.append("    for each (argument in arguments) {\n");
        builder.append("        var className = new Packages.java.lang.String(argument);\n");
        builder.append("        if (className.startsWith('class ')) {\n");
        builder.append("            className = className.substring(6);\n");
        builder.append("        }\n");
        builder.append("        eval('importPackage(' + Packages.java.lang.Class.forName(className).getPackage().getName() + ')');\n");
        builder.append("    }\n");
        builder.append("}\n");

        // TODO: Replace the above with this in 3.1
        // builder.append("importClass = undefined;\n");
    }

    private static void appendMapFunctions(StringBuilder builder) {
        builder.append("function $co(key, value) { if (arguments.length == 1) { return connectorMap.get(key); } else { return connectorMap.put(key, value); } }\n");
        builder.append("function $c(key, value) { if (arguments.length == 1) { return channelMap.get(key); } else { return channelMap.put(key, value); } }\n");
        builder.append("function $s(key, value) { if (arguments.length == 1) { return sourceMap.get(key); } else { return sourceMap.put(key, value); } }\n");
        builder.append("function $gc(key, value) { if (arguments.length == 1) { return globalChannelMap.get(key); } else { return globalChannelMap.put(key, value); } }\n");
        builder.append("function $g(key, value) { if (arguments.length == 1) { return globalMap.get(key); } else { return globalMap.put(key, value); } }\n");
        builder.append("function $cfg(key, value) { if (arguments.length == 1) { return configurationMap.get(key); } else { return configurationMap.put(key, value); } }\n");
        builder.append("function $r(key, value) { if (arguments.length == 1) { return responseMap.get(key); } else { return responseMap.put(key, value); } }\n");

        // No need to check the code context here; the function checks whether each individual map exists first 
        builder.append("function $(string) { ");
        builder.append("try { if(responseMap.containsKey(string)) { return $r(string); } } catch(e){}");
        builder.append("try { if(connectorMap.containsKey(string)) { return $co(string); } } catch(e){}");
        builder.append("try { if(channelMap.containsKey(string)) { return $c(string); } } catch(e){}");
        builder.append("try { if(sourceMap.containsKey(string)) { return $s(string); } } catch(e){}");
        builder.append("try { if(globalChannelMap.containsKey(string)) { return $gc(string); } } catch(e){}");
        builder.append("try { if(globalMap.containsKey(string)) { return $g(string); } } catch(e){}");
        builder.append("try { if(configurationMap.containsKey(string)) { return $cfg(string); } } catch(e){}");
        // TODO: This is temporary for the database reader and should not stay
        builder.append("try { if(resultMap.containsKey(string)) { return resultMap.get(string); } } catch(e){}");
        builder.append("return ''; }\n");
    }

    private static void appendAttachmentFunctions(StringBuilder builder, Set<String> scriptOptions) {
        // Helper function to access attachment IDs (returns List<String>)
        builder.append("function getAttachmentIds(channelId, messageId) {\n");
        builder.append("    if (arguments.length == 2) {\n");
        builder.append("        return AttachmentUtil.getMessageAttachmentIds(channelId, messageId);\n");
        builder.append("    } else {\n");
        builder.append("        return AttachmentUtil.getMessageAttachmentIds(connectorMessage);\n");
        builder.append("    }\n");
        builder.append("}\n");

        // Helper function to access attachments (returns List<Attachment>)
        builder.append("function getAttachments(base64Decode) {\n");
        builder.append("    return AttachmentUtil.getMessageAttachments(connectorMessage, !!base64Decode || false);\n");
        builder.append("}\n");

        // Helper function to access a specific attachment (returns Attachment)
        builder.append("function getAttachment() {\n");
        builder.append("    if (arguments.length >= 3) {\n");
        builder.append("        return AttachmentUtil.getMessageAttachment(arguments[0], arguments[1], arguments[2], !!arguments[3] || false);\n");
        builder.append("    } else {\n");
        builder.append("        return AttachmentUtil.getMessageAttachment(connectorMessage, arguments[0], !!arguments[1] || false);\n");
        builder.append("    }\n");
        builder.append("}\n");

        // Helper function to update a specific attachment (returns Attachment)
        builder.append("function updateAttachment() {\n");
        builder.append("    if (arguments.length >= 5) {\n");
        builder.append("        return AttachmentUtil.updateAttachment(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], !!arguments[5] || false);\n");
        builder.append("    } else if (arguments.length >= 3) {\n");
        builder.append("        if (arguments[2] && arguments[2] instanceof Attachment) {\n");
        builder.append("            return AttachmentUtil.updateAttachment(arguments[0], arguments[1], arguments[2], !!arguments[3] || false);\n");
        builder.append("        } else {\n");
        builder.append("            return AttachmentUtil.updateAttachment(connectorMessage, arguments[0], arguments[1], arguments[2], !!arguments[3] || false);\n");
        builder.append("        }\n");
        builder.append("    } else {\n");
        builder.append("        return AttachmentUtil.updateAttachment(connectorMessage, arguments[0], !!arguments[1] || false);\n");
        builder.append("    }\n");
        builder.append("}\n");

        // Helper function to set attachment
        if (scriptOptions != null && scriptOptions.contains("useAttachmentList")) {

            builder.append("function addAttachment(data, type, base64Encode) {\n");
            builder.append("return AttachmentUtil.addAttachment(mirth_attachments, data, type, !!base64Encode || false);\n");
            builder.append("}\n");
        } else {
            builder.append("function addAttachment(data, type, base64Encode) {\n");
            builder.append("return AttachmentUtil.createAttachment(connectorMessage, data, type, !!base64Encode || false);\n");
            builder.append("}\n");
        }
    }

    private static void appendCodeTemplates(StringBuilder builder, String channelId, ContextType contextType) {
        try {
            Map<String, CodeTemplate> codeTemplateMap = new HashMap<String, CodeTemplate>();
            for (CodeTemplate template : codeTemplateController.getCodeTemplates(null)) {
                codeTemplateMap.put(template.getId(), template);
            }

            for (CodeTemplateLibrary library : codeTemplateController.getLibraries(null, false)) {
                for (CodeTemplate template : library.getCodeTemplates()) {
                    // Ignore code templates that have already been handled
                    // Only add the code template if the library is enabled for this channel, or if it's not explicitly disabled and new channels are being included 
                    if (codeTemplateMap.containsKey(template.getId()) && (channelId == null || library.getEnabledChannelIds().contains(channelId) || (!library.getDisabledChannelIds().contains(channelId) && library.isIncludeNewChannels()))) {
                        CodeTemplate serverCodeTemplate = codeTemplateMap.get(template.getId());
                        if (serverCodeTemplate.isAddToScripts() && serverCodeTemplate.getContextSet().contains(contextType)) {
                            builder.append(CodeTemplateUtil.stripDocumentation(serverCodeTemplate.getCode()));
                            builder.append('\n');
                        }
                    }

                    // This code template has been handled, remove it from the map
                    codeTemplateMap.remove(template.getId());
                }
            }
        } catch (ControllerException e) {
            logger.error("Could not append code templates.", e);
        }
    }
}
