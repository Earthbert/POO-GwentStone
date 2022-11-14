package game;

import cards.Card;
import cards.Minion;
import cards.environmentcards.*;
import cards.heroes.*;
import cards.specialcards.*;
import deck.Deck;
import fileio.ActionsInput;
import fileio.Coordinates;
import fileio.DecksInput;
import fileio.GameInput;
import game.errors.*;
import game.errors.GameOver;
import game.outputs.*;
import players.Player;
import table.Row;
import table.Table;
import utils.*;
import utils.Error;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private final GameMaster gameMaster;
    private int manaGain = 1;
    private final ArrayList<ActionsInput> commands;
    private int cmdInx = 0;
    private final int startingPlayer;
    private final int secondPlayer;
    public final Table table;
    private final Command command = new Command();
    private boolean gameOver = false;


    public Game(GameInput gameInput, DecksInput playerOneDecks, DecksInput playerTwoDecks, GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        commands = gameInput.getActions();

        startingPlayer = gameInput.getStartGame().getStartingPlayer();
        if (startingPlayer == 1)
            secondPlayer = 2;
        else
            secondPlayer = 1;

        int shuffleSeed = gameInput.getStartGame().getShuffleSeed();

        int playerOneIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        Deck playerOneDeck = new Deck(playerOneDecks.getDecks().get(playerOneIndex), shuffleSeed);
        this.gameMaster.getPlayer(1).startGame(gameInput.getStartGame().getPlayerOneHero(), playerOneDeck);

        int playerTwoIndex = gameInput.getStartGame().getPlayerTwoDeckIdx();
        Deck playerTwoDeck = new Deck(playerTwoDecks.getDecks().get(playerTwoIndex), shuffleSeed);
        this.gameMaster.getPlayer(2).startGame(gameInput.getStartGame().getPlayerTwoHero(), playerTwoDeck);

        table = new Table();
    }

    void playTurn(int playerId) {
        loop:
        while (true) {
            ActionsInput command = commands.get(cmdInx);
            cmdInx++;
            switch (CommandTypes.getType(command.getCommand())) {
                case TURNOVER -> {
                    break loop;
                }
                case ACTION -> this.command.action.handle(command, playerId);
                case OUTPUT -> this.command.output.handle(command, playerId);
            }

            if (cmdInx >= commands.size())
                throw new ExceptionNoCommands();
        }
        table.prepareTable(playerId);
        gameMaster.getPlayer(playerId).getHero().prepareHero();
    }

    void play() {
        while (true) {
            if (!gameOver) {
                gameMaster.getPlayer(1).preparePlayer(manaGain);
                gameMaster.getPlayer(2).preparePlayer(manaGain);
            }

            try {
                playTurn(startingPlayer);
            } catch (ExceptionNoCommands e) {
                return;
            } catch (ExceptionWonGame e) {
                gameMaster.winGame(startingPlayer);
                gameOver = true;
                gameMaster.output.addPOJO(new GameOver(startingPlayer));
            }

            try {
                playTurn(secondPlayer);
            } catch (ExceptionNoCommands e) {
                return;
            } catch (ExceptionWonGame e) {
                gameMaster.winGame(secondPlayer);
                gameOver = true;
                gameMaster.output.addPOJO(new GameOver(secondPlayer));
            }

            if (manaGain < 10) {
                manaGain++;
            }
        }
    }

    private class Command {
        Action action = new Action();
        Output output = new Output();

        private class Action {
            public void handle(ActionsInput command, int playerId) {
                switch (command.getCommand()) {
                    case "placeCard" -> placeCard(command.getHandIdx(), playerId);
                    case "cardUsesAttack" ->
                        cardUsesAttack(command.getCardAttacker(), command.getCardAttacked(), playerId);
                    case "cardUsesAbility" ->
                        cardUsesAbility(command.getCardAttacker(), command.getCardAttacked(), playerId);
                    case "useAttackHero" -> useAttackHero(command.getCardAttacker(), playerId);
                    case "useHeroAbility" -> useHeroAbility(command.getAffectedRow(), playerId);
                    case "useEnvironmentCard" ->
                        useEnvironmentCard(command.getHandIdx(), command.getAffectedRow(), playerId);
                }
            }

            public void placeCard(int handIdx, int playerId) {
                Player player = gameMaster.getPlayer(playerId);
                Card card = player.getDeck().getCardFromHand(handIdx);
                if (card instanceof Environment) {
                    gameMaster.output.addPOJO(new PlaceCardError(handIdx, Error.getString(ErrorType.PLACE_ENV)));
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    gameMaster.output.addPOJO(new PlaceCardError(handIdx, Error.getString(ErrorType.NO_MANA)));
                    return;
                }
                if (!table.placeCard((Minion) card, playerId)) {
                    gameMaster.output.addPOJO(new PlaceCardError(handIdx, Error.getString(ErrorType.FULL_ROW)));
                    return;
                }
                player.getDeck().usedCard(handIdx);
                player.setMana(player.getMana() - card.getMana());
            }

            public void cardUsesAttack(Coordinates attackerC, Coordinates attackedC, int playerId) {
                Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC, Error.getString(ErrorType.FROZEN_ATTACKER)));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC, Error.getString(ErrorType.ALREADY_ATTACKED)));
                    return;
                }
                if (table.whichPlayer(attackedC.getX()) == playerId) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC, Error.getString(ErrorType.INVALID_ATTACK)));
                    return;
                }
                if (!table.canAttack(playerId) && !attacked.isTank()) {
                    gameMaster.output.addPOJO(new CardUsesAttackError(attackerC, attackedC, Error.getString(ErrorType.NOT_TANK)));
                    return;
                }
                attacker.attack(attacked);
            }

            public void cardUsesAbility(Coordinates attackerC, Coordinates attackedC, int playerId) {
                Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC, Error.getString(ErrorType.FROZEN_ATTACKER)));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC, Error.getString(ErrorType.ALREADY_ATTACKED)));
                    return;
                }
                if (attacker instanceof Disciple) {
                    if (table.whichPlayer(attackedC.getX()) != playerId) {
                        gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC, Error.getString(ErrorType.INVALID_HEAL)));
                        return;
                    }
                } else {
                    if (table.whichPlayer(attackedC.getX()) == playerId) {
                        gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC, Error.getString(ErrorType.INVALID_ATTACK)));
                        return;
                    }
                    if (!table.canAttack(playerId) && !attacked.isTank()) {
                        gameMaster.output.addPOJO(new CardUsesAbilityError(attackerC, attackedC, Error.getString(ErrorType.NOT_TANK)));
                        return;
                    }
                }
                ((SpecialCard) attacker).useAbility(attacked);
            }

            public void useAttackHero(Coordinates attackerC, int playerId) {
                Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                if (attacker.isFrozen()) {
                    gameMaster.output.addPOJO(new UseAttackHeroError(attackerC, Error.getString(ErrorType.FROZEN_ATTACKER)));
                    return;
                }
                if (attacker.hasAttacked()) {
                    gameMaster.output.addPOJO(new UseAttackHeroError(attackerC, Error.getString(ErrorType.ALREADY_ATTACKED)));
                    return;
                }
                if (!table.canAttack(playerId)) {
                    gameMaster.output.addPOJO(new UseAttackHeroError(attackerC, Error.getString(ErrorType.NOT_TANK)));
                    return;
                }
                attacker.attack(gameMaster.getEnemyPlayerPlayer(playerId).getHero());
            }

            public void useHeroAbility(int affectedRow, int playerId) {
                Player player = gameMaster.getPlayer(playerId);
                if (player.getMana() < player.getHero().getMana()) {
                    gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow, Error.getString(ErrorType.NO_MANA_H)));
                    return;
                }
                if (player.getHero().hasAttacked()) {
                    gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow, Error.getString(ErrorType.ALREADY_ATTACKED_H)));
                    return;
                }
                if (player.getHero() instanceof LordRoyce || player.getHero() instanceof EmpressThorina) {
                    if (table.whichPlayer(affectedRow) == playerId) {
                        gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow, Error.getString(ErrorType.ROW_ENEMY_H)));
                        return;
                    }
                } else {
                    if (table.whichPlayer(affectedRow) != playerId) {
                        gameMaster.output.addPOJO(new UseHeroAbilityError(affectedRow, Error.getString(ErrorType.ROW_PLAYER)));
                        return;
                    }
                }

                player.getHero().useAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - player.getHero().getMana());
            }

            public void useEnvironmentCard(int handIdx, int affectedRow, int playerId) {
                Player player = gameMaster.getPlayer(playerId);
                Card card = player.getDeck().getCardFromHand(handIdx);
                if (!(card instanceof Environment)) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx, affectedRow, Error.getString(ErrorType.NOT_ENV)));
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx, affectedRow, Error.getString(ErrorType.NO_MANA_E)));
                    return;
                }
                if (table.whichPlayer(affectedRow) == playerId) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx, affectedRow, Error.getString(ErrorType.ROW_ENEMY)));
                    return;
                }
                if ((card instanceof HeartHound) && table.getRow(3 - affectedRow).getNrOfCards() >= 5) {
                    gameMaster.output.addPOJO(new UseEnvironmentCardError(handIdx, affectedRow, Error.getString(ErrorType.HEART_HOUND)));
                    return;
                }
                ((Environment) card).useEnvAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - card.getMana());
                player.getDeck().usedCard(handIdx);
            }
        }

        private class Output {
            public void handle(ActionsInput command, int playerId) {
                switch (command.getCommand()) {
                    case "getCardsInHand" -> getCardsInHand(command.getPlayerIdx());
                    case "getPlayerDeck" -> getPlayerDeck(command.getPlayerIdx());
                    case "getCardsOnTable" -> getCardsOnTable();
                    case "getPlayerTurn" -> getPlayerTurn(playerId);
                    case "getPlayerHero" -> getPlayerHero(command.getPlayerIdx());
                    case "getCardAtPosition" -> getCardAtPosition(command.getX(), command.getY());
                    case "getPlayerMana" -> getPlayerMana(command.getPlayerIdx());
                    case "getEnvironmentCardsInHand" -> getEnvironmentCardsInHand(command.getPlayerIdx());
                    case "getFrozenCardsOnTable" -> getFrozenCardsOnTable();
                    case "getTotalGamesPlayed" -> getTotalGamesPlayed();
                    case "getPlayerOneWins" -> getPlayerWins(1);
                    case "getPlayerTwoWins" -> getPlayerWins(2);
                }
            }

            private void getCardsInHand(int playerId) {
                List<Card> cards = gameMaster.getPlayer(playerId).getDeck().getCardsOnHand();
                gameMaster.output.addPOJO(new GetCardsInHandOutput(playerId, cards));
            }

            private void getPlayerDeck(int playerId) {
                List<Card> cards = gameMaster.getPlayer(playerId).getDeck().getCardsOnDeck();
                gameMaster.output.addPOJO(new GetPlayerDeckOutput(playerId, cards));
            }

            private void getCardsOnTable() {
                List<List<Card>> rows = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Row row = table.getRow(i);
                    rows.add(row.copyOfCards());
                }
                gameMaster.output.addPOJO(new GetCardsOnTableOutput(rows));
            }

            private void getPlayerTurn(int playerId) {
                gameMaster.output.addPOJO(new GetPlayerTurnOutput(playerId));
            }

            private void getPlayerHero(int playerId) {
                Hero hero = gameMaster.getPlayer(playerId).getHero();
                gameMaster.output.addPOJO(new GetPlayerHeroOutput(playerId, hero));
            }

            private void getCardAtPosition(int rowNr, int cardIdx) {
                Card card = table.getCard(rowNr, cardIdx);
                gameMaster.output.addPOJO(new GetCardAtPositionOutput(rowNr, cardIdx, card));
            }

            private void getPlayerMana(int playerId) {
                int mana = gameMaster.getPlayer(playerId).getMana();
                gameMaster.output.addPOJO(new GetPlayerManaOutput(playerId, mana));
            }

            private void getEnvironmentCardsInHand(int playerId) {
                List<Card> cards = new ArrayList<>();
                for (Card card : gameMaster.getPlayer(playerId).getDeck().getCardsOnHand()) {
                    if (card instanceof Environment) {
                        cards.add(card.clone());
                    }
                }
                gameMaster.output.addPOJO(new GetEnvironmentCardsInHandOutput(playerId, cards));
            }

            private void getFrozenCardsOnTable() {
                List<Card> cards = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Row row = table.getRow(i);
                    for (Minion card : row.getCardsOnRow()) {
                        if (card.isFrozen())
                            cards.add(card.clone());
                    }
                }
                gameMaster.output.addPOJO(new GetFrozenCardsOnTableOutput(cards));
            }

            private void getTotalGamesPlayed() {
                int totalGames = gameMaster.getTotalGames();
                gameMaster.output.addPOJO(new GetTotalGamesPlayedOutput(totalGames));
            }

            private void getPlayerWins(int playerId) {
                int gamesWon = gameMaster.getPlayer(playerId).getGamesWon();
                gameMaster.output.addPOJO(new GetPlayerWinsOutput(playerId, gamesWon));
            }

        }
    }
}
