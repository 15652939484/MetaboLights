/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * Last modified: 27/09/13 14:43
 * Modified by:   kenneth
 *
 * Copyright 2013 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 */

package uk.ac.ebi.metabolights.repository.model;

import java.io.File;
import java.util.Collection;

public class MetaboliteAssignment {

    public String metaboliteAssignmentFileName;
    public String metaboliteAssignmentFileNameUriSafe;
    Collection<MetaboliteAssignmentLine> metaboliteAssignmentLines;

    public String getMetaboliteAssignmentFileName() {
        return metaboliteAssignmentFileName;
    }

    public void setMetaboliteAssignmentFileName(String metaboliteAssignmentFileName) {
        this.metaboliteAssignmentFileName = metaboliteAssignmentFileName;
    }

    public String getMetaboliteAssignmentFileNameUriSafe() {
        return metaboliteAssignmentFileNameUriSafe;
    }

    public void setMetaboliteAssignmentFileNameUriSafe(String metaboliteAssignmentFileNameUriSafe) {

        metaboliteAssignmentFileNameUriSafe = metaboliteAssignmentFileName;

        if (metaboliteAssignmentFileNameUriSafe.contains(File.separator))
            metaboliteAssignmentFileNameUriSafe = metaboliteAssignmentFileNameUriSafe.replaceAll(File.separator,"__");

        if (metaboliteAssignmentFileNameUriSafe.contains(" "))
            metaboliteAssignmentFileNameUriSafe = metaboliteAssignmentFileNameUriSafe.replaceAll(" ","+");

        this.metaboliteAssignmentFileNameUriSafe = metaboliteAssignmentFileNameUriSafe;
    }

    public Collection<MetaboliteAssignmentLine> getMetaboliteAssignmentLines() {
        return metaboliteAssignmentLines;
    }

    public void setMetaboliteAssignmentLines(Collection<MetaboliteAssignmentLine> metaboliteAssignmentLines) {
        this.metaboliteAssignmentLines = metaboliteAssignmentLines;
    }

    public enum fieldNames {

        //setters vs maf column names
        identifier("identifier"),             //V1
        databaseIdentifier("database_identifier"),  //V2
        unitId("unit_id"),
        chemicalFormula("chemical_formula"),
        smiles("smiles"),
        inchi("inchi"),
        description("description"),       //V1
        metaboliteIdentification("metabolite_identification"),     //V2
        chemicalShift("chemical_shift"),
        multiplicity("multiplicity"),
        massToCharge("mass_to_charge"),
        fragmentation("fragmentation"),
        modifications("modifications"),
        charge("charge"),
        retentionTime("retention_time"),
        taxid("taxid"),
        species("species"),
        database("database"),
        databaseVersion("database_version"),
        reliability("reliability"),
        uri("uri"),
        searchEngine("search_engine"),
        searchEngineScore("search_engine_score"),
        smallmoleculeAbundanceSub("smallmolecule_abundance_sub"),
        smallmoleculeAbundanceStdevSub("smallmolecule_abundance_stdev_sub"),
        smallmoleculeAbundanceStdErrorSub("smallmolecule_abundance_std_error_sub");

        private final String name;

        private fieldNames(String toString) {
            this.name = toString;
        }

        public String toString() {
            return name;
        }

    }
}
