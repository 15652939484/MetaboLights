goog.provide('specview.model.Spectrum');

goog.require('specview.model.Molecule');


/**
 * Class representing a spectrum
 * @constructor
 */

specview.model.Spectrum=function(optMolecule, optPeaklist)
{
	this.molecule=null;
	this.peakList=new Array();

	this.experiment="";
	this.NMRtype="";
	this.MS="";

	};
goog.exportSymbol("specview.model.Spectrum", specview.model.Spectrum);


/*
 * Description of the object
 */
specview.model.Spectrum.prototype.toString = function() {
	return "=====Object of a spectrum===\n\n " + "1-"+this.molecule + "\n"+"4-"+this.peakList;
};

