import java.io.Serializable;
/**
 * Base class for storing user information to pass to the
 * RMI server.
 */
public class User implements Serializable{
    private String email;
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
