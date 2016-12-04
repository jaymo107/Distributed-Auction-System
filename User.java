import java.io.Serializable;
/**
 * Base class for storing user information to pass to the
 * RMI server.
 */
public class User implements Serializable{
    private int id;

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
