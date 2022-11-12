package utils;

public class ExceptionWonGame extends Exception {
    String msg;
    public ExceptionWonGame(int playerId) {
        if (playerId == 1)
            msg = "Player one killed the enemy hero.";
        else
            msg = "Player two killed the enemy hero.";
    }

    public String toString () {
        return msg;
    }
}
