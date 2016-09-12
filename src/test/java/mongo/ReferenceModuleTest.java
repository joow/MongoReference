package mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Message;
import domain.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceModuleTest {
    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ReferenceModule());
    }

    @Test
    public void itShouldFullySerializeReferable() throws JsonProcessingException {
        final User user = new User("John", "Doe");

        final String serialized = objectMapper.writeValueAsString(user);

        assertThat(serialized).contains("\"firstName\":\"John\"");
    }

    @Test
    public void itShouldOnlySerializeIdWhenReference() throws JsonProcessingException {
        final User from = new User("John", "Doe").setId("fromId");
        final User to = new User("Jane", "Doe").setId("toId");
        final Message message = new Message("Hello World", from, to, null);

        final String serialized = objectMapper.writeValueAsString(message);

        assertThat(serialized).contains("\"from\":\"fromId\",\"to\":\"toId\"");
    }

    @Test
    public void itShouldNotInstanciateReferenceWhenDeserializingNullReference() throws IOException {
        final String serialized = "{\"id\":\"messageId\",\"content\":\"Hello World\",\"from\":\"fromId\",\"to\":null,\"provider\":null}";

        final Message message = objectMapper.readValue(serialized, Message.class);

        assertThat(message.getTo()).isNull();
    }
}
