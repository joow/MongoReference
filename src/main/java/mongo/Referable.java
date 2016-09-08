package mongo;

/**
 * A referable object might be referenced from another object.
 * @param <T> The type of referable object.
 */
public interface Referable<T extends Referable> {
    String getId();
    T setId(String id);
}
