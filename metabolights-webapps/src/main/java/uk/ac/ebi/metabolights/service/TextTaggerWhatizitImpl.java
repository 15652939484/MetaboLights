/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 5/19/14 2:34 PM
 * Modified by:   conesa
 *
 *
 * ©, EMBL, European Bioinformatics Institute, 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package uk.ac.ebi.metabolights.service;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class TextTaggerWhatizitImpl implements TextTaggerService {


	private static Logger logger = LoggerFactory.getLogger(TextTaggerService.class);
	private final String xsltFileName = "uncompilables/isatabuploaderconfig/chemicals.xslt";
	//private final String xsltFileName = "ebimed.xslt";

	//See various pipelines listed at http://www.ebi.ac.uk/webservices/whatizit/info.jsf
	//private final String pipeLine = "whatizitEBIMedDiseaseChemicals"; 
	private final String pipeLine = "whatizitChebiDict"; 
	//private final String pipeLine = "whatizitUkPmcChemicals";
	
	/**
	 * Takes in a text String to be marked up by the WhatIzit pipeline.
	 * <br> 
	 * See further info on: http://www.ebi.ac.uk/Rebholz-srv/whatizit/pipe
	 * XSLT files can be found in EBI directory /ebi/textmining/Web/xslt/
	 * <br>
	 * Class calls a servlet, alternatively you could call the web service. See
	 * http://www.ebi.ac.uk/webservices/whatizit/info.jsf
	 *
	 * @param text : input text (abstract or title)
	 * @param pipeLine : type of analysis to be done by WhatIzIt
	 * @return text with identified keywords/hyperlinks added by WhatIzIt
	 */
	@Override
	public String tagText(String text) {

		String output = text;

		if (TextUtils.isNullOrEmpty(text))
			return output;

		try {
			InputStream ins = this.getClass().getClassLoader().getResourceAsStream(xsltFileName);

			String pipeInputText = 
				pipeLine + "\n" + WhatizitHttpClient.XML_START + "<text>" + StringEscapeUtils.escapeXml(text) + "</text>" + WhatizitHttpClient.XML_END;

			Source streamSource = new StreamSource(ins);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(streamSource);

			logger.info("making Whatizit call");

			WhatizitHttpClient client = new WhatizitHttpClient();
			client.upload(pipeInputText);

			String originalPipeOutputText = client.download();

			ByteArrayInputStream inputStream = new ByteArrayInputStream(originalPipeOutputText.getBytes("UTF-8"));
			ByteArrayOutputStream transformedOutputStream = new ByteArrayOutputStream();
            try { //This may throw and error, ignore
                transformer.transform(new StreamSource(inputStream), new StreamResult(transformedOutputStream));
            }   catch (Exception e) {}

			output = new String(transformedOutputStream.toByteArray(), "UTF-8").replaceAll(WhatizitHttpClient.XML_START, "").replaceAll(WhatizitHttpClient.XML_END, "");
			client.close();

		} catch (Exception e) {
			output = text;
			e.printStackTrace();
		}
		return output;
	}

}
