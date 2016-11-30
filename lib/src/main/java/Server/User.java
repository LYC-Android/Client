package Server;

import java.io.Serializable;

/**
 * Created by mr.cheng on 2016/10/23.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private float[] mdatas;

    public float[] getMdatas() {
        return mdatas;
    }

    public void setMdatas(float[] mdatas) {
        this.mdatas = mdatas;
    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
