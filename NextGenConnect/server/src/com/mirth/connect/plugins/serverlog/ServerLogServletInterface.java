/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.serverlog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.Operation.ExecuteType;
import com.mirth.connect.client.core.api.BaseServletInterface;
import com.mirth.connect.client.core.api.MirthOperation;
import com.mirth.connect.client.core.api.Param;

@Path("/extensions/serverlog")
@Tag(name = "Extension Services")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public interface ServerLogServletInterface extends BaseServletInterface {

    public static final String PLUGIN_POINT = "Server Log";
    public static final String PERMISSION_VIEW = "View Server Log";

    @GET
    @Path("/")
    @Operation(summary = "Retrieves server log entries.")
    @ApiResponse(content = { @Content(mediaType = MediaType.APPLICATION_XML, examples = {
            @ExampleObject(name = "serverLogList", ref = "../apiexamples/server_log_item_list_xml") }),
            @Content(mediaType = MediaType.APPLICATION_JSON, examples = {
                    @ExampleObject(name = "serverLogList", ref = "../apiexamples/server_log_item_list_json") }) })
    @MirthOperation(name = "getMirthServerLogs", display = "View Server Log", permission = PERMISSION_VIEW, type = ExecuteType.ASYNC, auditable = false)
    public List<ServerLogItem> getServerLogs(// @formatter:off
            @Param("fetchSize") @Parameter(description = "Specifies the maximum number of log items to return.", required = true, schema = @Schema(defaultValue = "100")) @QueryParam("fetchSize") int fetchSize,
            @Param("lastLogId") @Parameter(description = "The last log ID the client retrieved. Only log items with a greater ID will be returned.") @QueryParam("lastLogId") Long lastLogId) throws ClientException;
    // @formatter:on
}