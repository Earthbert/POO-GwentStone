package cards;

import fileio.CardInput;

import java.util.ArrayList;

/**
 * The type Card.
 */
public abstract class Card {
    protected final String name;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final int mana;

    public Card(final CardInput card) {
        this.name = card.getName();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.mana = card.getMana();
    }

    protected Card(final Card card) {
        this.name = card.name;
        this.description = card.description;
        this.colors = card.colors;
        this.mana = card.mana;
    }

    /**
     * Creates a deep copy of a card.
     * Uses private / protected copy constructor.
     * @return new Card
     */
    public abstract Card clone();

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final int getMana() {
        return mana;
    }
}
