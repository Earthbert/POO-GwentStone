

# Homework POO  - GwentStone

### Daraban Albert-Timotei 324CA

GwentStone is a combination of Gwent and HearthStone, being card game where you place cards on table on different rows and the goal is to kill the hero card of the enemy player.

## Game Mechanics

* Card Types:
  * Minion cards, are cards that can be placed on the table. They are of two types:
    * Standard Cards, are cards that can just attack. 
    * Special Cards, are cards that have abilities and also can attack.
  * Environment Cards, are cards one time use cards, they affect a whole row.
  * Hero Cards, one per player, when it dies the game ends. They also have abilities that affect a whole row.
* Players:
  * have their own mana. It costs mana to:
    * Place a card.
    * Use Environment card.
    * Use hero ability.
  * take turns making their actions.

## Implementation

I will go through packages and java files from src in a semi-logical order and present and describe how they relate.

* cards
  * Card (abstract class) contains:
    * fields that all types of cards have.
    * clone (abstract method) which will be used for creating a deep copy of any subclass.
    * deep copy is needed because output is written in file at the end of all games and some fields of a card could be changed.
    * protected copy constructor called by subclass's copy constructor called by clone implementation.
  * Minion (subclass of Card):
    * contains fields that a minion should have.
    * contains a reference to the Row object in this the card is placed. Having this field is useful for removing card from row when it dies.
    * contains attack method which can attack Attackable cards.
    * implements Attackable interface.
    * Standard Cards (cards that don't have abilities) are instances of this class.
  * Attackable (interface):
    * contains takeDamage method, which is called by attack method of Minion.
  * specialcards:
    * SpecialCard (interface) contains useAbility.
    * all other classes from this package extend Minion class and implement SpecialCard interface.
  * environmentcards:
    * Environment (interface) contains useEnvAbility method.
    * all other classes from this package extend Card class and implement Environment interface.
  * heroes:
    * Hero (abstract class): 
      * fields that a hero should have.
      * useAbility (abstract method).
      * implements Attackable interface.
    * all other classes from this package extend Hero class.

* deck
  * Deck (class) contains:
    * two lists of cards
      * one for cards on deck
      * one for cards in hand
    * constructor, which created card and puts them in deck.
    * method to get / remove cards from the two lists.
    * prepareDeck method which moves a card from deck to hand.

* players
  * Player (class) contains:
    * fields:
      * mana, how much mana a player has at a given time.
      * gamesWon
      * hero, what Hero object the player has.
      * deck, Deck object.
    * startGame method called at the start of each game, which set field and creates hero card.
    * preparePlayer
      * called at the start of a round.
      * adds mana.
      * calls prepare deck.

* table
  * Row (class) contains:
    * a list of cards placed on row.
    * a reference to table it belong to.
    * player index which the row belong to.
    * self-explanatory method.
  * Table (class) contains:
    * const array of 4 Row object.
    * self-explanatory method.

* game
  * Game (class) this class represents a single game.
    * fields:
      * reference to GameMaster object that created this class, to be able to access field from it.
      * manaGain how much mana to give at the start of a round.
      * startingPlayer / secondPlayer which player start / goes second in a round.
      * ActionInput array and cmdIdx will be used to get commands.
      * Table object.
      * Command object, will be used to handle commands.
    * methods:
      * play is called by gameMaster and in it the whole game runs.
        * a round is an iteration in a loop inside this method.
      * playTurn is called for each player by play() every round.
    * Command (inner-Class)
      * used to better separate commands.
      * is implemented as inner-class because we need to have access to Game fields.
      * is divided in two inner-classes:
        * Action - action type commands
          * method from this class will output only if an error occurs.
        * Output - output type commands
        * both of the above classes have JsonCreator inner-class which is used to create ObjectNodes for output.

  * GameMaster (class)
    * fields:
      * totalGames
      * ArrayNode object, used to output.
      * array of two Player objects
        * Players are in this class because they remain between games.
    * entry (method) is called by main, and it goes through game inputs and creates new games. 

* helpers
  * Errors (class) contains constant String to make it easier to use throughout the program.
  * UnitPos (enum) it is used to know on which type of row a card should be placed on.
    * RANGED for back row.
    * CLOSE for front row.
  * UnitType (enum) what type a card it is.
  * UnitProp (Class)
    * Proprieties (record) contains useful information about a card and what class it is.
    * map which associates card name with proprieties.
    * methods to extract the information.
    * getCtor return the constructor of a card by its name which is used to initialize an object without having to use switches.
  * CommandType (enum), I chose to divide commands in 3 categories.
    * TURNOVER commands that end turn. 
    * ACTION commands that change the state of the game.
    * OUTPUT commands from debugging and statistics.
    * INVALID if commands doesn't exist.
  * CommandTypes (class)
    * map which associated Strings with CommandType.
    * if it doesn't find a command in map it return a CommandType.INVALID.
  * Exceptions:
    * ExceptionInvalidCard, thrown when the input contains a card whose name isn't in UnitProp.
      * if it is thrown no games are played anymore, and it prints in stdout name of the invalid card.
    * ExceptionWonGame thrown when a hero's health goes below 0
      * when cough updates statistics
      * stops mana gain
      * continues taking output commands. if an action command is received it print an error.
    * ExceptionNoCommands, thrown when there are no more commands to execute. When cough it actually end the game.
