package com.jaijobner.transport_new.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class FlexibleDoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonToken token = parser.currentToken();
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token == JsonToken.VALUE_NUMBER_FLOAT || token == JsonToken.VALUE_NUMBER_INT) {
            return parser.getDoubleValue();
        }
        if (token == JsonToken.VALUE_STRING) {
            String value = parser.getText().trim();
            if (value.isEmpty()) {
                return null;
            }
            return Double.parseDouble(value);
        }
        return null;
    }
}
