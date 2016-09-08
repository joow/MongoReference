package mongo;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

public class ReferenceModule extends SimpleModule {
    public ReferenceModule() {
        super("ReferenceModule");

        setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                if (Referable.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new ReferableSerializer(serializer);
                }

                return super.modifySerializer(config, beanDesc, serializer);
            }
        });

        setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (Referable.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new ReferableDeserializer(deserializer, beanDesc.getBeanClass());
                }
                return super.modifyDeserializer(config, beanDesc, deserializer);
            }
        });
    }
}
