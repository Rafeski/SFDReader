package com.company.talend.components.input;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.type.DataSet;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

@GridLayout({
        @GridLayout.Row("filePath"),
        @GridLayout.Row("connection")

        })


@DataSet("SDFReaderInputDataset")
@Documentation("SDF Reader dataset")
public class SDFReaderInputDataset implements Serializable {

    @Option
    @Documentation("Hazelcast connection")
    private SDFReaderInputDatastore connection;

    @Option
    @Documentation("SDF filepath")
    private String filePath;

    public String getFile() {
        return filePath;
    }

    public SDFReaderInputDataset setFile(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
