package game.command;

import fileio.ActionsInput;

public class Command {
    public static class Action {
        public static void placeCard(ActionsInput command, int playerId) {
        }

        public static void cardUsesAttack(ActionsInput command, int playerId) {
        }

        public static void cardUsesAbility(ActionsInput command, int playerId) {
        }

        public static void useAttackHero(ActionsInput command, int playerId) {
        }

        public static void useHeroAbility(ActionsInput command, int playerId) {
        }

        public static void useEnvironmentCard(ActionsInput command, int playerId) {
        }
        
        public static void handle(ActionsInput command, int playerId) {
            switch (command.getCommand()) {
                case "placeCard" -> placeCard(command, playerId);
                case "cardUsesAttack" -> cardUsesAttack(command, playerId);
                case "cardUsesAbility" -> cardUsesAbility(command, playerId);
                case "useAttackHero" -> useAttackHero(command, playerId);
                case "useHeroAbility" -> useHeroAbility(command, playerId);
                case "useEnvironmentCard" -> useEnvironmentCard(command, playerId);
            }
        }
    }

    public static class Output {
        public static void handle(ActionsInput command, int playerId) {
        }
    }
}