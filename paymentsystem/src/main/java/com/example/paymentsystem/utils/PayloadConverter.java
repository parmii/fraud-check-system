package com.example.paymentsystem.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;

import java.util.Map;

@Log4j2
public class PayloadConverter {

    public static String getXML(Map<String, Object> jsonInputMap) {
        JSONObject jsonInput = new JSONObject(jsonInputMap);

        System.out.println(jsonInput);
        // Convert JSON back to XML
        String regeneratedXML = XML.toString(jsonInput);
        System.out.println("Converted XML:\n" + regeneratedXML);
        log.info("JSON payload: {}, converted XML: {}", jsonInputMap, regeneratedXML);
        return regeneratedXML;
    }

    public static String getJSON(String xmlInput) {
        //   JSONObject jsonInput = new JSONObject(jsonInputMap);
        JSONObject jsonObject = XML.toJSONObject(xmlInput, true);

        // beautify the JSON output
        String jsonPrettyPrintString = jsonObject.toString();
        System.out.println(jsonPrettyPrintString);
        log.info("XML payload: {}, converted JSON: {}", xmlInput, jsonObject.toString());
        return jsonObject.toString();
    }

    // Generic Method to Convert JSON String to XML String
    public static <T> String jsonToXml(String jsonString, Class<T> valueType) throws Exception {
        // Convert JSON to Java Object
        ObjectMapper objectMapper = new ObjectMapper();
        T object = objectMapper.readValue(jsonString, valueType);

        // Convert Java Object to XML
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(object);
    }

    // Generic Method to Convert XML String to JSON String
    public static <T> String xmlToJson(String xmlString, Class<T> valueType) throws Exception {
        // Convert XML to Java Object
        XmlMapper xmlMapper = new XmlMapper();
        T object = xmlMapper.readValue(xmlString, valueType);

        // Convert Java Object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    // Generic method to convert JSON string to any object
    public static <T> T jsonToObject(String jsonString, Class<T> valueType) throws Exception {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the JSON string to the specified object type
        return objectMapper.readValue(jsonString, valueType);
    }

    // Generic method to convert any object to a JSON String
    public static <T> String objectToJson(T object) throws Exception {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the object to JSON and return it as a String
        return objectMapper.writeValueAsString(object);
    }

    // Generic method to convert any object to XML
    public static <T> String toXml(T object) throws Exception {
        // Create an XmlMapper instance
        XmlMapper xmlMapper = new XmlMapper();

        // Convert the object to XML and return as a String
        return xmlMapper.writeValueAsString(object);
    }

    // Generic method to convert XML to any Java object
    public static <T> T toObject(String xml, Class<T> clazz) throws Exception {
        // Create an instance of XmlMapper
        XmlMapper xmlMapper = new XmlMapper();

        // Convert XML string to the given object type
        return xmlMapper.readValue(xml, clazz);
    }
}
