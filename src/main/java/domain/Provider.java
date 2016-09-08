package domain;

public class Provider extends BaseOidObject {
    private String name;

    public Provider() {
    }

    public Provider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
