package domain;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class BaseOidObject<T extends BaseOidObject> extends BaseObject<T> {
    @Override
    @MongoObjectId
    public String getId() {
        return super.getId();
    }
}
