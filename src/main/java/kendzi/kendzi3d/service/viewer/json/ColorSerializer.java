package kendzi.kendzi3d.service.viewer.json;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ColorSerializer extends JsonSerializer<Color> {

    @Override
    public void serialize(Color color, com.fasterxml.jackson.core.JsonGenerator jgen,
            com.fasterxml.jackson.databind.SerializerProvider provider) throws IOException, JsonProcessingException {
        if (color != null) {
            jgen.writeStartObject();
            jgen.writeStringField("r", "" + color.getRed() / 256d);
            jgen.writeStringField("g", "" + color.getGreen() / 256d);
            jgen.writeStringField("b", "" + color.getBlue() / 256d);
            jgen.writeEndObject();
        }
    }
}