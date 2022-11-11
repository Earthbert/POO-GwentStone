package cards;

import fileio.CardInput;

import java.util.ArrayList;

public abstract class Card {
    protected String name;
    protected final String description;
    protected final ArrayList<String> colors;
    protected int mana;

    private boolean usedCard = false;

    public Card(CardInput card) {
        this.name = card.getName();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.mana = card.getMana();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

}
