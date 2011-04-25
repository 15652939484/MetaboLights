/**
 * Copyright 2011 Mark Rijnbeek (markr@ebi.ac.uk)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
goog.provide('specview.view.SpectrumRenderer');
goog.require('specview.view.Renderer');
goog.require('goog.debug.Logger');
goog.require('specview.graphics.ElementArray');
goog.require('goog.array');

/**
 * Class to render a spectrum, a list of peaks really
 * 
 * @param {goog.graphics.AbstractGraphics} something to draw on
 * @param {Object=} opt_config override default configuration
 * @constructor
 * @extends {specview.view.Renderer}
 */
specview.view.SpectrumRenderer = function(graphics, opt_config, opt_box) {
	specview.view.Renderer.call(this, graphics, specview.view.SpectrumRenderer.defaultConfig, opt_config);
    this.box= opt_box;
};
goog.inherits(specview.view.SpectrumRenderer, specview.view.Renderer);

specview.view.SpectrumRenderer.prototype.setBoundsBasedOnMolecule = function(molecule) {

    var molBox = molecule.getBoundingBox();
    molHeight=Math.abs(molBox.top-molBox.bottom);
    molWidth=Math.abs(molBox.left-molBox.right);
    size=Math.max(molHeight,molWidth);
    top=molBox.bottom;
    bottom=top-size;
    left= 1.1*molBox.right;
    right=left+size;
    bottom=-5.5;
    right=23.5;

    this.box = new goog.math.Box(top,right,bottom,left);
    
    //this.box = new goog.math.Box(top,right,bottom,left);//THIS IS THE GOOD BOX FOR INVERTING
//   alert("top"+this.box.top+"\nleft"+this.box.left+"\nright:"+this.box.right+"\nbottom:"+this.box.bottom)
    
    /*
    var gSize=graphics.getSize();
    b = new goog.math.Box();
    b.left = gSize.width/2;
    b.right = gSize.width*(2/3);
    b.top = gSize.height*(5/6);
    b.bottom = gSize.height*(1/6);
    this.box=b;
    this.logger.info(this.box.top+" "+this.box.bottom+" "+this.box.left+" "+this.box.right+" ");
    */
}
specview.view.SpectrumRenderer.prototype.setBoundsBasedOnMetaSpecmetaSpecObject=function(metaSpec){
	var molecule=metaSpec.molecule;
	var spectrum=metaSpec.spectrum;
    var specBox = spectrum.getBoundingBox();
    var molBox=molecule.getBoundingBox();
    this.logger.info("In spectrum renderer: "+molBox);
    specHeight=Math.abs(specBox.top-specBox.bottom);
    specWidth=Math.abs(specBox.left-specBox.right);
    size=Math.max(specHeight,specWidth);
    top=molBox.bottom;//THE TOP IS FUNCTION OF THE MOLECULE
    bottom=top-size;
    left= 1.1*molBox.right;//THE LEFT IS FUNCTION OF THE MOLECULE
    right=left+size;
    this.box = new goog.math.Box(top,right,bottom,left);
}








/**
 * The spectrum is simply the object
 * Transform is static and has been set up in specview.controller.Controller.prototype.render. 
 */
specview.view.SpectrumRenderer.prototype.render = function(metaSpecObject, transform) {
	var spectrum=metaSpecObject.spectrum;
    this.setTransform(transform);
//    this.logger.info("spectrum_renderer: "+this.box)
    this.renderBoundingBox(this.box,'red'); 
    var peakPath = new goog.graphics.Path();
    var peakStroke = new goog.graphics.Stroke(1.05,'black');
    var peakFill = null;   
/*
    var minX=spectrum.peakList[0].xValue;
    var maxX=spectrum.peakList[0].xValue;
    
   goog.array.forEach(spectrum.peakList,
  function(peak) {
     if (peak.xValue < minX)
        minx=peak.xValue;
       if (peak.xValue > maxX)
            maxX=peak.xValue;
    },
    this);
    
    var xAxisLen=(this.box.right-this.box.left)*0.8;
    this.logger.info(minX+" "+maxX +  "  "+xAxisLen);

    var correct = xAxisLen/(maxX-minX);
    var rapport=23.5/maxX;
    
    var xStart= this.box.left*1.1;    
    var yStart=this.box.bottom

    var maxHeightOfPeak=spectrum.getMaxHeightPeak();
    var maxValueOfPeak=spectrum.getMaxValuePeak();
    var pTo=0;
    var pFrom=0;
    
    var adjustXvalue;
    var boxCoords=this.transform.transformCoords([new goog.math.Coordinate(this.box.left,this.box.top),new goog.math.Coordinate(this.box.right,this.box.bottom)]);
    */
    //Draw the peaks
    goog.array.forEach(spectrum.peakList,
    function(peak) {
        peakPath.moveTo(peak.xPixel, peak.yPixel); 
        peakPath.lineTo(peak.xTpixel,peak.yTpixel);
    },
    this);
    this.graphics.drawPath(peakPath, peakStroke, peakFill);

};
	

/*
specview.view.SpectrumRenderer.prototype.highlightOn = function(peak) {
	
    var xStart= this.box.left*1.1;    
    var yStart= this.box.top;   
//	this.logger.info(peak)
	var correct=0.0298;
	var pCoords=this.transform.transformCoords([new goog.math.Coordinate(this.box.left*1.1+(peak.xValue*correct), this.box.top )]);
//	this.logger.info(pCoords[0])
	var strokeWidth = 2.4;
	opt_element_array = new specview.graphics.ElementArray();
	var fill = new goog.graphics.SolidFill("#55bb00", .3);
	var radius = 8.80
//	var coords = this.transform.transformCoords([ atom.coord ])[0];//TODO
    var peakFrom =new goog.math.Coordinate(xStart+(peak.xValue*correct), yStart );
    var peakTo =new goog.math.Coordinate(xStart+(peak.xValue*correct), (this.box.top+this.box.bottom)*peak.intensity/62  );
    var peakCoords = this.transform.transformCoords( [peakFrom, peakTo]);
    opt_element_array.add(this.graphics.drawRect(peakCoords[0].x,peakCoords[0].y,7,peakCoords[1].y,null,fill));
	return opt_element_array;
};

*/

specview.view.SpectrumRenderer.prototype.highlightOn = function(peak,editor) {
	opt_element_array = new specview.graphics.ElementArray();
	var fill = new goog.graphics.SolidFill("#55bb00", .3);
    var peakPath = new goog.graphics.Path();
    peakPath.moveTo(peak.xPixel, peak.yPixel); 
    peakPath.lineTo(peak.xTpixel,peak.yTpixel);
    opt_element_array.add(this.graphics.drawPath(peakPath,new goog.graphics.Stroke(2,'blue'),null));
//    opt_element_array.add(this.graphics.drawText(50, 150, 100, 500, 600,'center', null, font, stroke, fill));
	return opt_element_array;
};


specview.view.SpectrumRenderer.logger = goog.debug.Logger.getLogger('specview.view.SpectrumRenderer');
