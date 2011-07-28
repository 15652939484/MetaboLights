package uk.ac.ebi.metabolights.search;

import java.util.HashMap;

import uk.ac.ebi.bioinvindex.search.hibernatesearch.StudyBrowseField;

public class FilterSet extends HashMap<String, FilterItem>{
	private String name;
	private StudyBrowseField field;
	private String prefix="";
	private String suffix="";
	public FilterSet(String name, StudyBrowseField field, String prefix, String suffix){
		this.name = name;
		this.field = field;
		this.prefix = prefix;
		this.suffix = suffix;
	}
	public FilterSet(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public StudyBrowseField getField(){
		return field;
	}
	public String getPrefix(){
		return prefix;
	}
	public String getSuffix(){
		return suffix;
	}
}
