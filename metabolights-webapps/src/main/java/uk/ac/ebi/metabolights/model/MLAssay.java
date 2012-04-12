/*
 * EBI MetaboLights Project - 2012.
 *
 * File: MLAssay.java
 *
 * Modified by:   kenneth
 *
 * European Bioinformatics Institute, Wellcome Trust Genome Campus, Hinxton, Cambridgeshire, CB10 1SD, UK.
 */

package uk.ac.ebi.metabolights.model;

import uk.ac.ebi.bioinvindex.model.Annotation;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.AssayResult;
import uk.ac.ebi.bioinvindex.model.Metabolite;
import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.model.processing.Assay;
import uk.ac.ebi.bioinvindex.model.term.Factor;
import uk.ac.ebi.bioinvindex.model.term.FactorValue;
import uk.ac.ebi.metabolights.parallelcoordinates.ParallelCoordinatesDataSet;
import uk.ac.ebi.metabolights.parallelcoordinates.ParallelCoordinatesStrategyAllCombinations;
import uk.ac.ebi.metabolights.parallelcoordinates.ParallelCoordinatesStrategyFixed;

import java.util.ArrayList;
import java.util.List;

/**
 * THis is a Metabolights assay as it is needed in the webapp
 * @author conesa
 *
 */
public class MLAssay {
	private String technology;
	private String platform;
	private String measurement;
	private String fileName;
	private List<Factor> factors = new ArrayList();
	private List<AssayResult> assayResult = new ArrayList();
	private List<Assay> assayLines = new ArrayList();
	private List<MetaboliteGUI> metabolites = new ArrayList();
	private AssayGroup ag;
	private Study study;
	private ParallelCoordinatesDataSet pcds;
	
	
	public static String getAssayNameFromAssay(Assay assay){
		
		for (Annotation annotation: assay.getAnnotations()){
			if (annotation.getType().getValue().equals("assayFileId")){
				return annotation.getText();
			}
		}
		return "";
		
	}
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}
	public MLAssay(Assay assay){
		this.fileName = getAssayNameFromAssay(assay);
		this.technology = assay.getTechnologyName();
		this.platform = assay.getAssayPlatform();
		this.measurement = assay.getMeasurement().getName();

		
	}
	
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getMeasurement() {
		return measurement;
	}
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<Factor> getFactors(){
		return factors;
	}
	public List<AssayResult> getAssayResult(){
		return assayResult;
	}
	public void addAssayResult(AssayResult newAssayResult){
		assayResult.add(newAssayResult);
		checkFactors(newAssayResult);
			
	}
	public List<Assay> getAssayLines(){
		return assayLines;
	}
	public void addAssayLines(Assay newAssayLine){
		assayLines.add(newAssayLine);
		
	}
	private void checkFactors(AssayResult assayResult){
		
		// Get the factors ...
		for (FactorValue factorValue :assayResult.getData().getFactorValues()){
			// If its a new factor
			if (!HaveFactor(factorValue.getType())){
				factors.add(factorValue.getType());
			}
		}
	}
	private boolean HaveFactor(Factor newFactor){
		for (Factor factor:factors){
			if (factor.getValue().equals(newFactor.getValue())){
				return true;
			}
		}
		 return false;
	}
	public void setAssayGroup (AssayGroup ag){
		this.ag = ag;
		addGUIMetabolites();
		addParallelCoordinatesDataSet();
		
	}
	public List<MetaboliteGUI> getMetabolitesGUI(){
		return metabolites;
	}
	
	private void addGUIMetabolites(){
		
		for (Metabolite met: ag.getMetabolites()){
			
			// Only add metabolites known
			if (!met.getDescription().toLowerCase().startsWith("unk")){
			
				metabolites.add(new MetaboliteGUI(met));
				
			}
		}
		
	}
	private void addParallelCoordinatesDataSet(){
		
		
		new ParallelCoordinatesStrategyAllCombinations().Proccess(ag, study);
		// Get data for the parallel coordinates
		pcds = (new ParallelCoordinatesStrategyFixed()).Proccess(this.ag, null);

		
	}
	public ParallelCoordinatesDataSet getParallelCoordinatesDataset(){
		return pcds;
	}
	
}
