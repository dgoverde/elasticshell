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
package org.elasticsearch.shell.client.builders.cluster;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.shell.client.builders.AbstractRequestBuilder;
import org.elasticsearch.shell.json.JsonSerializer;

import java.io.IOException;

/**
 * @author Luca Cavanna
 *
 * Request builder for update cluster settings API
 */
@SuppressWarnings("unused")
public class UpdateClusterSettingsRequestBuilder<JsonInput, JsonOutput> extends AbstractRequestBuilder<ClusterUpdateSettingsRequest, ClusterUpdateSettingsResponse, JsonInput, JsonOutput> {

    public UpdateClusterSettingsRequestBuilder(Client client, JsonSerializer<JsonInput, JsonOutput> jsonSerializer) {
        super(client, new ClusterUpdateSettingsRequest(), jsonSerializer);
    }

    public UpdateClusterSettingsRequestBuilder<JsonInput, JsonOutput> transientSettings(JsonInput settings) {
        request.transientSettings(jsonToString(settings));
        return this;
    }

    public UpdateClusterSettingsRequestBuilder<JsonInput, JsonOutput> persistentSettings(JsonInput settings) {
        request.persistentSettings(jsonToString(settings));
        return this;
    }

    @Override
    protected ActionFuture<ClusterUpdateSettingsResponse> doExecute(ClusterUpdateSettingsRequest request) {
        return client.admin().cluster().updateSettings(request);
    }

    @Override
    protected XContentBuilder toXContent(ClusterUpdateSettingsRequest request, ClusterUpdateSettingsResponse response, XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.field(Fields.OK, true);
        builder.endObject();
        return builder;
    }
}
