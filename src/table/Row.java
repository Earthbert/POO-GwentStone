package table;

import cards.Card;
import cards.Minion;
import helpers.UnitPos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Row {
    public static final int MAX_R = 5;

    private final List<Minion> cardsOnRow = new LinkedList<>();

    private final Table table;
    private final int player;
    private final UnitPos type;

    public Row(final int player, final UnitPos type, final Table table) {
        this.player = player;
        this.type = type;
        this.table = table;
    }

    /**
     * Return a deep copy of all cards on the row.
     * It is used at output.
     * @return Deep Copy
     */
    public List<Card> copyOfCards() {
        final List<Card> cards = new ArrayList<>();
        for (final Card card : cardsOnRow) {
            cards.add(card.clone());
        }
        return cards;
    }

    public final List<Minion> getCardsOnRow() {
        return cardsOnRow;
    }

    public final Table getTable() {
        return table;
    }

    public final int getPlayer() {
        return player;
    }

    public final int getNrOfCards() {
        return cardsOnRow.size();
    }

    /**
     * Return true if a tank is placed on the row and false if a tank is not placed.
     * @return tankPlaced
     */
    public final boolean isTankPlaced() {
        for (final Minion m : cardsOnRow) {
            if (m.isTank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Place a card on row if there is space.
     * @param minion Card to be placed
     * @return Return true only if card was placed.
     */
    boolean placeCard(final Minion minion) {
        if (cardsOnRow.size() >= MAX_R) {
            return false;
        }
        cardsOnRow.add(minion);
        minion.setRow(this);
        return true;
    }

    /**
     * Return a minion at a specific index.
     * @param cardIx Card Index
     * @return Returns Card, or null if there is no Card preset.
     */
    Minion getCard(final int cardIx) {
        if (cardIx >= cardsOnRow.size()) {
            return null;
        }
        return cardsOnRow.get(cardIx);
    }

    /**
     * Remove a minion from a row.
     * It is called when a minion is killed or stolen.
     * @param minion which card to be removed.
     */
    public void removeCard(final Minion minion) {
        cardsOnRow.remove(minion);
    }

    /**
     * Prepare row for next turn.
     * Calls prepareCard.
     */
    public void prepareRow() {
        for (final Minion minion : cardsOnRow) {
            minion.prepareCard();
        }
    }
}
