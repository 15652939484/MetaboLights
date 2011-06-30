/**  
 * Copyright 2011 Mark Rijnbeek(markr@ebi.ac.uk)
 * 				  and Samy Deghou (deghou@polytech.unice.fr)
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
 */

goog.provide('specview.view.MoleculeRenderer');

goog.require('specview.view.BondRenderer');
goog.require('specview.view.BondRendererFactory');
goog.require('specview.view.AtomRenderer');
goog.require('specview.view.AromaticityRenderer');
goog.require('specview.graphics.ElementArray');

/**
 * Class to render a molecule object onto a graphics object.
 * 
 * @constructor
 * @param graphics
 *            {goog.graphics.AbstractGraphics} graphics to draw on.
 * @extends {specview.view.Renderer}
 */
specview.view.MoleculeRenderer = function(graphics, opt_config) {
    specview.view.Renderer.call(this, graphics,
    specview.view.MoleculeRenderer.defaultConfig, opt_config);
    this.bondRendererFactory = new specview.view.BondRendererFactory(graphics,
    this.config);
//    alert(this.config)
    this.atomRenderer = new specview.view.AtomRenderer(graphics, this.config);
    this.aromaticityRenderer = new specview.view.AromaticityRenderer(graphics, this.config);

};
goog.inherits(specview.view.MoleculeRenderer, specview.view.Renderer);

specview.view.MoleculeRenderer.prototype.logger = goog.debug.Logger.getLogger('specview.view.MoleculeRenderer');

specview.view.MoleculeRenderer.prototype.setScaleFactor = function(scale) {
	alert("here is the scale factor: (to remove this message alert go to molecule_renderer.js line 48) : "+scale);
    this.scale_factor = scale;
};

/**
 * This method is a substitute to the clear methods provided by the google closure library.
 * It 'clears' a molecule from the canvas by drawing a rectangle element (the molecule box) over it and filling it with
 * white.
 * @param box
 * @param graphics
 */
specview.view.MoleculeRenderer.prototype.clearMolecule = function(box,graphics){
    var fill = new goog.graphics.SolidFill('#FFFFFF');
    var stroke = new goog.graphics.Stroke(2, '#FFFFFF');
	graphics.drawRect(box[2].x-7,box[2].y-7,box[3].x,box[0].y,stroke,fill);
}


specview.view.MoleculeRenderer.prototype.render = function(molecule, transform, molecule_Box) {
    this.setTransform(transform);

	if (!molecule._elements){
	    molecule._elements = new specview.graphics.ElementArray();
	} else {
		molecule._elements.clear();
	}
    molecule.mustRecalcSSSR = true;

    var bondStroke = new goog.graphics.Stroke(
    this.config.get("bond")['stroke']['width'],
    this.config.get("bond")['stroke']['color']);
    var bondFill = new goog.graphics.SolidFill(
    this.config.get("bond")['fill']['color']);
    var bondPath = new goog.graphics.Path();

    goog.array.forEach(molecule.bonds,
        function(bond) { 
//    	this.logger.info(bond.stereo)
    		this.bondRendererFactory.get(bond).render(bond, transform, bondPath);
    	}, this);

    var aromRingRenderer = this.aromaticityRenderer;
    goog.array.forEach(molecule.getRings(),
    function(ring) {
        var aromatic_bonds = goog.array.filter(ring.bonds,
        function(b) {
            return b.aromatic;
        });
        if (aromatic_bonds.length == ring.bonds.length) {
            this._elements.add(
            aromRingRenderer.render(ring, transform, bondPath));
        }
    });
    molecule._elements.add(this.graphics.drawPath(bondPath, bondStroke, bondFill));
//	var centerOfMolecule = document.metaSpecObject.getMoleculeCenter();
    goog.array.forEach(molecule.atoms,
    function(atom) {
//    	this.logger.info("before :  "+atom.symbol+": "+atom.xPixel+"   ,   "+atom.yPixel);
//    	atom.xPixel += atom.xPixel + parseInt(specview.util.Utilities.parsePixel(document.getElementById("fieldSet").style.width))/2 - centerOfMolecule.x
//      	var c= point.x + parseInt(specview.util.Utilities.parsePixel(document.getElementById("fieldSet").style.width))/2 - centerOfMolecule.x;
//      	point = new goog.math.Coordinate(c,atom.yPixel);
//    	this.logger.info("after :  "+atom.symbol+": "+atom.xPixel+"   ,   "+atom.yPixel);
        this.atomRenderer.render(atom, transform, molecule._elements);
    },
    this);
//    alert(molecule_Box)
//    this.renderBoundingBox(molecule_Box,'red');

};


specview.view.MoleculeRenderer.prototype.renderBox = function(box){
	this.renderBoundingBox(box,"red");
}

/**
 * @param {specview.model.Molecule}
 *            molecule
 * @param {string=}
 *            opt_color
 * @param {specview.graphics.ElementArray=} opt_element_array
 * @return {specview.graphics.ElementArray}
 */

specview.view.MoleculeRenderer.prototype.highlightOn = function(molecule,
opt_color, opt_element_array) {

    if (!opt_color) {
        opt_color = this.config.get("highlight")['color'];
    }
    if (!opt_element_array) {
        opt_element_array = new specview.graphics.ElementArray();
    }

    goog.array.forEach(molecule.bonds,
    function(bond) {
        this.bondRendererFactory.get(bond).highlightOn(bond, opt_color, opt_element_array);
    },
    this);

    goog.array.forEach(molecule.atoms,
    function(atom) {
        this.atomRenderer.highlightOn(atom, opt_color, opt_element_array);
    },
    this);

    return opt_element_array;
};

/**
 * A default configuration for renderer
 */
specview.view.MoleculeRenderer.defaultConfig = {
    'bond': {
        'stroke': {
            'width': 1.5,
            'color': 'black'
        },
        'fill': {
            'color': 'black'
        }
    },
    'highlight': {
        'radius': .2,
        'color': 'blue'
    }
};
