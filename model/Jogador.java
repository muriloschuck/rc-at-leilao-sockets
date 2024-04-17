package model;

import java.io.Serial;
import java.io.Serializable;

public class Jogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 46205311964890351L;
    private int id;
    private String nome;

    public Jogador(final String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
