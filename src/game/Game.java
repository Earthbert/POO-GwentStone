package game;

import cards.Card;
import cards.Minion;
import cards.environmentcards.Environment;
import cards.environmentcards.HeartHound;
import cards.heroes.EmpressThorina;
import cards.heroes.LordRoyce;
import cards.specialcards.Disciple;
import cards.specialcards.SpecialCard;
import deck.Deck;
import fileio.*;
import players.Player;
import table.Table;
import utils.CommandTypes;
import utils.ExceptionNoCommands;
import utils.ExceptionWonGame;

import java.util.ArrayList;

public class Game {
    private final GameMaster gameMaster;
    private int manaGain = 1;
    private final ArrayList<ActionsInput> commands;
    private int cmdInx = 0;
    private final int startingPlayer;
    private final int secondPlayer;
    public final Table table;
    private final Command command = new Command();


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
        this.gameMaster.getPlayer(2).startGame(gameInput.getStartGame().getPlayerOneHero(), playerTwoDeck);

        table = new Table();
    }

    void playTurn(int playerId) throws ExceptionWonGame, ExceptionNoCommands {
        loop:
        while (cmdInx < commands.size()) {
            ActionsInput command = commands.get(cmdInx);

            switch (CommandTypes.getType(command.getCommand())) {
                case TURNOVER -> {
                    break loop;
                }
                case ACTION -> this.command.action.handle(command, playerId);
                case OUTPUT -> this.command.output.handle(command, playerId);
            }

            cmdInx++;
            if (cmdInx == commands.size())
                throw new ExceptionNoCommands();
        }
        table.prepareTable(playerId);
    }

    void play() {
        while (true) {
            gameMaster.getPlayer(1).preparePlayer(manaGain);
            gameMaster.getPlayer(2).preparePlayer(manaGain);

            try {
                playTurn(startingPlayer);
            } catch (ExceptionNoCommands e) {
                return;
            } catch (ExceptionWonGame e) {
                System.out.println(e.toString());
            }

            try {
                playTurn(secondPlayer);
            } catch (ExceptionNoCommands e) {
                return;
            } catch (ExceptionWonGame e) {
                System.out.println(e.toString());
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
                    case "cardUsesAttack" -> cardUsesAttack(command.getCardAttacker(), command.getCardAttacked(), playerId);
                    case "cardUsesAbility" -> cardUsesAbility(command.getCardAttacker(), command.getCardAttacked(),playerId);
                    case "useAttackHero" -> useAttackHero(command.getCardAttacker(), playerId);
                    case "useHeroAbility" -> useHeroAbility(command.getAffectedRow(), playerId);
                    case "useEnvironmentCard" -> useEnvironmentCard(command.getAffectedRow(), playerId);
                }
            }

            public void placeCard(int handIdx, int playerId) {
                Card card = gameMaster.getPlayer(playerId).getDeck().getCardFromHand(handIdx);
                if (card instanceof Environment) {
                    //TODO PRINT ERROR
                    return;
                }
                if (gameMaster.getPlayer(playerId).getMana() < card.getMana()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (!table.placeCard((Minion) card, playerId)) {
                    //TODO PRINT ERROR
                    return;
                }
                gameMaster.getPlayer(playerId).getDeck().usedCard(handIdx);
            }

            public void cardUsesAttack(Coordinates attackerCord, Coordinates attackedCord, int playerId) {
                Minion attacker = table.getCard(attackerCord.getX(), attackerCord.getY());
                Minion attacked = table.getCard(attackedCord.getX(), attackedCord.getY());
                if (attacker.isFrozen()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (attacker.hasAttacked()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (table.whichPlayer(attackedCord.getX()) == playerId) {
                    //TODO PRINT ERROR
                    return;
                }
                if (!table.canAttack(playerId) && !attacked.isTank()) {
                    //TODO PRINT ERROR
                    return;
                }
                attacker.attack(attacked);
            }

            public void cardUsesAbility(Coordinates attackerCord, Coordinates attackedCord, int playerId) {
                Minion attacker = table.getCard(attackerCord.getX(), attackerCord.getY());
                Minion attacked = table.getCard(attackedCord.getX(), attackedCord.getY());
                if (attacker.isFrozen()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (attacker.hasAttacked()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (attacker instanceof Disciple) {
                    if (table.whichPlayer(attackedCord.getX()) != playerId) {
                        //TODO PRINT ERROR
                        return;
                    }
                } else {
                    if (table.whichPlayer(attackedCord.getX()) == playerId) {
                        //TODO PRINT ERROR
                        return;
                    }
                    if (!table.canAttack(playerId) && !attacked.isTank()) {
                        //TODO PRINT ERROR
                        return;
                    }
                }
                ((SpecialCard)attacker).useAbility(attacked);
            }

            public void useAttackHero(Coordinates attackerCord, int playerId) {
                Minion attacker = table.getCard(attackerCord.getX(), attackerCord.getY());
                if (attacker.isFrozen()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (attacker.hasAttacked()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (table.canAttack(playerId)) {
                    //TODO PRINT ERROR
                    return;
                }
                attacker.attack(gameMaster.getOtherPlayer(playerId).getHero());
            }

            public void useHeroAbility(int affectedRow, int playerId) {
                Player currentPlayer = gameMaster.getPlayer(playerId);
                if (currentPlayer.getMana() < currentPlayer.getHero().getMana()) {
                    //TODO ERROR
                    return;
                }
                if (currentPlayer.getHero().hasAttacked()) {
                    //TODO ERROR
                    return;
                }
                if (currentPlayer.getHero() instanceof LordRoyce || currentPlayer.getHero() instanceof EmpressThorina) {
                    if (table.whichPlayer(affectedRow) == playerId) {
                        //TODO ERROR
                        return;
                    }
                } else {
                    if (table.whichPlayer(affectedRow) != playerId) {
                        //TODO ERROR
                        return;
                    }
                }

                currentPlayer.getHero().useAbility(table.getRow(affectedRow));
            }

            public void useEnvironmentCard(int affectedRow, int playerId) {
                Card card = gameMaster.getPlayer(playerId).getDeck().getCardFromHand(affectedRow);
                if (!(card instanceof Environment)) {
                    //TODO PRINT ERROR
                    return;
                }
                if (gameMaster.getPlayer(playerId).getMana() < card.getMana()) {
                    //TODO PRINT ERROR
                    return;
                }
                if (table.whichPlayer(affectedRow) == playerId) {
                    //TODO PRINT ERROR
                    return;
                }
                if ((card instanceof HeartHound) && table.getRow(affectedRow).getNrOfCards() < 5) {
                    //TODO PRINT ERROR
                    return;
                }
                ((Environment)card).useEnvAbility(table.getRow(affectedRow));
            }
        }

        private class Output {
            public void handle(ActionsInput command, int playerId) {
                switch (command.getCommand()) {
                    case "getCardsInHand" -> getCardsInHand(command);
                    case "getPlayerDeck" -> getPlayerDeck(command);
                    case "getCardsOnTable" -> getCardsOnTable(command);
                    case "getPlayerTurn" -> getPlayerTurn(command, playerId);
                    case "getPlayerHero" -> getPlayerHero(command);
                    case "getCardAtPosition" -> getCardAtPosition(command);
                    case "getPlayerMana" -> getPlayerMana(command);
                    case "getEnvironmentCardsInHand" -> getEnvironmentCardsInHand(command);
                    case "getFrozenCardsOnTable" -> getFrozenCardsOnTable(command);
                    case "getTotalGamesPlayed" -> getTotalGamesPlayed(command);
                    case "getPlayerOneWins" -> getPlayerWins(command, 1);
                    case "getPlayerTwoWins" -> getPlayerWins(command, 2);
                }
            }

            private void getCardsInHand(ActionsInput command) {
            }

            private void getPlayerDeck(ActionsInput command) {
            }

            private void getCardsOnTable(ActionsInput command) {
            }

            private void getPlayerTurn(ActionsInput command, int playerId) {
            }

            private void getPlayerHero(ActionsInput command) {
            }

            private void getPlayerMana(ActionsInput command) {
            }

            private void getEnvironmentCardsInHand(ActionsInput command) {
            }

            private void getFrozenCardsOnTable(ActionsInput command) {
            }

            private void getTotalGamesPlayed(ActionsInput command) {
            }

            private void getPlayerWins(ActionsInput command, int playerId) {
            }

            private void getCardAtPosition(ActionsInput command) {
            }

        }
    }
}
