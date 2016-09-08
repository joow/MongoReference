package mongo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;

import java.io.IOException;

class ReferableDeserializer<T extends Referable> extends JsonDeserializer<T> implements ResolvableDeserializer, ContextualDeserializer {
    private final JsonDeserializer<T> defaultDeserializer;
    private final Class<T> clazz;
    private final boolean useReference;

    public ReferableDeserializer(JsonDeserializer<T> defaultDeserializer, Class<T> clazz) {
        this(defaultDeserializer, clazz, false);
    }

    public ReferableDeserializer(JsonDeserializer<T> defaultDeserializer, Class<T> clazz, boolean useReference) {
        this.defaultDeserializer = defaultDeserializer;
        this.clazz = clazz;
        this.useReference = useReference;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (useReference) {
            T value = newInstance();
            value.setId(p.getValueAsString());
            return value;
        } else {
            return defaultDeserializer.deserialize(p, ctxt);
        }
    }

    private T newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonDeserializer<T> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property == null || property.getAnnotation(Reference.class) == null) {
            return this;
        } else {
            return new ReferableDeserializer<>(defaultDeserializer, clazz, true);
        }
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }
}
