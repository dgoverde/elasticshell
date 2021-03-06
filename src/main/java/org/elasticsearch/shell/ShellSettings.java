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
package org.elasticsearch.shell;

import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.FailedToResolveConfigException;

import java.io.File;

/**
 * @author Luca Cavanna
 *
 * Holds the configuration settings for the shell
 * Uses and exposes the elasticsearch Settings object to load and provide the settings from file system
 */
public class ShellSettings {

    public static final String DEFAULT_NODE_NAME = "elasticshell";
    public static final String DEFAULT_CLUSTER_NAME = ClusterName.DEFAULT.value();
    public static final String DEFAULT_TRANSPORT_HOST = "localhost";
    public static final Integer DEFAULT_TRANSPORT_PORT = 9300;

    public static final String CLUSTER_NAME = "cluster.name";

    public static final String NODE_NAME = "node.name";

    public static final String TRANSPORT_HOST = "transport.host";
    public static final String TRANSPORT_PORT = "transport.port";

    public static final String DEFAULT_STARTUP_SCRIPT = ".elasticshellrc.js";
    public static final String STARTUP_SCRIPT = "startup.script";

    public static final String HISTORY_FILE = ".elasticshell_history";

    public static final String PLAYGROUND_MODE = "playground.mode";

    public static final String SUGGESTIONS_MAX = "suggestions.max";

    public static final String WELCOME_MESSAGE = "welcome";
    public static final String PROMPT_MESSAGE = "prompt";
    public static final String BYE_MESSAGE = "bye";

    private final Settings settings;

    @Inject
    public ShellSettings() {
        this.settings = loadSettings();
    }

    public Settings settings() {
        return settings;
    }

    Settings loadSettings() {

        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
        Environment environment = new Environment();

        try {
            settingsBuilder.loadFromUrl(environment.resolveConfig("elasticshell.yml"));
        } catch (FailedToResolveConfigException e) {
            // ignore
        } catch (NoClassDefFoundError e) {
            // ignore, no yaml
        }
        try {
            settingsBuilder.loadFromUrl(environment.resolveConfig("elasticshell.json"));
        } catch (FailedToResolveConfigException e) {
            // ignore
        }
        try {
            settingsBuilder.loadFromUrl(environment.resolveConfig("elasticshell.properties"));
        } catch (FailedToResolveConfigException e) {
            // ignore
        }

        settingsBuilder.replacePropertyPlaceholders();


        String name = settingsBuilder.get(NODE_NAME);
        if (name == null || name.isEmpty()) {
            name = DEFAULT_NODE_NAME;
        }

        if (name != null) {
            settingsBuilder.put(NODE_NAME, name);
        }


        // put the cluster name
        if (settingsBuilder.get(ClusterName.SETTING) == null) {
            settingsBuilder.put(ClusterName.SETTING, DEFAULT_CLUSTER_NAME);
        }

        if (settingsBuilder.get(TRANSPORT_HOST) == null) {
            settingsBuilder.put(TRANSPORT_HOST, DEFAULT_TRANSPORT_HOST);
        }

        if (settingsBuilder.get(TRANSPORT_PORT) == null) {
            settingsBuilder.put(TRANSPORT_PORT, DEFAULT_TRANSPORT_PORT);
        }

        if (settingsBuilder.get(PLAYGROUND_MODE) == null) {
            settingsBuilder.put(PLAYGROUND_MODE, false);
        }

        if (settingsBuilder.get(STARTUP_SCRIPT) == null) {
            //we look in the user home only if the startup script is not set in the configuration
            //otherwise we expect a path, which can of course be relative
            String userHome = System.getProperty( "user.home" );
            File startupScriptFile = new File(userHome, DEFAULT_STARTUP_SCRIPT);
            if (startupScriptFile.exists()) {
                settingsBuilder.put(STARTUP_SCRIPT, startupScriptFile.getAbsolutePath());
            }
        }

        return settingsBuilder.build();
    }

}