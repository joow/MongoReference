package domain;

import mongo.Referable;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.ObjectId;

public abstract class BaseObject<T extends BaseObject> implements Referable {
    private String id;

    public BaseObject() {
    }

    @MongoId
    public String getId() {
        return id;
    }

    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @Override
    public String toString() {
        return "BaseObject{" +
                "id='" + id + '\'' +
                '}';
    }
}
