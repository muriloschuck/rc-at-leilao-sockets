package model;

import java.io.Serial;
import java.io.Serializable;

public class Lance implements Serializable {
    @Serial
    private static final long serialVersionUID = 46205311964890351L;
    private final int numeroItem;
    private final int valor;

    public Lance(final int numeroItem, final int valor) {
        this.numeroItem = numeroItem;
        this.valor = valor;
    }

    public int getNumeroItem() {
        return numeroItem;
    }

    public int getValor() {
        return valor;
    }
}