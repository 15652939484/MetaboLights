/**
 * Copyright 2011 Samy Deghou (deghou@polytech.unice.fr)
 * and  Mark Rijnbeek (markr@ebi.ac.uk)
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

goog.provide('specview.io.SpectrumCMLParser');

goog.require('goog.dom.xml');
goog.require('goog.debug.Logger');

goog.require('specview.model.Peak');
goog.require('specview.model.TextElement');
goog.require('specview.model.Molecule');
goog.require('specview.model.Atom');
goog.require('specview.model.Bond');
goog.require('specview.model.Spectrum');
goog.require('specview.model.NMRdata');
goog.require('specview.util.Utilities');

specview.io.SpectrumCMLParser=function(){};



/**
 * Logging object.
 */
specview.io.SpectrumCMLParser.logger = goog.debug.Logger.getLogger('specview.io.SpectrumCMLParser');


/**
 * Parses CML with spectrum data such as can be found in NMR Shift DB.
**/

/**
 * Converts CML string to DOM
 * @param cmlString
 * @returns XMLdocument
 */
specview.io.SpectrumCMLParser.getDocument=function(cmlString) {
	return goog.dom.xml.loadXml(cmlString);
};

/**
 * Parses CML XMl document into NMRData
 * @param XMLdocument
 * @returns {specview.model.NMRdata()}
 */
specview.io.SpectrumCMLParser.parseDocument=function(NMRdataObject,XMLdoc){
	var molToBuild=new Array();
			
    //Attributes of an Atom object
	var atomId; var elementType; var x; var y; var charge; var hydrogenCount; 
    //Attributes of a Peak object
	var peakId; var xValue; var multiplicity; var height; var atomRefs; var peakShape; var maxPValue=0; var molRefs; var xUnits; var yUnits;
    //Attributes of a Bond object
	var bondId; var type; var source; var target; 
	var stereo=null;
	
    var ArrayOfAtoms=new Array(); // Keys are the inner atom id. Values are the atom objects
	var ArrayOfBonds=new Array();
	var ArrayOfPeaks=new Array();
	var mol="";
	var experimentType="not available";
//	specview.io.SpectrumCMLParser.logger.info("metadataList: "+XMLdoc.getElementsByTagName("molecule").length)
//	alert(XMLdoc.getElementsByTagName("metadataList")[0].childNodes[0].attributes[0].value);
	try{
		experimentType=XMLdoc.getElementsByTagName("metadataList")[0].childNodes[1].attributes[0].value;
	}catch(err){
		alert("Warning:\n"+err);
	}
//	if(experimentType==null){
//		experimentType=XMLdoc.getElementsByTagName("metadataList")[0].childNodes[0].attributes[0].value;
//	}
//	specview.io.SpectrumCMLParser.logger.info(experimentType)
//var experimentType=XMLdoc.getElementsByTagName("metadataList")[0].childNodes[1].attributes[0].value;
	var nmrData=NMRdataObject;
	var THEMOLECULENAME="not available";var THEMOLECULEID="not available";
	var listOfMolecules=new Array();
	
	
	if(specview.util.Utilities.startsWith("ms",experimentType)){
		nmrData.experienceType="MS";
//		alert(XMLdoc.getElementsByTagName("reactant")[0].childNodes[0].attributes[0].value);
		try{
			THEMOLECULENAME=XMLdoc.getElementsByTagName("reactant")[0].childNodes[0].attributes[0].value;
		}catch(err){}
		THEMOLECULENAME=XMLdoc.getElementsByTagName("reactant")[0].childNodes[1].attributes[0].value;	
	}else if(specview.util.Utilities.startsWith("nmr",experimentType)){
		nmrData.experienceType="NMR";
		try{
			THEMOLECULENAME=XMLdoc.getElementsByTagName('molecule').item(0).attributes[0].value;
			THEMOLECULEID=XMLdoc.getElementsByTagName('molecule').item(0).attributes[1].value;	
		}catch(err){
			alert(err+"\n\nNo molecule name or id available");
		}
	}
//	alert(THEMOLECULENAME)
//	specview.io.SpectrumCMLParser.logger.info("the molecule name: "+THEMOLECULENAME)
	var molecules=XMLdoc.getElementsByTagName("moleculeList");
//	alert(molecules[0].childNodes.length);
	if(nmrData.experienceType=="MS"){
		if(molecules[0]!=undefined){//Stephen files: moleculeList is the tag holding all the information about the molecules
			listOfMolecules=molecules[0].childNodes;	
		}else{//2008 cml files: moleculeList does not exist. INstead, we have productList
//			listOfMolecules=XMLdoc.getElementsByTagName('productList')[0].childNodes;
			listOfMolecules=XMLdoc.getElementsByTagName('cml:molecule');
		}
	}else{
//		alert(XMLdoc.getElementsByTagName("molecule")[0].childNodes)
		listOfMolecules[0]=XMLdoc.getElementsByTagName("molecule");
//		alert(listOfMolecules[1])
	}
//	specview.io.SpectrumCMLParser.logger.info("before the loop: "+listOfMolecules.length);
	nmrData.mainMoleculeName=THEMOLECULENAME;
	for(var molecule=0;molecule<listOfMolecules.length;molecule++){
//		alert(listOfMolecules[molecule]);
		var XmlTextElement=false;
		try{
			XmlTextElement=listOfMolecules[molecule] instanceof Text;
		}catch(err){
			
		}
//		alert("text: "+XmlTextElement)
		if(!XmlTextElement){
			var MS = nmrData.experienceType=='MS';
			var NMR = nmrData.experienceType=='NMR';
			var moleculeName;
			var moleculeNode;
//			alert(listOfMolecules[molecule])
			if(MS){
//			alert(moleculeName)
				moleculeNode=listOfMolecules[molecule];
//				alert(moleculeNode)
//				specview.io.SpectrumCMLParser.logger.info("line 94 spec09: "+moleculeNode.nodeName)
			}else if(NMR){
				moleculeNode=listOfMolecules[molecule][0];
	///			alert(moleculeNode.childNodes.length)
			}
//			alert(listOfMolecules[molecule][0].childNodes[0].nodeName+"\n"+listOfMolecules[molecule][0].childNodes[1].nodeName+"\n"+listOfMolecules[molecule][0].childNodes[2].nodeName+"\n");
//			moleculeNode=listOfMolecules[molecule][0];
//			alert(moleculeNode);
//				alert(listOfMolecules[molecule][0].childNodes.length)
				if(MS){
//					alert(moleculeNode)
					moleculeName=moleculeNode.attributes[0].value;
//					alert(moleculeName)
				}else if(NMR){
					moleculeName=THEMOLECULENAME;
				}
				var moleculeBeingBuilt=new specview.model.Molecule(moleculeName);
				var atomArray=null;
				var bondArray=null;
//				alert(moleculeNode.childNodes.length)
				for(var i=0;i<moleculeNode.childNodes.length;i++){
//					specview.io.SpectrumCMLParser.logger.info(moleculeNode.childNodes[i].nodeName);
					if(moleculeNode.childNodes[i].nodeName=="atomArray"){
						atomArray=moleculeNode.childNodes[i].childNodes;
					}else if(moleculeNode.childNodes[i].nodeName=="bondArray"){
						bondArray=moleculeNode.childNodes[i].childNodes;
					}
				}
				
				
				
				var lenAtom=atomArray.length;var lenBond=bondArray.length;
//				specview.io.SpectrumCMLParser.logger.info(atomArray.item(1).attributes.length)
				//Create the atoms and put it in the Array of Atoms of the cmlObject.
				for(var k=0;k<lenAtom;k++){
					if(atomArray.item(k).attributes!=null){
						for(var attributeIndice=0;attributeIndice<atomArray.item(k).attributes.length;attributeIndice++){
							var attribute=atomArray.item(k).attributes[attributeIndice];
							var attributeName=attribute.name;
							var attributeValue=attribute.value;
							switch(attributeName){
							case "id":
								atomId=attributeValue;
								break;
							case "elementType":
								elementType=attributeValue;
								break;
							case "x2":
								x=parseFloat(attributeValue);
								break;
							case "y2":
								y=parseFloat(attributeValue);
								break;
							case "formalCharge":
								charge=attributeValue;
								break;
							case "hydrogenCount":
								hydrogenCount=attributeValue;
								break;
							case "isotopeNumber":
								isotopeNumber=attributeValue;
								break;
							}
						}
				        if (charge=="0")
				            charge=null; // prevents charge rendering on zero charge
						var currentAtom=new specview.model.Atom(elementType,x,y,charge);//Create the atom
				//		specview.io.SpectrumCMLParser.logger.info(currentAtom)
						currentAtom.setInnerIdentifier(atomId);//Set its inner identifier..a1,a2...
						currentAtom.peakMap[currentAtom.getInnerIdentifier()]=new Array();//Prepare the array for the peaks..[a1=[] ; a2=[] ...]
				        //this.logger.info("atom id "+currentAtom.getInnerIdentifier())
						/**
						 * THAT IS WEIRD
						 */
						ArrayOfAtoms[currentAtom.getInnerIdentifier()]=currentAtom;//Set the ArrayOfAtoms attribute of the graphical object
						nmrData.ArrayOfAtoms[currentAtom.getInnerIdentifier()]=currentAtom;
//						alert(currentAtom)	
					}
				}
//				alert(bondArray[6].nodeName)
				//specview.io.SpectrumCMLParser.logger.info(bondArray.item(1).attributes.length);
				//Create the bonds with the stereo and put it in the Array of bonds of the cmlObject
				for(var k=0;k<lenBond;k++){
				//	stereo=null;
					var bondElement = bondArray.item(k);
					if(bondArray.item(k).attributes!=null){
//						alert(k)
						var tag=bondArray[k].nodeName;
//						alert(specview.model.Bond.ORDER.SINGLE)
							if(k<bondArray.length-2){
								var nextTag=bondArray[k+2].nodeName;// Not very smart to proceed like this. Must be a better way.
//								alert(k+'  '+nextTag)
							}else{var nextTag=null;}
							var isStereo = (tag=="bondStereo") || (tag=="cml:bondStereo");
							if(tag=="bond" || tag=="cml:bond" && !isStereo){// TO AVOID DOING USELESS OPERATIONS ON BONDSTEREO
								for(var attributeIndice=0;attributeIndice<bondArray.item(k).attributes.length;attributeIndice++){
									var attributeName=bondArray[k].attributes[attributeIndice].name;
									var attributeValue=bondArray[k].attributes[attributeIndice].value;
//									alert(attributeName+": "+attributeValue);
									switch(attributeName){
									case "id":
										bondId=attributeValue;
										break;
									case "atomRefs2":
										source=attributeValue.split(" ")[0];
										target=attributeValue.split(" ")[1];
										break;
									case "order":
										type=attributeValue;
										if(type=="S"){type=specview.model.Bond.ORDER.SINGLE;}
										else if(type=="D"){type=specview.model.Bond.ORDER.DOUBLE;}				
										else if(type=="T"){type=specview.model.Bond.ORDER.TRIPLE;}
//										alert('end of order')
										break;
									default :
										break;
									}
//									alert('outside')
								}
							}
							if(bondElement.childNodes[1]!=undefined){
//								alert(bondElement.childNodes[1].attributes[0].value);
							}
//							alert(stereo)
							if(nextTag=="bondStereo"){//DO NOT FORGET TO ADD THE REMAING STEREOSPECIFITIY
//								alert("stereo");
								stereo=bondArray[k+2].attributes[0].value;
							}else if(bondElement.childNodes[1]!=undefined){
								stereo=bondElement.childNodes[1].attributes[0].value;
							}
							switch(stereo){
							case "cml:W":
								stereo=specview.model.Bond.STEREO.UP;
								break;
							case "cml:H":
								stereo=specview.model.Bond.STEREO.DOWN;
								break;
							default:
									stereo=specview.model.Bond.STEREO.NOT_STEREO;
							}
//							specview.io.SpectrumCMLParser.logger.info("end of the bond. Stereo = "+stereo)
//							stereo=(stereo!=specview.model.Bond.STEREO.NOT_STEREO ? stereo : specview.model.Bond.NOT_STEREO);
				            var currentBond=new specview.model.Bond (ArrayOfAtoms[source],ArrayOfAtoms[target],type,stereo );
	//			            specview.io.SpectrumCMLParser.logger.info("before: "+currentBond);
	//			            specview.io.SpectrumCMLParser.logger.info("in the parser: "+ArrayOfBonds[bondId])
				            if(ArrayOfBonds[bondId]==undefined){
				            	ArrayOfBonds[bondId]=currentBond
								nmrData.ArrayOfBonds[bondId]=currentBond;//Set the ArrayOfBonds of the graphical object
//								stereo=specview.model.Bond.STEREO.NOT_STEREO;//Reset the value of stereo!
//								specview.io.SpectrumCMLParser.logger.info("bond: "+currentBond);	
				            }	
					}
				}
				
				/*
				 * Now take care of the relative information of the molecule
				 */	
					
				var scalarInfo = moleculeNode.getElementsByTagName("scalar");
				for(var s = 0;s<scalarInfo.length;s++){
					var lineInfo = scalarInfo.item(s);

					for(var attribut = 0 ; attribut<lineInfo.attributes.length ; attribut++){
							var at = lineInfo.attributes[attribut]
							var attributName = at.name;
							var attributValue = at.value;
							var textValue = lineInfo.childNodes[0].nodeValue;
							
//							specview.io.SpectrumCMLParser.logger.info(attributName+" : "+attributValue+" -->"+scalarInfo.item(s).childNodes[0].nodeValue);
					}
				}
				
				
				if(THEMOLECULENAME.length > 80){
					THEMOLECULENAME = specview.util.Utilities.insertCarriageReturnInString(THEMOLECULENAME,"<br>",80);
				}
				if(nmrData.experienceType=="MS"){
//					alert(moleculeName)
//					specview.io.SpectrumCMLParser.logger.info(moleculeName);
					if(moleculeName==THEMOLECULENAME){
						nmrData.molecule=new specview.model.Molecule(THEMOLECULENAME);
					}
					var secondaryMolecule=new specview.model.Molecule(moleculeName);
					for(k in ArrayOfAtoms){
						if(ArrayOfAtoms[k].coord.x!=0 && ArrayOfAtoms[k].coord.y!=0){
							if(moleculeName==THEMOLECULENAME){
								nmrData.molecule.addAtom(ArrayOfAtoms[k]);
							}
							secondaryMolecule.addAtom(ArrayOfAtoms[k]);
						}
					}
					//Add the bonds to the molecule
//					alert("how many bonds: "+ArrayOfBonds.length);
					for(k in ArrayOfBonds){
						if(moleculeName==THEMOLECULENAME){
							nmrData.molecule.addBond(ArrayOfBonds[k]);
						}
						secondaryMolecule.addBond(ArrayOfBonds[k]);
					//	specview.io.SpectrumCMLParser.logger.info(ArrayOfBonds[k]);
					}
					molecule.fragmentId=moleculeName;
					nmrData.ArrayOfSecondaryMolecules[moleculeName]=secondaryMolecule;
					
//					alert(secondaryMolecule)
					//specview.io.SpectrumCMLParser.logger.info("molecule: "+secondaryMolecule.name)
					ArrayOfAtoms=new Array();
					ArrayOfBonds=new Array();
				}	
		}
	}	
	
	
	/**
	 * Now , take care of the information of the experiment :
	 * Extract information from the tag:
	 *  <conditionList>
	 *  	<scalar info ...>
	 *  </conditionList>
	 *  <metadataList>
	 *  	<metadata ...>
	 *  </metadataList>
	 */
	
	nmrData.metadata = new specview.model.TextElement();
	nmrData.metadata.text["experienceType"] = nmrData.experienceType;
//	alert(nmrData.metadata.text["experienceType"]);
	var conditionExperiment = XMLdoc.getElementsByTagName("conditionList")[0].childNodes;
	var metadataExperiment = XMLdoc.getElementsByTagName("metadata");
//	specview.io.SpectrumCMLParser.logger.info("condtion: "+conditionExperiment.length+"  metadata: "+metadataExperiment.length);
	for(var metadata = 0 ; metadata < metadataExperiment.length ; metadata++){
		var lineInfo = metadataExperiment[metadata];
		var name = lineInfo.attributes[0].value;
		var content = lineInfo.attributes[1].value;
		nmrData.metadata.text[specview.util.Utilities.getStringAfterCharacter(name,":")]=content;
//		specview.io.SpectrumCMLParser.logger.info(name + " --> "+ content);
	}
	
	for(condition = 0 ; condition < conditionExperiment.length ; condition++){
		var lineInfo = conditionExperiment[condition];
		if(lineInfo.attributes!=null){
//			specview.io.SpectrumCMLParser.logger.info(lineInfo.attributes[0].name+" ; "+lineInfo.attributes[1].name+" ; "+lineInfo.attributes[2].name);
			var name = lineInfo.attributes["dictRef"].value;
			var content = (lineInfo.attributes["units"]!=undefined) ? lineInfo.attributes["units"].value : "unavailable";
			var textField = lineInfo.childNodes[0].nodeValue;
			nmrData.metadata.text[specview.util.Utilities.getStringAfterCharacter(name,":")]=new Array(content,textField);
//			specview.io.SpectrumCMLParser.logger.info(name + " --> "+ content+"--->"+textField);
		}
	}
	

	
	
	if(nmrData.experienceType=="NMR"){
		nmrData.molecule=new specview.model.Molecule(THEMOLECULENAME);
		nmrData.molecule.molecule_id=THEMOLECULEID;
		for(k in ArrayOfAtoms){
			if(ArrayOfAtoms[k].coord.x!=0 && ArrayOfAtoms[k].coord.y!=0){
				nmrData.molecule.addAtom(ArrayOfAtoms[k]);	
			}
		}
		for(z in ArrayOfBonds){
			nmrData.molecule.addBond(ArrayOfBonds[z]);
		}
	}
	
	var peaks=XMLdoc.getElementsByTagName("peakList")[0].childNodes;//Peaks are the same for MS and NMR
	var lenPeak=peaks.length;
//	alert("peakk: "+lenPeak)
	
	for(var k=0;k<lenPeak;k++){
//		if(peaks[k] instanceof Element){
			var peak=peaks[k];
//			alert(peak)
			if(peak!=null && !(peak instanceof Text)){
				var moleculeRef=null;
				
				var isThereAmoleculeAssignedToThePeak=(peak.childNodes[1]==undefined) ? false : true;
				if(isThereAmoleculeAssignedToThePeak){
					moleculeRef=peak.childNodes[1].attributes[0].value;			
				}
				for(var attributeIndice=0;attributeIndice<peak.attributes.length;attributeIndice++){
					var attributeName=peak.attributes[attributeIndice].name;
					var attributeValue=peak.attributes[attributeIndice].value;
//					alert(attributeName+' : '+attributeValue)
//					specview.io.SpectrumCMLParser.logger.info(attributeName);
					switch(attributeName){
					case "xValue":
						xValue=parseFloat(attributeValue);
						maxPValue=(xValue>maxPValue ? xValue : maxPValue);
						break;
					case "xUnits":
						xUnits=specview.util.Utilities.getStringAfterCharacter(attributeValue,":");
						break;
					case "yUnits":
						yUnits=specview.util.Utilities.getStringAfterCharacter(attributeValue,":");
						break;
					case "peakShape":
						peakShape=attributeValue;
						break;
					case "peakMultiplicity":
						multiplicity=attributeValue;
						break;
					case "id":
						peakId=attributeValue;
						break;
					case "atomRefs":
						atomRefs=attributeValue.split(" ");
						break;
					case "moleculeRefs" :
						moleculeRef=attributeValue;
						break;
					case "yValue":
						height=attributeValue;
						break;
					default:
						break;
					}
				}
				
				if(nmrData.experienceType=="NMR"){
					for(at in atomRefs){
						var atomRef=atomRefs[at];
//						this.logger.info(atomRef);
						for(s in ArrayOfAtoms){
							var atom=ArrayOfAtoms[s];
							var innerIdentifier=atom.innerIdentifier;
							if(atomRef==innerIdentifier){
								atom.peakMap[atomRef].push(peakId);
							}
						}
					}
				}
				
				height = height ? height : 50 ; // Should be more precise on the height	
				var peakToBuild=new specview.model.Peak(xValue,height,peakId,atomRefs,multiplicity,moleculeRef,xUnits,yUnits);
			//	alert(peakToBuild)
//				specview.io.SpectrumCMLParser.logger.info("SpectrumCMLParser09.js: "+peakToBuild);
				molRefs=null;atomRefs=null;
				nmrData.ArrayOfPeaks[peakId]=peakToBuild;
				goog.array.insert(ArrayOfPeaks,peakToBuild);
				
			}
	}
//	this.logger.info("there is exactly "+specview.util.Utilities.getAssoArrayLength(nmrData.ArrayOfPeaks)+" peaks in ArrayOfPeaks");
//	for(k in nmrData.ArrayOfPeaks){
//		this.logger.info(k+": "+nmrData.ArrayOfPeaks[k]);
//	}
	
        //Create a spectrum
       // this.logger.info("ArrayOfPeaks "+ArrayOfPeaks.length);
        var spec= new specview.model.Spectrum(nmrData.molecule, ArrayOfPeaks);
        nmrData.spectrum=spec;
        nmrData.spectrum.xUnit=xUnits;
        nmrData.spectrum.yUnit=yUnits;
	return nmrData;
	
};




