package model;

import java.util.ArrayList;
import java.util.List;

public class Farmer {

    private final String name;
    private final List<Field> ownedFields;

    public Farmer(String name) {
        this.name = name;
        this.ownedFields = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Field> getOwnedFields() {
        return ownedFields;
    }

    public void assignField(Field field) {
        ownedFields.add(field);
    }
}
