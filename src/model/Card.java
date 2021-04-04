package model;

public class Card {
    private String face;
    private String back;
    private String category;

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

    public Card(String face, String back, String category) {
        this.face = face;
        this.back = back;
        this.category = category;
    }
}
