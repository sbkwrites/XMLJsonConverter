package com.risksense.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@Service
public class XMLJSONConverter implements XMLJSONConverterI {

	private static final Logger logger = LoggerFactory.getLogger(XMLJSONConverter.class);

	@Override
	public void convertJSONtoXML(File json, File xml) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(json);

		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<object>");

		Iterator<Entry<String, JsonNode>> jsonMap = jsonNode.fields();
		
		while (jsonMap.hasNext())
		{
			Map.Entry<String, JsonNode> entry = jsonMap.next();
			convertTXMLData(xmlBuilder, entry.getKey(), entry.getValue());
		}
		xmlBuilder.append("</object>");

		String xmlData = xmlBuilder.toString();
		
		System.out.println("XML :: "+xmlBuilder.toString());

		FileWriter ofstream = new FileWriter(xml);
		try (BufferedWriter out = new BufferedWriter(ofstream))
		{
			out.write(xmlData);
		}
		
		catch (Exception e) 
		{
			logger.error("Error in writing data:", e);
		}

	}

	private StringBuilder convertTXMLData(StringBuilder builder, String key, JsonNode value) throws IOException {

		try {
			if (value.getNodeType() == JsonNodeType.OBJECT) {
				if (key != null)
					builder.append("<object name=\"" + key + "\">");

				else
					builder.append("<object>");

				Iterator<Entry<String, JsonNode>> jsonMap = value.fields();
				while (jsonMap.hasNext()) {
					Map.Entry<String, JsonNode> entry = jsonMap.next();
					convertTXMLData(builder, entry.getKey(), entry.getValue());
				}
				builder.append("</object>");
			}

			if (value.getNodeType() == JsonNodeType.ARRAY) {
				if (key != null)
					builder.append("<array name=\"" + key + "\">");

				else
					builder.append("<array>");

				for (JsonNode node : value) {
					convertTXMLData(builder, null, node);
				}

				builder.append("</array>");
			}
			if (value.getNodeType() == JsonNodeType.STRING) {
				if (key != null)
					builder.append("<string name=\"" + key + "\">");
				else
					builder.append("<string>");
				builder.append(value.textValue());
				builder.append("</string>");
			}

			if (value.getNodeType() == JsonNodeType.NUMBER) {
				switch (value.numberType()) {

				case INT:
					if (key != null)
						builder.append("<number name=\"" + key + "\">");
					else
						builder.append("<number>");
					builder.append(value.intValue());
					builder.append("</number>");
					break;
				case LONG:
					if (key != null)
						builder.append("<number name=\"" + key + "\">");
					else
						builder.append("<number>");
					builder.append(value.longValue());
					builder.append("</number>");
					break;
				case BIG_INTEGER:
					if (key != null)
						builder.append("<number name=\"" + key + "\">");
					else
						builder.append("<number>");
					builder.append(value.bigIntegerValue());
					builder.append("</number>");
					break;
				case DOUBLE:
					if (key != null)
						builder.append("<number name=\"" + key + "\">");
					else
						builder.append("<number>");
					builder.append(value.doubleValue());
					builder.append("</number>");
					break;
				case FLOAT:
					if (key != null)
						builder.append("<number name=\"" + key + "\">");
					else
						builder.append("<number>");
					builder.append(value.floatValue());
					builder.append("</number>");
					break;
				case BIG_DECIMAL:
					if (key != null)
						builder.append("<number name=\"" + key + "\">");
					else
						builder.append("<number>");
					builder.append(value.decimalValue());
					builder.append("</number>");
					break;
				}
			}
			if (value.getNodeType() == JsonNodeType.NULL) {
				builder.append("<null name=\"" + key + "\" ");
				builder.append("/>");
			}

			if (value.getNodeType() == JsonNodeType.BOOLEAN) {
				if (key != null)
					builder.append("<boolean name=\"" + key + "\">");
				else
					builder.append("<boolean>");
				builder.append(value.booleanValue());
				builder.append("</boolean>");
			}

		} catch (Exception e) {
			logger.error("Error while parsing:", e);
		}
		return builder;
	}

}
