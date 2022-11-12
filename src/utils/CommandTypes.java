package utils;

import java.util.HashMap;



public class CommandTypes {
    private static final HashMap<String, CommandType> cmdTypes = new HashMap<>();

    static {
        cmdTypes.put("endPlayerTurn", CommandType.TURNOVER);
        // Action commands
        cmdTypes.put("placeCard", CommandType.ACTION);
        cmdTypes.put("cardUsesAttack", CommandType.ACTION);
        cmdTypes.put("cardUsesAbility", CommandType.ACTION);
        cmdTypes.put("useAttackHero", CommandType.ACTION);
        cmdTypes.put("useHeroAbility", CommandType.ACTION);
        cmdTypes.put("useEnvironmentCard", CommandType.ACTION);
        // Output commands
        cmdTypes.put("getCardsInHand", CommandType.OUTPUT);
        cmdTypes.put("getPlayerDeck", CommandType.OUTPUT);
        cmdTypes.put("getCardsOnTable", CommandType.OUTPUT);
        cmdTypes.put("getPlayerTurn", CommandType.OUTPUT);
        cmdTypes.put("getPlayerHero", CommandType.OUTPUT);
        cmdTypes.put("getCardAtPosition", CommandType.OUTPUT);
        cmdTypes.put("getPlayerMana", CommandType.OUTPUT);
        cmdTypes.put("getEnvironmentCardsInHand", CommandType.OUTPUT);
        cmdTypes.put("getFrozenCardsOnTable", CommandType.OUTPUT);
        cmdTypes.put("getTotalGamesPlayed", CommandType.OUTPUT);
        cmdTypes.put("getPlayerOneWins", CommandType.OUTPUT);
    }

    public static CommandType getType(String command) {
        return cmdTypes.get(command);
    }
}
