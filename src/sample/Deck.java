package sample;

public class Deck {
    private String deckName;

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public Deck(String deckName) {
        this.deckName = deckName;
    }
}
