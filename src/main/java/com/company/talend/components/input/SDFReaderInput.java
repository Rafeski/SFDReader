package com.company.talend.components.input;

import org.openscience.cdk.Atom;
import org.openscience.cdk.exception.*;
import org.openscience.cdk.silent.*;
import org.openscience.cdk.smiles.*;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.input.Emitter;
import org.talend.sdk.component.api.input.Producer;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.record.Record;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.Serializable;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;

import java.io.*;
import java.util.*;

@Version
@Icon(value = Icon.IconType.CUSTOM, custom = "molecule_32x32")
@Emitter(name = "Input")
@Documentation("SDF Reader source")
public class SDFReaderInput implements Serializable {
    private final SDFReaderInputDataset dataset;
    private final RecordBuilderFactory recordBuilderFactory;
    private transient IteratingSDFReader mdliter;
    private transient FileReader sdFile;


    public SDFReaderInput(@Option("configuration") final SDFReaderInputDataset configuration,
                          final RecordBuilderFactory recordBuilderFactory) {
        this.dataset = configuration;
        this.recordBuilderFactory = recordBuilderFactory;
    }

    @PostConstruct
    public void init() throws IOException {
        //Here we can init connections
        String filePath = dataset.getFile();
        System.out.println("[" + filePath + "]");

        sdFile = null;
        try {
            /* CDK does not automatically understand gzipped files */
            sdFile = new FileReader(new File(filePath));
        } catch (FileNotFoundException e) {
            System.err.println("File " + filePath + " not found");
            System.exit(1);
        }
    }

    @Producer
    public Record next() throws CDKException {
        // provide a record every time it is called. Returns null if there is no more data
        if (mdliter == null) {
            mdliter = new IteratingSDFReader(sdFile, DefaultChemObjectBuilder.getInstance());
        }

        // no more molecules
        if (!mdliter.hasNext()) {
            return null;
        }

        // there is a molecule
        IAtomContainer mol = mdliter.next();
        IMolecularFormula mf = MolecularFormulaManipulator.getMolecularFormula(mol);
        System.out.println(MolecularFormulaManipulator.getString(mf));
        int numHeavies = 0;
        for (IAtom atom : mol.atoms()) {
            if (atom.getAtomicNumber() > 1) {

            }
                numHeavies++;
//            }
//        }
//        return recordBuilderFactory.newRecordBuilder().withInt("count", numHeavies).build();

//        System.out.println("TotalMass:" + totMass.toString());
        SmilesGenerator smiles = new SmilesGenerator(SmiFlavor.Absolute);

        return recordBuilderFactory.newRecordBuilder()
                .withInt("SingleElectronCount", mol.getSingleElectronCount())
                .withString("Title", mol.getTitle())
                .withString("MolecularFormula", MolecularFormulaManipulator.getString(mf))
                .withInt("AtomCount", mol.getAtomCount())
                .withInt("BondCount", mol.getBondCount())
                .withString("SMILES", smiles.create(mol))
                .withString("atoms",mol.atoms().toString())
                .build();

    }

    @PreDestroy
    public void release() throws IOException {
        // clean and release any resources
        if (mdliter != null) {
            try {
                mdliter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sdFile != null) {
            try {
                sdFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
