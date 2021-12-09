package dev.source.model;

import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private String username, token;

    public User() {
    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public static User getUserFromJSON(JSONObject jsonObject) throws Exception {
        String username = jsonObject.getString("username");
        String token = jsonObject.getString("token");
        return new User(username, token);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
