package game;

import cards.Card;
import cards.Minion;
import cards.environmentcards.Environment;
import cards.specialcards.SpecialCard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import deck.Deck;

import fileio.CardInput;
import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.Coordinates;
import fileio.GameInput;
import helpers.*;
import players.Player;

import table.Row;
import table.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * It is created a new instance for every game played.
 * The game ends when ExceptionNoCommands is thrown.
 */
public class Game {
    public static final int MAX_MANA_GAIN = 10;

    // Used to access field that remain between games.
    private final GameMaster gameMaster;
    private int manaGain = 1;
    private final ArrayList<ActionsInput> commands;
    private int cmdInx = 0;
    private final int startingPlayer;
    private final int secondPlayer;
    private final Table table;
    private final Command command = new Command();
    private boolean gameOver = false;

    /**
     * Prepares decks for both player and creates table object.
     *
     * @param gameInput      Information about the game.
     * @param playerOneDecks Decks available to player one.
     * @param playerTwoDecks Decks available to player two.
     * @param gameMaster     Reference to GameMaster.
     */
    public Game(final GameInput gameInput, final DecksInput playerOneDecks,
                final DecksInput playerTwoDecks, final GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        commands = gameInput.getActions();

        startingPlayer = gameInput.getStartGame().getStartingPlayer();
        if (startingPlayer == 1) {
            secondPlayer = 2;
        } else {
            secondPlayer = 1;
        }

        final int sSeed = gameInput.getStartGame().getShuffleSeed();

        final int playerOneIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        final CardInput playerOneHero = gameInput.getStartGame().getPlayerOneHero();
        final Deck playerOneDeck = new Deck(playerOneDecks.getDecks().get(playerOneIndex), sSeed);
        this.gameMaster.getPlayer(1).startGame(playerOneHero, playerOneDeck);

        final int playerTwoIndex = gameInput.getStartGame().getPlayerTwoDeckIdx();
        final CardInput playerTwoHero = gameInput.getStartGame().getPlayerTwoHero();
        final Deck playerTwoDeck = new Deck(playerTwoDecks.getDecks().get(playerTwoIndex), sSeed);
        this.gameMaster.getPlayer(2).startGame(playerTwoHero, playerTwoDeck);

        table = new Table();
    }

    /**
     * Plays a turn.
     * Take commands and calls different handles depending on type.
     * A turn end when command is of TURNOVER type.
     * At the of the turn prepare cards of current player for next turn.
     *
     * @param playerId which player's turn it is
     * @throws ExceptionNoCommands if all commands are finished.
     * @throws ExceptionWonGame    if enemy hero is dead.
     */
    void playTurn(final int playerId) {
        loop:
        while (true) {
            final ActionsInput cmd = commands.get(cmdInx);
            cmdInx++;
            switch (CommandTypes.getType(cmd.getCommand())) {
                case TURNOVER -> {
                    break loop;
                }
                case ACTION -> this.command.action.handle(cmd, playerId);
                case OUTPUT -> this.command.output.handle(cmd, playerId);
                case INVALID -> gameMaster.output.addPOJO(command.output.jsonCreator.invalidCmd(cmd.getCommand()));
                default -> { }
            }

            if (cmdInx >= commands.size()) {
                throw new ExceptionNoCommands();
            }
        }
        table.prepareTable(playerId);
        gameMaster.getPlayer(playerId).getHero().prepareHero();
    }

    /**
     * Prepares players for a new round.
     * Calls playTurn for both player.
     * If ExceptionWonGame is thrown prints gameOver massage, updates statistic and continues.
     * If ExceptionNoCommands is thrown it return to gameMaster.entry.
     */
    void play() {
        while (true) {
            if (!gameOver) {
                gameMaster.getPlayer(1).preparePlayer(manaGain);
                gameMaster.getPlayer(2).preparePlayer(manaGain);
            }

            try {
                playTurn(startingPlayer);
            } catch (final ExceptionNoCommands e) {
                return;
            } catch (final ExceptionWonGame e) {
                gameMaster.winGame(startingPlayer);
                gameOver = true;
                gameMaster.output.addPOJO(command.action.jsonCreator.gameOver(startingPlayer));
            }

            try {
                playTurn(secondPlayer);
            } catch (final ExceptionNoCommands e) {
                return;
            } catch (final ExceptionWonGame e) {
                gameMaster.winGame(secondPlayer);
                gameOver = true;
                gameMaster.output.addPOJO(command.action.jsonCreator.gameOver(secondPlayer));
            }

            if (manaGain < MAX_MANA_GAIN) {
                manaGain++;
            }
        }
    }

    // Helper class used to make it easier to manage the number of possible action.
    // It is divided in Action and Output classes.
    private class Command {
        private final Action action = new Action();
        private final Output output = new Output();

        // Handles commands that change state of the game.
        // Print only in case of errors or GameOver.
        private class Action {
            private final JsonCreator jsonCreator = new JsonCreator();

            public void handle(final ActionsInput cmd, final int playerId) {
                switch (cmd.getCommand()) {
                    case "placeCard" -> placeCard(cmd.getHandIdx(), playerId);
                    case "cardUsesAttack" ->
                        cardUsesAttack(cmd.getCardAttacker(), cmd.getCardAttacked(), playerId);
                    case "cardUsesAbility" ->
                        cardUsesAbility(cmd.getCardAttacker(), cmd.getCardAttacked(), playerId);
                    case "useAttackHero" -> useAttackHero(cmd.getCardAttacker(), playerId);
                    case "useHeroAbility" -> useHeroAbility(cmd.getAffectedRow(), playerId);
                    case "useEnvironmentCard" ->
                        useEnvironmentCard(cmd.getHandIdx(), cmd.getAffectedRow(), playerId);
                    default -> { }
                }
            }

            private void placeCard(final int handIdx, final int playerId) {
                final Player player = gameMaster.getPlayer(playerId);
                final Card card = player.getDeck().getCardFromHand(handIdx);
                if (UnitProp.getType(card.getName()) == UnitType.ENVIRONMENT) {
                    gameMaster.output.addPOJO(jsonCreator.placeCard(handIdx, Errors.PLACE_ENV));
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    gameMaster.output.addPOJO(jsonCreator.placeCard(handIdx, Errors.NO_MANA));
                    return;
                }
                if (!table.placeCard((Minion) card, playerId)) {
                    gameMaster.output.addPOJO(jsonCreator.placeCard(handIdx, Errors.FULL_ROW));
                    return;
                }
                player.getDeck().usedCard(handIdx);
                player.setMana(player.getMana() - card.getMana());
            }

            private void cardUsesAttack(final Coordinates attackerC,
                                       final Coordinates attackedC, final int playerId) {
                final Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                final Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(jsonCreator.cardUsesAttack(attackerC, attackedC,
                        Errors.FROZEN_ATTACKER));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(jsonCreator.cardUsesAttack(attackerC, attackedC,
                        Errors.ALREADY_ATTACKED));
                    return;
                }
                if (table.whichPlayer(attackedC.getX()) == playerId) {
                    gameMaster.output.addPOJO(jsonCreator.cardUsesAttack(attackerC, attackedC,
                        Errors.INVALID_ATTACK));
                    return;
                }
                if (table.isTankPlaced(playerId) && !attacked.isTank()) {
                    gameMaster.output.addPOJO(jsonCreator.cardUsesAttack(attackerC, attackedC,
                        Errors.NOT_TANK));
                    return;
                }
                attacker.attack(attacked);
            }

            private void cardUsesAbility(final Coordinates attackerC,
                                        final Coordinates attackedC, final int playerId) {
                final Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                final Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(jsonCreator.cardUsesAbility(attackerC, attackedC,
                        Errors.FROZEN_ATTACKER));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(jsonCreator.cardUsesAbility(attackerC, attackedC,
                        Errors.ALREADY_ATTACKED));
                    return;
                }
                if (UnitProp.isSupport(attacker.getName())) {
                    if (table.whichPlayer(attackedC.getX()) != playerId) {
                        gameMaster.output.addPOJO(jsonCreator.cardUsesAbility(attackerC, attackedC,
                            Errors.INVALID_SUPPORT));
                        return;
                    }
                } else {
                    if (table.whichPlayer(attackedC.getX()) == playerId) {
                        gameMaster.output.addPOJO(jsonCreator.cardUsesAbility(attackerC, attackedC,
                            Errors.INVALID_ATTACK));
                        return;
                    }
                    if (table.isTankPlaced(playerId) && !attacked.isTank()) {
                        gameMaster.output.addPOJO(jsonCreator.cardUsesAbility(attackerC, attackedC,
                            Errors.NOT_TANK));
                        return;
                    }
                }
                ((SpecialCard) attacker).useAbility(attacked);
            }

            private void useAttackHero(final Coordinates attackerC, final int playerId) {
                final Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(jsonCreator.useAttackHero(attackerC,
                        Errors.FROZEN_ATTACKER));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(jsonCreator.useAttackHero(attackerC,
                        Errors.ALREADY_ATTACKED));
                    return;
                }
                if (table.isTankPlaced(playerId)) {
                    gameMaster.output.addPOJO(jsonCreator.useAttackHero(attackerC,
                        Errors.NOT_TANK));
                    return;
                }
                attacker.attack(gameMaster.getEnemyPlayerPlayer(playerId).getHero());
            }

            private void useHeroAbility(final int affectedRow, final int playerId) {
                final Player player = gameMaster.getPlayer(playerId);
                if (player.getMana() < player.getHero().getMana()) {
                    gameMaster.output.addPOJO(jsonCreator.useHeroAbility(affectedRow,
                        Errors.NO_MANA_H));
                    return;
                }
                if (player.getHero().hasAttacked()) {
                    gameMaster.output.addPOJO(jsonCreator.useHeroAbility(affectedRow,
                        Errors.ALREADY_ATTACKED_H));
                    return;
                }
                if (!UnitProp.isSupport(player.getHero().getName())) {
                    if (table.whichPlayer(affectedRow) == playerId) {
                        gameMaster.output.addPOJO(jsonCreator.useHeroAbility(affectedRow,
                            Errors.ROW_ENEMY_H));
                        return;
                    }
                } else {
                    if (table.whichPlayer(affectedRow) != playerId) {
                        gameMaster.output.addPOJO(jsonCreator.useHeroAbility(affectedRow,
                            Errors.ROW_PLAYER));
                        return;
                    }
                }

                player.getHero().useAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - player.getHero().getMana());
            }

            private void useEnvironmentCard(final int handIdx, final int affectedRow,
                                           final int playerId) {
                final Player player = gameMaster.getPlayer(playerId);
                final Card card = player.getDeck().getCardFromHand(handIdx);
                if (!(UnitProp.getType(card.getName()) == UnitType.ENVIRONMENT)) {
                    gameMaster.output.addPOJO(jsonCreator.useEnvironmentCard(handIdx,
                        affectedRow, Errors.NOT_ENV));
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    gameMaster.output.addPOJO(jsonCreator.useEnvironmentCard(handIdx,
                        affectedRow, Errors.NO_MANA_E));
                    return;
                }
                if (table.whichPlayer(affectedRow) == playerId) {
                    gameMaster.output.addPOJO(jsonCreator.useEnvironmentCard(handIdx,
                        affectedRow, Errors.ROW_ENEMY));
                    return;
                }
                if ((card.getName().equals("Heart Hound"))
                    && table.getRow(Table.NR_ROWS - 1 - affectedRow).getNrOfCards() >= Row.MAX_R) {
                    gameMaster.output.addPOJO(jsonCreator.useEnvironmentCard(handIdx,
                        affectedRow, Errors.HEART_HOUND));
                    return;
                }
                ((Environment) card).useEnvAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - card.getMana());
                player.getDeck().usedCard(handIdx);
            }

            private class JsonCreator {
                private final ObjectMapper objectMapper = new ObjectMapper();

                private ObjectNode placeCard(final int handIdx, final String error) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "placeCard");
                    objectNode.put("handIdx", handIdx);
                    objectNode.put("error", error);
                    return objectNode;
                }

                private ObjectNode cardUsesAttack(final Coordinates cardAttacker,
                                                  final Coordinates cardAttacked,
                                                  final String error) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "cardUsesAttack");
                    objectNode.putPOJO("cardAttacker", cardAttacker);
                    objectNode.putPOJO("cardAttacked", cardAttacked);
                    objectNode.put("error", error);
                    return objectNode;
                }

                private ObjectNode cardUsesAbility(final Coordinates cardAttacker,
                                                 final Coordinates cardAttacked,
                                                  final String error) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "cardUsesAbility");
                    objectNode.putPOJO("cardAttacker", cardAttacker);
                    objectNode.putPOJO("cardAttacked", cardAttacked);
                    objectNode.put("error", error);
                    return objectNode;
                }

                private ObjectNode useAttackHero(final Coordinates cardAttacker,
                                                final String error) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "useAttackHero");
                    objectNode.putPOJO("cardAttacker", cardAttacker);
                    objectNode.put("error", error);
                    return objectNode;
                }

                private ObjectNode useHeroAbility(final int affectedRow, final String error) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "useHeroAbility");
                    objectNode.put("affectedRow", affectedRow);
                    objectNode.put("error", error);
                    return objectNode;
                }

                private ObjectNode useEnvironmentCard(final int handIdx,
                                                     final int affectedRow, final String error) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "useEnvironmentCard");
                    objectNode.put("handIdx", handIdx);
                    objectNode.put("affectedRow", affectedRow);
                    objectNode.put("error", error);
                    return objectNode;
                }

                public ObjectNode gameOver(final int playerId) {
                    final ObjectNode node = objectMapper.createObjectNode();
                    if (playerId == 1) {
                        node.put("gameEnded", Errors.PLAYER_ONE_KILL);
                    } else {
                        node.put("gameEnded", Errors.PLAYER_TWO_KILL);
                    }
                    return node;
                }
            }
        }

        // Handles commands that just print information about the state of the game.
        private class Output {
            JsonCreator jsonCreator = new JsonCreator();

            public void handle(final ActionsInput cmd, final int playerId) {
                switch (cmd.getCommand()) {
                    case "getCardsInHand" -> getCardsInHand(cmd.getPlayerIdx());
                    case "getPlayerDeck" -> getPlayerDeck(cmd.getPlayerIdx());
                    case "getCardsOnTable" -> getCardsOnTable();
                    case "getPlayerTurn" -> getPlayerTurn(playerId);
                    case "getPlayerHero" -> getPlayerHero(cmd.getPlayerIdx());
                    case "getCardAtPosition" -> getCardAtPosition(cmd.getX(), cmd.getY());
                    case "getPlayerMana" -> getPlayerMana(cmd.getPlayerIdx());
                    case "getEnvironmentCardsInHand" ->
                        getEnvironmentCardsInHand(cmd.getPlayerIdx());
                    case "getFrozenCardsOnTable" -> getFrozenCardsOnTable();
                    case "getTotalGamesPlayed" -> getTotalGamesPlayed();
                    case "getPlayerOneWins" -> getPlayerWins(1);
                    case "getPlayerTwoWins" -> getPlayerWins(2);
                    default -> { }
                }
            }

            private void getCardsInHand(final int playerId) {
                final ArrayList<Card> cards = new ArrayList<>();
                for (final Card card : gameMaster.getPlayer(playerId).getDeck().getCardsOnHand()) {
                    cards.add(card.clone());
                }
                gameMaster.output.addPOJO(jsonCreator.getCardsInHand(playerId, cards));
            }

            private void getPlayerDeck(final int playerId) {
                final ArrayList<Card> cards = new ArrayList<>();
                for (final Card card : gameMaster.getPlayer(playerId).getDeck().getCardsOnDeck()) {
                    cards.add(card.clone());
                }
                gameMaster.output.addPOJO(jsonCreator.getPlayerDeck(playerId, cards));
            }

            private void getCardsOnTable() {
                final List<List<Card>> rows = new ArrayList<>();
                for (int i = 0; i < Table.NR_ROWS; i++) {
                    final Row row = table.getRow(i);
                    rows.add(row.copyOfCards());
                }
                gameMaster.output.addPOJO(jsonCreator.getCardsOnTable(rows));
            }

            private void getPlayerTurn(final int playerId) {
                gameMaster.output.addPOJO(jsonCreator.getPlayerTurn(playerId));
            }

            private void getPlayerHero(final int playerId) {
                final Card hero = gameMaster.getPlayer(playerId).getHero().clone();
                gameMaster.output.addPOJO(jsonCreator.getPlayerHero(playerId, hero));
            }

            private void getCardAtPosition(final int rowNr, final int cardIdx) {
                final Card card = (table.getCard(rowNr, cardIdx));
                if (card == null) {
                    gameMaster.output.addPOJO(jsonCreator.getCardAtPosition(rowNr,cardIdx, Errors.NO_CARD_POS));
                } else {
                    gameMaster.output.addPOJO(jsonCreator.getCardAtPosition(rowNr, cardIdx, card.clone()));
                }
            }

            private void getPlayerMana(final int playerId) {
                final int mana = gameMaster.getPlayer(playerId).getMana();
                gameMaster.output.addPOJO(jsonCreator.getPlayerMana(playerId, mana));
            }

            private void getEnvironmentCardsInHand(final int playerId) {
                final List<Card> cards = new ArrayList<>();
                for (final Card card : gameMaster.getPlayer(playerId).getDeck().getCardsOnHand()) {
                    if (UnitProp.getType(card.getName()) == UnitType.ENVIRONMENT) {
                        cards.add(card.clone());
                    }
                }
                gameMaster.output.addPOJO(jsonCreator.getEnvironmentCardsInHand(playerId, cards));
            }

            private void getFrozenCardsOnTable() {
                final List<Card> cards = new ArrayList<>();
                for (int i = 0; i < Table.NR_ROWS; i++) {
                    final Row row = table.getRow(i);
                    for (final Minion card : row.getCardsOnRow()) {
                        if (card.isFrozen()) {
                            cards.add(card.clone());
                        }
                    }
                }
                gameMaster.output.addPOJO(jsonCreator.getFrozenCardsOnTable(cards));
            }

            private void getTotalGamesPlayed() {
                final int totalGames = gameMaster.getTotalGames();
                gameMaster.output.addPOJO(jsonCreator.getTotalGamesPlayed(totalGames));
            }

            private void getPlayerWins(final int playerId) {
                final int gamesWon = gameMaster.getPlayer(playerId).getGamesWon();
                gameMaster.output.addPOJO(jsonCreator.getPlayerWins(playerId, gamesWon));
            }

            private class JsonCreator {
                private final ObjectMapper objectMapper = new ObjectMapper();

                private ObjectNode getCardsInHand(final int playerIdx, final List<Card> cards) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getCardsInHand");
                    objectNode.put("playerIdx", playerIdx);
                    objectNode.putPOJO("output", cards);
                    return objectNode;
                }

                private ObjectNode getPlayerDeck(final int playerIdx, final List<Card> cards) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getPlayerDeck");
                    objectNode.put("playerIdx", playerIdx);
                    objectNode.putPOJO("output", cards);
                    return objectNode;
                }

                private ObjectNode getCardsOnTable(final List<List<Card>> rows) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getCardsOnTable");
                    objectNode.putPOJO("output", rows);
                    return objectNode;
                }

                private ObjectNode getPlayerTurn(final int activePlayer) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getPlayerTurn");
                    objectNode.put("output", activePlayer);
                    return objectNode;
                }

                private ObjectNode getPlayerHero(final int playerIdx, final Card hero) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getPlayerHero");
                    objectNode.put("playerIdx", playerIdx);
                    objectNode.putPOJO("output", hero);
                    return objectNode;
                }

                private ObjectNode getCardAtPosition(final int x, final int y, final Object out) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getCardAtPosition");
                    objectNode.put("x", x);
                    objectNode.put("y", y);
                    objectNode.putPOJO("output", out);
                    return objectNode;
                }

                private ObjectNode getPlayerMana(final int playerIdx, final int mana) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getPlayerMana");
                    objectNode.put("playerIdx", playerIdx);
                    objectNode.put("output", mana);
                    return objectNode;
                }

                private ObjectNode getEnvironmentCardsInHand(final int playerIdx,
                                                            final List<Card> cards) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getEnvironmentCardsInHand");
                    objectNode.put("playerIdx", playerIdx);
                    objectNode.putPOJO("output", cards);
                    return objectNode;
                }

                private ObjectNode getFrozenCardsOnTable(final List<Card> cards) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getFrozenCardsOnTable");
                    objectNode.putPOJO("output", cards);
                    return objectNode;
                }

                private ObjectNode getTotalGamesPlayed(final int gamesPlayed) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "getTotalGamesPlayed");
                    objectNode.put("output", gamesPlayed);
                    return objectNode;
                }

                private ObjectNode getPlayerWins(final int playerIdx, final int gamesWon) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    if (playerIdx == 1) {
                        objectNode.put("command", "getPlayerOneWins");
                    } else {
                        objectNode.put("command", "getPlayerTwoWins");
                    }
                    objectNode.put("output", gamesWon);
                    return objectNode;
                }

                private ObjectNode invalidCmd(final String cmd) {
                    final ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", cmd);
                    objectNode.put("error:", Errors.INVALID_CMD);
                    return objectNode;
                }
            }
        }
    }
}
