package uk.ac.ebi.metabolights.parallelcoordinates;

/**
 * A unit for the parrallel coordinates javascript.
 * 
 * We need to build this:
 *	var units = {
 *		  st: {name: "Sample Type", unit: ""},
 *		  dsp: {name: "Deconvolution", unit: ""},
 *		  lbs: {name: "Alignment", unit: ""},
 *		  siv: {name: "Spike in volume", unit: " �l"},
 *		  year: {name: "mass", unit: " Da"}
 *	};
 *
 * siv --> abbreviation
 * Spyke in volume -->  name
 * �l --> unit
 * 
 * @author conesa
 *
 */
public class ParallelCoordinatesDimension {
	
	private String unit, abbreviation, name;
	
	public ParallelCoordinatesDimension(String abbreviation, String name, String unit){
		this.abbreviation = abbreviation;
		this.name = name;
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getAbbreviation() {
		return abbreviation;
	}


	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString(){

		// We need to return this
		//siv: {name: "Spike in volume", unit: " �l"},
		return (this.abbreviation + ": {name: \"" + this.name + "\", unit: \"" + this.unit + "\"}"); 
		
	}
}
