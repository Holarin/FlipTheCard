package model;

import java.util.Objects;

public class Card {
    private String face;
    private String back;
    private String category;
    private int rating;

    public Card(String face, String back, String category, int rating) {
        this.face = face;
        this.back = back;
        this.category = category;
        this.rating = rating;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(face, card.face) && Objects.equals(back, card.back) && Objects.equals(category, card.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(face, back, category);
    }
}
