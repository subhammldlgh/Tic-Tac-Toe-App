package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OnlineGameInfo {
    private String username;
    private String uid;


    public OnlineGameInfo() {
    }

    public OnlineGameInfo(String username, String uid) {
        this.username = username;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}