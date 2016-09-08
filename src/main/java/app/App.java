package app;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import domain.*;
import mongo.ReferenceModule;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

import java.net.UnknownHostException;

import static org.jongo.Oid.withOid;

public class App {
    public static void main(String[] args) throws UnknownHostException {
        final DB db = new MongoClient().getDB("reference");
        final Jongo jongo = new Jongo(db, new JacksonMapper.Builder()
                .registerModule(new ReferenceModule())
                .build()
        );

        final MongoCollection users = jongo.getCollection("users");
        final MongoCollection messages = jongo.getCollection("messages");
        final MongoCollection providers = jongo.getCollection("providers");

        // cleanup
        users.remove();
        messages.remove();
        providers.remove();

        // add two users
        final User from = new User("John", "Doe");
        users.save(from);
        final User to = new User("Jane", "Doe");
        users.save(to);

        // add a provider
        final Provider provider = new Provider("GMail");
        providers.save(provider);

        // add a message between the two users
        final Message message = new Message("Hello World", from, to, provider);
        messages.save(message);

        final Message reloadedMessage = messages.findOne("{from: #}", from.getId()).as(Message.class);
        System.out.println("reloadedMessage=" + reloadedMessage);

        final User reloadedFrom = users.findOne(withOid(from.getId())).as(User.class);
        System.out.println("reloadedFrom=" + reloadedFrom);

        final Provider reloadedProvider = providers.findOne(withOid(provider.getId())).as(Provider.class);
        System.out.println("reloadedProvider=" + reloadedProvider);
    }
}
