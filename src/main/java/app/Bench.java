package app;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import domain.Message;
import domain.Provider;
import domain.User;
import mongo.ReferenceModule;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

import java.net.UnknownHostException;
import java.time.Duration;

public class Bench {
    public static void main(String[] args) throws UnknownHostException {
        final DB db = new MongoClient().getDB("reference");
        final Jongo jongo = new Jongo(db, new JacksonMapper.Builder()
                .registerModule(new ReferenceModule())
                .build()
        );

        final long start = System.nanoTime();
        runBench(jongo);
        final long end = System.nanoTime();

        System.out.println("elapsed=" + Duration.ofNanos(end - start).getSeconds());
    }

    private static void runBench(Jongo jongo) {
        final MongoCollection users = jongo.getCollection("users");
        final MongoCollection messages = jongo.getCollection("messages");
        final MongoCollection providers = jongo.getCollection("providers");

        // cleanup
        users.remove();
        messages.remove();
        providers.remove();

        for (int i = 0; i < 10000; i++) {
            new Thread(() -> createMessage(users, messages, providers)).run();
        }
    }

    private static void createMessage(MongoCollection users, MongoCollection messages, MongoCollection providers) {
        // add two users
        final User from = new User("John", "Doe");
        users.save(from);
        final User to = new User("Jane", "Doe");
        users.save(to);

        // add a provider
        final Provider provider = new Provider("GMail");
        providers.save(provider);

        // add a message
        final Message message = new Message("Hello World", from, to, provider);
        messages.save(message);
    }
}
