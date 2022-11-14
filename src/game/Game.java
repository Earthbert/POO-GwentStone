package game;

import cards.Card;
import cards.Minion;
import cards.environmentcards.Environment;
import cards.environmentcards.HeartHound;
import cards.heroes.EmpressThorina;
import cards.heroes.Hero;
import cards.heroes.LordRoyce;
import cards.specialcards.Disciple;
import cards.specialcards.SpecialCard;

import deck.Deck;

import fileio.CardInput;
import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.Coordinates;
import fileio.GameInput;
// Imported like this to pass checkstyle :(
import game.errors.PlaceCardError;
import game.errors.GameOver;
import game.errors.CardUsesAttackError;
import game.errors.UseHeroAbilityError;
import game.errors.CardUsesAbilityError;
import game.errors.UseAttackHeroError;
import game.errors.UseEnvironmentCardError;

import game.outputs.GetCardAtPositionOutput;
import game.outputs.GetCardsInHandOutput;
import game.outputs.GetCardsOnTableOutput;
import game.outputs.GetEnvironmentCardsInHandOutput;
import game.outputs.GetFrozenCardsOnTableOutput;
import game.outputs.GetPlayerDeckOutput;
import game.outputs.GetPlayerHeroOutput;
import game.outputs.GetPlayerManaOutput;
import game.outputs.GetPlayerTurnOutput;
import game.outputs.GetPlayerWinsOutput;
import game.outputs.GetTotalGamesPlayedOutput;

import helpers.CommandTypes;
import helpers.Errors;
import helpers.ExceptionNoCommands;
import helpers.ExceptionWonGame;

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
                default -> System.out.println(Errors.INVALID_CMD);
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
                gameMaster.output.addPOJO(new GameOver(startingPlayer));
            }

            try {
                playTurn(secondPlayer);
            } catch (final ExceptionNoCommands e) {
                return;
            } catch (final ExceptionWonGame e) {
                gameMaster.winGame(secondPlayer);
                gameOver = true;
                gameMaster.output.addPOJO(new GameOver(secondPlayer));
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
                    default -> System.out.println(Errors.INVALID_CMD);
                }
            }

            public void placeCard(final int handIdx, final int playerId) {
                final Player player = gameMaster.getPlayer(playerId);
                final Card card = player.getDeck().getCardFromHand(handIdx);
                if (card instanceof Environment) {
                    gameMaster.output.addPOJO(new PlaceCardError(handIdx, Errors.PLACE_ENV));
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    gameMaster.output.addPOJO(new PlaceCardError(handIdx, Errors.NO_MANA));
                    return;
                }
                if (!table.placeCard((Minion) card, playerId)) {
                    gameMaster.output.addPOJO(new PlaceCardError(handIdx, Errors.FULL_ROW));
                    return;
                }
                player.getDeck().usedCard(handIdx);
                player.setMana(player.getMana() - card.getMana());
            }

            public void cardUsesAttack(final Coordinates attackerC,
                                       final Coordinates attackedC, final int playerId) {
                final Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                final Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC,
                        Errors.FROZEN_ATTACKER));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC,
                        Errors.ALREADY_ATTACKED));
                    return;
                }
                if (table.whichPlayer(attackedC.getX()) == playerId) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC,
                        Errors.INVALID_ATTACK));
                    return;
                }
                if (table.isTankPlaced(playerId) && !attacked.isTank()) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC,
                        Errors.NOT_TANK));
                    return;
                }
                attacker.attack(attacked);
            }

            public void cardUsesAbility(final Coordinates attackerC,
                                        final Coordinates attackedC, final int playerId) {
                final Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                final Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC,
                        Errors.FROZEN_ATTACKER));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC,
                        Errors.ALREADY_ATTACKED));
                    return;
                }
                if (attacker instanceof Disciple) {
                    if (table.whichPlayer(attackedC.getX()) != playerId) {
                        gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC,
                            Errors.INVALID_HEAL));
                        return;
                    }
                } else {
                    if (table.whichPlayer(attackedC.getX()) == playerId) {
                        gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC,
                            Errors.INVALID_ATTACK));
                        return;
                    }
                    if (table.isTankPlaced(playerId) && !attacked.isTank()) {
                        gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC,
                            Errors.NOT_TANK));
                        return;
                    }
                }
                ((SpecialCard) attacker).useAbility(attacked);
            }

            public void useAttackHero(final Coordinates attackerC, final int playerId) {
                final Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(new UseAttackHeroError(attackerC,
                        Errors.FROZEN_ATTACKER));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(new UseAttackHeroError(attackerC,
                        Errors.ALREADY_ATTACKED));
                    return;
                }
                if (table.isTankPlaced(playerId)) {
                    gameMaster.output.addPOJO(new UseAttackHeroError(attackerC,
                        Errors.NOT_TANK));
                    return;
                }
                attacker.attack(gameMaster.getEnemyPlayerPlayer(playerId).getHero());
            }

            public void useHeroAbility(final int affectedRow, final int playerId) {
                final Player player = gameMaster.getPlayer(playerId);
                if (player.getMana() < player.getHero().getMana()) {
                    gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow,
                        Errors.NO_MANA_H));
                    return;
                }
                if (player.getHero().hasAttacked()) {
                    gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow,
                        Errors.ALREADY_ATTACKED_H));
                    return;
                }
                if (player.getHero() instanceof LordRoyce
                    || player.getHero() instanceof EmpressThorina) {
                    if (table.whichPlayer(affectedRow) == playerId) {
                        gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow,
                            Errors.ROW_ENEMY_H));
                        return;
                    }
                } else {
                    if (table.whichPlayer(affectedRow) != playerId) {
                        gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow,
                            Errors.ROW_PLAYER));
                        return;
                    }
                }

                player.getHero().useAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - player.getHero().getMana());
            }

            public void useEnvironmentCard(final int handIdx, final int affectedRow,
                                           final int playerId) {
                final Player player = gameMaster.getPlayer(playerId);
                final Card card = player.getDeck().getCardFromHand(handIdx);
                if (!(card instanceof Environment)) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx,
                        affectedRow, Errors.NOT_ENV));
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx,
                        affectedRow, Errors.NO_MANA_E));
                    return;
                }
                if (table.whichPlayer(affectedRow) == playerId) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx,
                        affectedRow, Errors.ROW_ENEMY));
                    return;
                }
                if ((card instanceof HeartHound)
                    && table.getRow(Table.NR_ROWS - 1 - affectedRow).getNrOfCards() >= Row.MAX_R) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx,
                        affectedRow, Errors.HEART_HOUND));
                    return;
                }
                ((Environment) card).useEnvAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - card.getMana());
                player.getDeck().usedCard(handIdx);
            }
        }

        // Handles commands that just print information about the state of the game.
        private class Output {
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
                    default -> System.out.println(Errors.INVALID_CMD);
                }
            }

            private void getCardsInHand(final int playerId) {
                final List<Card> cards = gameMaster.getPlayer(playerId).getDeck().getCardsOnHand();
                gameMaster.output.addPOJO(new GetCardsInHandOutput(playerId, cards));
            }

            private void getPlayerDeck(final int playerId) {
                final List<Card> cards = gameMaster.getPlayer(playerId).getDeck().getCardsOnDeck();
                gameMaster.output.addPOJO(new GetPlayerDeckOutput(playerId, cards));
            }

            private void getCardsOnTable() {
                final List<List<Card>> rows = new ArrayList<>();
                for (int i = 0; i < Table.NR_ROWS; i++) {
                    final Row row = table.getRow(i);
                    rows.add(row.copyOfCards());
                }
                gameMaster.output.addPOJO(new GetCardsOnTableOutput(rows));
            }

            private void getPlayerTurn(final int playerId) {
                gameMaster.output.addPOJO(new GetPlayerTurnOutput(playerId));
            }

            private void getPlayerHero(final int playerId) {
                final Hero hero = gameMaster.getPlayer(playerId).getHero();
                gameMaster.output.addPOJO(new GetPlayerHeroOutput(playerId, hero));
            }

            private void getCardAtPosition(final int rowNr, final int cardIdx) {
                final Card card = table.getCard(rowNr, cardIdx);
                gameMaster.output.addPOJO(new GetCardAtPositionOutput(rowNr, cardIdx, card));
            }

            private void getPlayerMana(final int playerId) {
                final int mana = gameMaster.getPlayer(playerId).getMana();
                gameMaster.output.addPOJO(new GetPlayerManaOutput(playerId, mana));
            }

            private void getEnvironmentCardsInHand(final int playerId) {
                final List<Card> cards = new ArrayList<>();
                for (final Card card : gameMaster.getPlayer(playerId).getDeck().getCardsOnHand()) {
                    if (card instanceof Environment) {
                        cards.add(card.clone());
                    }
                }
                gameMaster.output.addPOJO(new GetEnvironmentCardsInHandOutput(playerId, cards));
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
                gameMaster.output.addPOJO(new GetFrozenCardsOnTableOutput(cards));
            }

            private void getTotalGamesPlayed() {
                final int totalGames = gameMaster.getTotalGames();
                gameMaster.output.addPOJO(new GetTotalGamesPlayedOutput(totalGames));
            }

            private void getPlayerWins(final int playerId) {
                final int gamesWon = gameMaster.getPlayer(playerId).getGamesWon();
                gameMaster.output.addPOJO(new GetPlayerWinsOutput(playerId, gamesWon));
            }

        }
    }
}
