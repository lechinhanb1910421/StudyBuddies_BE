
package com.everett.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimestampSerializer extends StdSerializer<Timestamp> {

    public TimestampSerializer() {
        super(Timestamp.class);
    }

    @Override
    public void serialize(
            Timestamp value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        Instant timestamp = value.toInstant();
        ZonedDateTime zonedDate = ZonedDateTime.ofInstant(timestamp, ZoneId.of("Etc/UTC"));
        generator.writeString(zonedDate.toOffsetDateTime().toString());
    }
}
