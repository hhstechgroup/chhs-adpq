package com.engagepoint.cws.apqd.domain.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Custom Jackson deserializer for transforming a JSON object (using the ISO 8601 date format with optional time)
 * to a JSR310 LocalDate object.
 */
public class JSR310LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    public static final JSR310LocalDateDeserializer INSTANCE = new JSR310LocalDateDeserializer();

    private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;

    static {
        ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_OFFSET_TIME)
            .toFormatter();
    }

    private JSR310LocalDateDeserializer() {}

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        if (JsonToken.START_ARRAY == currentToken) {
            int[] dateArray = new int[3];
            boolean isFilled = fillIntArray(parser, context, dateArray);
            return isFilled ? LocalDate.of(dateArray[0], dateArray[1], dateArray[2]) : null;

        } else if (JsonToken.VALUE_STRING == currentToken) {
            String string = parser.getText().trim();
            return string.length() == 0 ? null : LocalDate.parse(string, ISO_DATE_OPTIONAL_TIME);

        } else {
            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
        }
    }

    private boolean fillIntArray(JsonParser parser, DeserializationContext context, int[] targetArray) throws IOException {
        if (parser.nextToken() == JsonToken.END_ARRAY || targetArray.length == 0) {
            return false;
        } else {
            targetArray[0] = parser.getIntValue();
        }

        for (int i = 1; i < targetArray.length; i++) {
            if (parser.nextToken() != JsonToken.VALUE_NUMBER_INT) {
                throw context.wrongTokenException(parser, JsonToken.VALUE_NUMBER_INT, "Expected int value.");
            } else {
                targetArray[i] = parser.getIntValue();
            }
        }

        if (parser.nextToken() != JsonToken.END_ARRAY) {
            throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
        }

        return true;
    }
}
