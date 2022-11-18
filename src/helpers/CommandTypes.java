package helpers;

import java.util.HashMap;

final public class CommandTypes {
    private CommandTypes() {
    }

    private static final HashMap<String, CommandType> CMD_TYPES = new HashMap<>() {
        @Override
        public CommandType get(final Object key) {
            if (!containsKey(key)) {
                return CommandType.INVALID;
            }
            return super.get(key);
        }
    };

    static {
        CMD_TYPES.put("endPlayerTurn", CommandType.TURNOVER);
        // Action commands
        CMD_TYPES.put("placeCard", CommandType.ACTION);
        CMD_TYPES.put("cardUsesAttack", CommandType.ACTION);
        CMD_TYPES.put("cardUsesAbility", CommandType.ACTION);
        CMD_TYPES.put("useAttackHero", CommandType.ACTION);
        CMD_TYPES.put("useHeroAbility", CommandType.ACTION);
        CMD_TYPES.put("useEnvironmentCard", CommandType.ACTION);
        // Output commands
        CMD_TYPES.put("getCardsInHand", CommandType.OUTPUT);
        CMD_TYPES.put("getPlayerDeck", CommandType.OUTPUT);
        CMD_TYPES.put("getCardsOnTable", CommandType.OUTPUT);
        CMD_TYPES.put("getPlayerTurn", CommandType.OUTPUT);
        CMD_TYPES.put("getPlayerHero", CommandType.OUTPUT);
        CMD_TYPES.put("getCardAtPosition", CommandType.OUTPUT);
        CMD_TYPES.put("getPlayerMana", CommandType.OUTPUT);
        CMD_TYPES.put("getEnvironmentCardsInHand", CommandType.OUTPUT);
        CMD_TYPES.put("getFrozenCardsOnTable", CommandType.OUTPUT);
        CMD_TYPES.put("getTotalGamesPlayed", CommandType.OUTPUT);
        CMD_TYPES.put("getPlayerOneWins", CommandType.OUTPUT);
        CMD_TYPES.put("getPlayerTwoWins", CommandType.OUTPUT);
    }

    /**
     * Given a string return what type of instruction it is.
     *
     * @param command Command name
     * @return COMMAND TYPE
     */
    public static CommandType getType(final String command) {
        return CMD_TYPES.get(command);
    }
}
