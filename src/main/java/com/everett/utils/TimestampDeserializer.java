package com.everett.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class TimestampDeserializer extends StdDeserializer<Timestamp> {

    protected TimestampDeserializer() {
        super(Timestamp.class);
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public Timestamp deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ZonedDateTime zonedDate = ZonedDateTime.parse(parser.readValueAs(String.class), DATE_TIME_FORMATTER);
        return Timestamp.valueOf(zonedDate.withZoneSameInstant(ZoneId.of("Etc/UCT")).toLocalDateTime());
    }
}
