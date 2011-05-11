goog.provide('specview.model.Spectrum');

goog.require('specview.model.Molecule');
goog.require('goog.math.Vec2');
goog.require('goog.debug.Logger');


/**
 * Class representing a spectrum
 * @constructor
 */

specview.model.Spectrum=function(optMolecule, optPeaklist)
{
        
        if (optMolecule)
            this.molecule=optMolecule;
        else
            this.molecule=null;


        if (optPeaklist)
            this.peakList=optPeaklist;
        else
            this.peakList=new Array();

        
    
	this.experiment="";
	this.NMRtype="";
	this.MS="";
	
	this.maxXpixel=0;
	this.minXpixel=0;
	
	this.xUnit=null;
	this.yUnit=null;

	};
goog.exportSymbol("specview.model.Spectrum", specview.model.Spectrum);

specview.model.Spectrum.prototype.logger = goog.debug.Logger.getLogger('specview.model.Spectrum');


/**
 * Set the xPixel coordinates of the peaks of the spectrum
 * @param maxValue
 */
specview.model.Spectrum.prototype.setXvalues=function(maxValue){
	var limitOfEditor=23.5;
	for(var k=0;k<this.peakList.length;k++){
		var peak=this.peakList[k];
		var newXvalueForPeak=(peak.xValue/maxValue)*limitOfEditor;
		peak.setXvalue(newXvalueForPeak);
	}
};


/**
 * returns bounding box of spectrum peaks
 * 
 * @return {goog.math.Box}
 */
specview.model.Spectrum.prototype.getBoundingBox = function() {
	return goog.math.Box.boundingBox.apply(null, goog.array.map(this.peakList,
			function(a) {
				return a.coord;
			}));
};

/**
 * Return the maximum intensity of all the peaks
 * @returns {Number}
 */
specview.model.Spectrum.prototype.getMaxHeightPeak=function(){
	var max=0;
	goog.array.forEach(this.peakList,function(peak){
		if(peak.intensity>max){
			max=peak.intensity;
		}
	});
	return max;
};


/**
 * 
 * @returns the maximum xValue of the peaks
 */
specview.model.Spectrum.prototype.getMaxValuePeak=function(){
	var max=0;
	goog.array.forEach(this.peakList,function(peak){
		if(peak.xValue>max){
			max=peak.xValue;
		}
	});
	return max;
};


/**
 * Return the array of the xValues of all peaks
 */
specview.model.Spectrum.prototype.getXvalues=function(){
	var array=[];
	goog.array.forEach(this.peakList,function(peak){
		array.push(parseFloat(peak.xValue));
	});
	return array;
};


/**
 * Called when zooming on the spectrum
 * The function ask for an array of xValues(represening the coordinates of the peaks of the spectrum)
 * Then sort the array.
 * @returns the sorted array
 */
specview.model.Spectrum.prototype.sortXvalues=function(){
	var array=this.getXvalues();
	array=array.sort(function(a,b){return a - b});
	return array;
};

specview.model.Spectrum.prototype.sortXpixel=function(){
	var array=this.getXpixel();
	array=array.sort(function(a,b){return a - b});
	return array;
};


/**
 * For a given xValue, the function look if there is a peak whose xVluae is associated to the parameter xValue.
 * If yes, then the peak is returned.
 * If no, return 0.
 * @param xValue
 * @returns {Number}
 */
specview.model.Spectrum.prototype.getPeakFromxValue=function(xValue){
	goog.array.forEach(this.peakList,function(peak){
		if(peak.xValue==xValue){
			return peak;
		}
	},this);
	return 0;
};

/**
 * 
 * @param array represents the array of xValue
 * @returns an array associating a given peak with its xValue
 */
specview.model.Spectrum.prototype.mapPeakToxValue=function(array){
	var arrayOfPeaks=[];
	for(k in array){
		for(p in this.peakList){
			if(this.peakList[p].xValue==array[k]){
//				alert("caca")
				arrayOfPeaks.push(this.peakList[p]);
			}
		}
	}
	return arrayOfPeaks;
};

/**
 * Function called when zooming on the spectrum
 * @param zoom
 * @param sortedArrayOfPeak
 * @returns the Nth max xPixel values.
 */
specview.model.Spectrum.prototype.getNthMaxValueOfPeak=function(zoom,sortedArrayOfPeak){
	var l=this.peakList.length;
	if(zoom<=l){
		return sortedArrayOfPeak[zoom].xValue;
	}
};

/**
 * Return an array of all the xPixel of the peaks of the spectrum
 * @returns {Array}
 */
specview.model.Spectrum.prototype.getXpixel=function(){
	var array=[];
	goog.array.forEach(this.peakList,function(peak){
		array.push(peak.xPixel);
	});
	return array;
};
/**
 * return the maximum and minimum xPixel value of peaks belonging to a spectrum
 * @returns {___array0}
 */
specview.model.Spectrum.prototype.getMaxAndMinXpixelValue=function(){
	var array=new Object();
	var arrayOfPixel=this.getXpixel();
	var max=0;
	var min=1.7976931348623157E+10308;
	for(k in arrayOfPixel){
		
		var value=arrayOfPixel[k];
		max=(arrayOfPixel[k]>max) ? arrayOfPixel[k] : max;
		min=(arrayOfPixel[k]<min) ? arrayOfPixel[k] : min;
	}
	array.maxPixel=max;
	array.minPixel=min;
	return array;
}

/**
 * return the maximum value of the peak(in the file unit)
 * @returns {Number}
 */
specview.model.Spectrum.prototype.getMaxValue = function(){
	var max=0;
	  goog.array.forEach(this.peakList,
			  function(peak) {
			      if (peak.xValue > max)
			          max=peak.xValue;
			  },
			  this);
	  return max;
}

/**
 * return the minimum value of the peak(in the file unit)
 * @returns {Number}
 */
specview.model.Spectrum.prototype.getMinValue = function(){
	var min=1000000000;
	  goog.array.forEach(this.peakList,
			  function(peak) {
			      if (peak.xValue < min)
			          min=peak.xValue;
			  },
			  this);
	  return min;
}

/**
 * return the minimum pixel value of the spectrum
 * @returns {Number}
 */
specview.model.Spectrum.prototype.getMinXvalue = function(){
	var min=1000000000;
	  goog.array.forEach(this.peakList,
			  function(peak) {
			      if (peak.xPixel < min)
			          min=peak.xPixel;
			  },
			  this);
	  return min;
}


specview.model.Spectrum.prototype.setExtremePixelValues=function(){
	var extremeValue=this.getMaxAndMinXpixelValue();
	this.maxXpixel=extremeValue.maxPixel;
	this.minXpixel=extremeValue.minPixel;
}



/**
 * Change the pixel coordinates of every peak of a spectrum when the spectrum is moved.
 * @param value
 * @param direction
 */
specview.model.Spectrum.prototype.reScaleCoordinates=function(value,direction){

	goog.array.forEach(this.peakList,function(peak){
		if(direction=="right"){
			peak.xPixel+=10;
			peak.xTpixel+=10;
		}else if(direction=="left"){
			peak.xPixel-=10;
			peak.xTpixel-=10;
		}
	});
};

specview.model.Spectrum.prototype.getMinPeak = function(){
	var minRef=1000000000;
	var minPeak=null;
	goog.array.forEach(this.peakList,function(peak){
		if(peak.xPixel<minRef){
			minPeak=peak;
		}
	});
	return minPeak;
}


specview.model.Spectrum.prototype.setCoordinatesAccordingToZoom = function(zoom,rightBoundOfTheBox){
	var minPixelValue=this.minXpixel;
	var ecart=rightBoundOfTheBox-minPixelValue;
	var factor=zoom*(ecart/100);
	
	var ArrayOfSortedPeak=spectrum.sortXpixel();
	
	var minPeak=this.getMinPeak();
	minPeak.xPixel=minPixelValue+factor;
	goog.array.forEach(this.peakList,function(peak){
		if(peak!=minPeak){
			var newXpixelValue=peak.xValue*minPeak.xPixel/peak.xPixel;
			peak.xPixel=newXpixelValue;
		}else{
			peak.xPixel=minPeak.xPixel;
		}
	});
	
//	this.logger.info(a);
};




/*
 * Description of the object
 */
specview.model.Spectrum.prototype.toString = function() {
	return "=====Object of a spectrum===\n\n " + "1-"+this.molecule + "\n"+"4-"+this.peakList;
};

