package kendzi.kendzi3d.service.viewer.json;

import java.awt.Color;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

@Component
public class CustomSimpleModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    @Override
    public void setupModule(SetupContext context) {
        SimpleSerializers serializers = new SimpleSerializers();

        serializers.addSerializer(Color.class, new ColorSerializer());

        context.addSerializers(serializers);
    }

}
