package ru.ravnasybullin.DoiReg.dto;

import org.json.JSONException;
import org.json.JSONObject;
public class UserData {
    String login;

    String accessToken;

    public JSONObject toJSON() throws JSONException {
        return new JSONObject()
                .put("login", login)
                .put("accessToken", accessToken);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
