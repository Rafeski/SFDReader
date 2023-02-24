package com.innospec.talend.components.input;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.type.DataStore;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.configuration.ui.widget.Credential;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

@GridLayout({
        @GridLayout.Row("clusterIpAddress"),
        @GridLayout.Row({"groupName", "password"})
})
@DataStore("SDFReaderInputDatastore")
@Documentation("Hazelcast Datastore configuration")
public class SDFReaderInputDatastore implements Serializable {

    @Option
    @Documentation("cluster ip address")
    private String clusterIpAddress;

    @Option
    @Documentation("cluster group name")
    private String groupName;

    @Option
    @Credential
    @Documentation("cluster password")
    private String password;

}
