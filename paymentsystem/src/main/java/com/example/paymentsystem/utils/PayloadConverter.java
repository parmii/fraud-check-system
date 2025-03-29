package com.example.paymentsystem.utils;

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
}
