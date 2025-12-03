package model;

public class Field {

    private final String id;
    private Farmer owner;

    public Field(String id) {
        this.id = id;
        this.owner = null;
    }

    public String getId() {
        return id;
    }

    public Farmer getOwner() {
        return owner;
    }

    public void setOwner(Farmer owner) {
        this.owner = owner;
    }
}
