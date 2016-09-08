package domain;

import mongo.Reference;

public class Message extends BaseOidObject {
    private String content;
    @Reference
    private User from;
    @Reference
    private User to;
    @Reference
    private Provider provider;

    public Message() {
    }

    public Message(String content, User from, User to, Provider provider) {
        this.content = content;
        this.from = from;
        this.to = to;
        this.provider = provider;
    }

    public String getContent() {
        return content;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public Provider getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", provider=" + provider +
                '}';
    }
}
