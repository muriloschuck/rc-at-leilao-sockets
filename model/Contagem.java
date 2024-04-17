package model;

import java.io.Serializable;

public class Contagem implements Serializable {
    private final int integer;
    public Contagem(final int integer) {
        this.integer = integer;
    }

    public int getInteger() {
        return this.integer;
    }
}
