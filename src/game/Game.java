package game;

import cards.Attackable;
import cards.Card;
import cards.Minion;
import cards.environmentcards.*;
import cards.heroes.*;
import cards.specialcards.*;;
import deck.Deck;
import fileio.ActionsInput;
import fileio.Coordinates;
import fileio.DecksInput;
import fileio.GameInput;
import game.errors.*;
import game.errors.GameOver;
import players.Player;
import table.Table;
import utils.*;

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

    void playTurn(int playerId) {
        loop: while (true) {
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
                gameMaster.getPlayer(startingPlayer).winGame();
                new GameOver(startingPlayer, gameMaster.output);
                return;
            }

            try {
                playTurn(secondPlayer);
            } catch (ExceptionNoCommands e) {
                return;
            } catch (ExceptionWonGame e) {
                gameMaster.getPlayer(secondPlayer).winGame();
                new GameOver(secondPlayer, gameMaster.output);
                return;
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
                    case "useEnvironmentCard" -> useEnvironmentCard(command.getHandIdx(), command.getAffectedRow(), playerId);
                }
            }

            public void placeCard(int handIdx, int playerId) {
                Player player = gameMaster.getPlayer(playerId);
                Card card = player.getDeck().getCardFromHand(handIdx);
                if (card instanceof Environment) {
                    new PlaceCardError(handIdx, ErrorTypes.getType(ErrorType.PLACE_ENV), gameMaster.output);
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    new PlaceCardError(handIdx, ErrorTypes.getType(ErrorType.NO_MANA), gameMaster.output);
                    return;
                }
                if (!table.placeCard((Minion) card, playerId)) {
                    new PlaceCardError(handIdx, ErrorTypes.getType(ErrorType.FULL_ROW), gameMaster.output);
                    return;
                }
                player.getDeck().usedCard(handIdx);
                player.setMana(player.getMana() - card.getMana());
            }

            public void cardUsesAttack(Coordinates attackerC, Coordinates attackedC, int playerId) {
                Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    new CardUsesAttackError(attackerC, attackedC, ErrorTypes.getType(ErrorType.FROZEN_ATTACKER), gameMaster.output);
                    return;
                }
                if (attacker.hasAttacked()) {
                    new CardUsesAttackError(attackerC, attackedC, ErrorTypes.getType(ErrorType.ALREADY_ATTACKED), gameMaster.output);
                    return;
                }
                if (table.whichPlayer(attackedC.getX()) == playerId) {
                    new CardUsesAttackError(attackerC, attackedC, ErrorTypes.getType(ErrorType.INVALID_ATTACK), gameMaster.output);
                    return;
                }
                if (!table.canAttack(playerId) && !attacked.isTank()) {
                    new CardUsesAttackError(attackerC, attackedC, ErrorTypes.getType(ErrorType.NOT_TANK), gameMaster.output);
                    return;
                }
                attacker.attack(attacked);
            }

            public void cardUsesAbility(Coordinates attackerC, Coordinates attackedC, int playerId) {
                Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                Minion attacked = table.getCard(attackedC.getX(), attackedC.getY());
                if (attacker.isFrozen()) {
                    new CardUsesAbilityError(attackerC, attackedC, ErrorTypes.getType(ErrorType.FROZEN_ATTACKER), gameMaster.output);
                    return;
                }
                if (attacker.hasAttacked()) {
                    new CardUsesAbilityError(attackerC, attackedC, ErrorTypes.getType(ErrorType.ALREADY_ATTACKED), gameMaster.output);
                    return;
                }
                if (attacker instanceof Disciple) {
                    if (table.whichPlayer(attackedC.getX()) != playerId) {
                        new CardUsesAbilityError(attackerC, attackedC, ErrorTypes.getType(ErrorType.INVALID_HEAL), gameMaster.output);
                        return;
                    }
                } else {
                    if (table.whichPlayer(attackedC.getX()) == playerId) {
                        new CardUsesAbilityError(attackerC, attackedC, ErrorTypes.getType(ErrorType.INVALID_ATTACK), gameMaster.output);
                        return;
                    }
                    if (!table.canAttack(playerId) && !attacked.isTank()) {
                        new CardUsesAbilityError(attackerC, attackedC, ErrorTypes.getType(ErrorType.NOT_TANK), gameMaster.output);
                        return;
                    }
                }
                ((SpecialCard)attacker).useAbility(attacked);
            }

            public void useAttackHero(Coordinates attackerC, int playerId) {
                Minion attacker = table.getCard(attackerC.getX(), attackerC.getY());
                if (attacker.isFrozen()) {
                    new UseAttackHeroError(attackerC, ErrorTypes.getType(ErrorType.FROZEN_ATTACKER), gameMaster.output);
                    return;
                }
                if (attacker.hasAttacked()) {
                    new UseAttackHeroError(attackerC, ErrorTypes.getType(ErrorType.ALREADY_ATTACKED), gameMaster.output);
                    return;
                }
                if (table.canAttack(playerId)) {
                    new UseAttackHeroError(attackerC, ErrorTypes.getType(ErrorType.NOT_TANK), gameMaster.output);
                    return;
                }
                attacker.attack((Attackable) gameMaster.getOtherPlayer(playerId).getHero());
            }

            public void useHeroAbility(int affectedRow, int playerId) {
                Player player = gameMaster.getPlayer(playerId);
                if (player.getMana() < player.getHero().getMana()) {
                    new UseHeroAbilityError(affectedRow, ErrorTypes.getType(ErrorType.NO_MANA_H), gameMaster.output);
                    return;
                }
                if (player.getHero().hasAttacked()) {
                    new UseHeroAbilityError(affectedRow, ErrorTypes.getType(ErrorType.ALREADY_ATTACKED_H), gameMaster.output);
                    return;
                }
                if (player.getHero() instanceof LordRoyce || player.getHero() instanceof EmpressThorina) {
                    if (table.whichPlayer(affectedRow) == playerId) {
                        new UseHeroAbilityError(affectedRow, ErrorTypes.getType(ErrorType.ROW_ENEMY), gameMaster.output);
                        return;
                    }
                } else {
                    if (table.whichPlayer(affectedRow) != playerId) {
                        new UseHeroAbilityError(affectedRow, ErrorTypes.getType(ErrorType.ROW_PLAYER), gameMaster.output);
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
                    new UseEnvironmentCardError(handIdx, affectedRow, ErrorTypes.getType(ErrorType.NOT_ENV), gameMaster.output);
                    return;
                }
                if (player.getMana() < card.getMana()) {
                    new UseEnvironmentCardError(handIdx, affectedRow, ErrorTypes.getType(ErrorType.NO_MANA_E), gameMaster.output);
                    return;
                }
                if (table.whichPlayer(affectedRow) == playerId) {
                    new UseEnvironmentCardError(handIdx, affectedRow, ErrorTypes.getType(ErrorType.ROW_ENEMY), gameMaster.output);
                    return;
                }
                if ((card instanceof HeartHound) && table.getRow(affectedRow).getNrOfCards() < 5) {
                    new UseEnvironmentCardError(handIdx, affectedRow, ErrorTypes.getType(ErrorType.HEART_HOUND), gameMaster.output);
                    return;
                }
                ((Environment)card).useEnvAbility(table.getRow(affectedRow));
                player.setMana(player.getMana() - player.getHero().getMana());
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
