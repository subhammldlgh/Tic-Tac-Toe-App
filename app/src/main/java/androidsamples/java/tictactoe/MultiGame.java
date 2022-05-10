package androidsamples.java.tictactoe;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MultiGame {
    private int playerChance;
    private String player1;
    private String player2;
    private String gameState;
    private String gameRes;
    public MultiGame() {
        this.playerChance=1;
        this.player1 = "";
        this.player2 = "";
        this.gameState = "000000000";
        this.gameRes="0";
    }


    public String getGameRes() {
        return gameRes;
    }

    public void setGameRes(String gameRes) {
        this.gameRes = gameRes;
    }

    public MultiGame(String player1) {
        this.playerChance=1;
        this.player1 = player1;
        this.player2 = "";
        this.gameState = "000000000";
        this.gameRes="0";
    }

    public int getPlayerChance() {
        return playerChance;
    }

    public void setPlayerChance(int playerChance) {
        this.playerChance = playerChance;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

}