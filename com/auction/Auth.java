package com.auction;

import java.io.Serializable;
import java.util.Random;

/**
 * An auth object to be sent to the server to allow authentication
 * between the client and server.
 */
public class Auth implements Serializable {

    private String random;
    private int userId;
    private String originIp;

    public Auth(int userId) {
        this.userId = userId;
        // Generate the random int to be used as the test.
        this.random = String.valueOf(new Random().nextInt());
        this.originIp = null;
    }

    /**
     * Return the value set by the object.
     *
     * @return String
     */
    public String getValue() {
        return this.random;
    }

    /**
     * Set the random challenge value.
     *
     * @param value The value to set to.
     * @return String
     */
    public String setValue(String value) {
        this.random = value;
        return this.random;
    }

    /**
     * Return the user trying to be authenticated.
     *
     * @return int
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Append some extra data to the payload of the message.
     *
     * @param value
     * @return String
     */
    public String appendValue(String value) {
        return this.random.concat(value);
    }

    /**
     * Set the IP address of the origin.
     */
    public String setOriginIp(String ip) {
        this.originIp = ip;
        return this.originIp;
    }

    /**
     * Return the IP Address
     *
     * @return
     */
    public String getOriginIp() {
        return this.originIp;
    }
}
