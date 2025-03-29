package com.example.paymentsystem.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;

@RequiredArgsConstructor
public class RestServiceHandler implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceHandler.class);

    private final String serviceName;

    @Override
    public void filter(ClientRequestContext requestContext) {
        logRequest(requestContext.getStringHeaders(), requestContext.getEntity());
    }

    public void logRequest(MultivaluedMap<String, String> stringHeaders, Object entity) {
        final StringBuilder msg = new StringBuilder();
        msg.append("REQUEST TO ").append(serviceName);
        msg.append("\n HEADERS = ")
                .append(stringHeaders);
        try {
            String serialized = serializeEntity(entity);
            msg.append("\n REQUEST = ")
                    .append(serialized);
        } catch (Exception e) {
            LOGGER.info("Exception in Rest Handler #1: " + e);
        }
        LOGGER.info(msg.toString());
    }

    private String serializeEntity(Object entity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(entity);
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        final StringBuilder msg = new StringBuilder();
        msg.append("RESPONSE FROM ").append(serviceName);
        try {
            msg.append("\n HEADERS = ")
                    .append(requestContext.getStringHeaders());
            msg.append("\n STATUS = ")
                    .append(responseContext.getStatus());
            if (responseContext.getStatusInfo() != null) {
                msg.append("\n REASON = ")
                        .append(responseContext.getStatusInfo().getReasonPhrase());
            }
            BufferedInputStream stream = new BufferedInputStream(responseContext.getEntityStream());
            String payload = IOUtils.toString(stream, "UTF-8");
            responseContext.setEntityStream(IOUtils.toInputStream(payload, "UTF-8"));
            msg.append("\n RESPONSE = ")
                    .append(payload);
        } catch (Exception e) {
            LOGGER.info("Exception in Rest Handler #1: " + e);
        }
        LOGGER.info(msg.toString());
    }
}
