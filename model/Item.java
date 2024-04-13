package model;

import java.io.Serial;
import java.io.Serializable;

public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 4808821991004546880L;
    private final int numero;
    private final String nome;
    private final int lanceInicial;
    private int lanceAtual;

    public Item(final int numero, final String nome, final int lanceInicial) {
        this.numero = numero;
        this.nome = nome;
        this.lanceInicial = lanceInicial;
        this.lanceAtual = lanceInicial;
    }
    
    public Item(final int numero, final String nome, final int lanceInicial, final int lanceAtual) {
        this.numero = numero;
        this.nome = nome;
        this.lanceInicial = lanceInicial;
        this.lanceAtual = lanceAtual;
    }

    public int getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public int getLanceInicial() {
        return lanceInicial;
    }

    public int getLanceAtual() {
        return lanceAtual;
    }

    public void setLanceAtual(final int lanceAtual) {
        this.lanceAtual = lanceAtual;
    }
    
    public Item copy() {
    	return new Item(this.numero, this.nome, this.lanceInicial, this.lanceAtual);
    }
}
