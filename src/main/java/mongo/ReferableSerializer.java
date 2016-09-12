package mongo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;

import java.io.IOException;

class ReferableSerializer<T extends Referable> extends JsonSerializer<T> implements ResolvableSerializer, ContextualSerializer {
    private final JsonSerializer<T> defaultSerializer;
    private final boolean useReference;

    public ReferableSerializer(JsonSerializer<T> defaultSerializer) {
        this(defaultSerializer, false);
    }

    public ReferableSerializer(JsonSerializer<T> defaultSerializer, boolean useReference) {
        this.defaultSerializer = defaultSerializer;
        this.useReference = useReference;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (useReference) {
            gen.writeString(value.getId());
        } else {
            defaultSerializer.serialize(value, gen, serializers);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property == null || property.getAnnotation(Reference.class) == null) {
            return this;
        } else {
            return new ReferableSerializer<>(defaultSerializer, true);
        }
    }

    @Override
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        ((ResolvableSerializer) defaultSerializer).resolve(provider);
    }
}
