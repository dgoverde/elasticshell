/*
 * Licensed to Luca Cavanna (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.shell.client.builders.indices;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.shell.client.builders.AbstractRequestBuilderJsonOutput;
import org.elasticsearch.shell.json.JsonToString;
import org.elasticsearch.shell.json.StringToJson;

import java.io.IOException;

import static org.elasticsearch.rest.action.support.RestActions.buildBroadcastShardsHeader;

/**
 * @author Luca Cavanna
 *
 * Request builder for flush API
 */
@SuppressWarnings("unused")
public class FlushRequestBuilder<JsonInput, JsonOutput> extends AbstractRequestBuilderJsonOutput<FlushRequest, FlushResponse, JsonInput, JsonOutput> {

    public FlushRequestBuilder(Client client, JsonToString<JsonInput> jsonToString, StringToJson<JsonOutput> stringToJson) {
        super(client, new FlushRequest(), jsonToString, stringToJson);
    }

    public FlushRequestBuilder<JsonInput, JsonOutput> indices(String... indices) {
        request.indices(indices);
        return this;
    }

    public FlushRequestBuilder<JsonInput, JsonOutput> refresh(boolean refresh) {
        request.refresh(refresh);
        return this;
    }

    public FlushRequestBuilder<JsonInput, JsonOutput> full(boolean full) {
        request.full(full);
        return this;
    }

    public FlushRequestBuilder<JsonInput, JsonOutput> force(boolean force) {
        request.force(force);
        return this;
    }

    @Override
    protected ActionFuture<FlushResponse> doExecute(FlushRequest request) {
        return client.admin().indices().flush(request);
    }

    @Override
    protected XContentBuilder toXContent(FlushRequest request, FlushResponse response, XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.field(Fields.OK, true);
        buildBroadcastShardsHeader(builder, response);
        builder.endObject();
        return builder;
    }
}
