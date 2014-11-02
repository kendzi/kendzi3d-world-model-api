package kendzi.kendzi3d.service.viewer.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        // SimpleModule module = new SimpleModule("ObjectIdmodule");
        // module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        this.registerModule(new CustomSimpleModule());
    }
}