package com.risksense.converters;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XMLJSONConverterController {

	private static final Logger logger = LoggerFactory.getLogger(XMLJSONConverterController.class);

	@RequestMapping(value = "/convertJsonXMLFile.htm")
	public String convertJsonXMLFile(@RequestParam String jsonFilePath) {
		try {
			Resource resource = new ClassPathResource(jsonFilePath);

			File json = resource.getFile();

			File xml = new File("ResultOutput.xml");

			ConverterFactory.createXMLJSONConverter().convertJSONtoXML(json, xml);

		} catch (Exception e) {
			logger.error("Error while reading the file :: ", e);
		}

		return "Generated xml file at ResultOutput.xml";
	}

}
