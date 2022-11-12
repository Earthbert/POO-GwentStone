package game;

import deck.Deck;
import fileio.*;
import game.command.Command;
import table.Table;
import utils.CommandTypes;
import utils.ExceptionNoCommands;
import utils.ExceptionWonGame;

import java.util.ArrayList;

public class Game {
    private int manaGain = 1;
    private final ArrayList<ActionsInput> commands;
    private int cmdInx = 0;
    private final int startingPlayer;
    private final int secondPlayer;
    public final Table table;


    public Game(GameInput gameInput, DecksInput playerOneDecks, DecksInput playerTwoDecks) {
        commands = gameInput.getActions();

        startingPlayer = gameInput.getStartGame().getStartingPlayer();
        if (startingPlayer == 1)
            secondPlayer = 2;
        else
            secondPlayer = 1;

        int shuffleSeed = gameInput.getStartGame().getShuffleSeed();

        int playerOneIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        Deck playerOneDeck = new Deck(playerOneDecks.getDecks().get(playerOneIndex), shuffleSeed);
        GameMaster.getInstance().getPlayer(1).startGame(gameInput.getStartGame().getPlayerOneHero(), playerOneDeck);

        int playerTwoIndex = gameInput.getStartGame().getPlayerTwoDeckIdx();
        Deck playerTwoDeck = new Deck(playerTwoDecks.getDecks().get(playerTwoIndex), shuffleSeed);
        GameMaster.getInstance().getPlayer(2).startGame(gameInput.getStartGame().getPlayerOneHero(), playerTwoDeck);

        table = new Table();
    }

    void playTurn(int playerId) throws ExceptionWonGame, ExceptionNoCommands {
        loop: while (cmdInx < commands.size()) {
            ActionsInput command = commands.get(cmdInx);

            switch (CommandTypes.getType(command.getCommand())) {
                case TURNOVER -> {break loop;}
                case ACTION -> Command.Action.handle(command, playerId);
                case OUTPUT -> Command.Output.handle(command, playerId);
            }

            cmdInx++;
            if (cmdInx == commands.size())
                throw new ExceptionNoCommands();
        }
        table.prepareTable(playerId);
    }

    void play() {
        while (true) {
            GameMaster.getInstance().getPlayer(1).preparePlayer(manaGain);
            GameMaster.getInstance().getPlayer(2).preparePlayer(manaGain);

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
}
