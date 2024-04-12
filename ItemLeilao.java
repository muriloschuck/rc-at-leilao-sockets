import java.io.Serializable;

public class ItemLeilao implements Serializable {
    private static final long serialVersionUID = 81273012L;
    private int numero;
    private String nome;
    private int lanceInicial;
    private int lanceAtual;

    public ItemLeilao(int numero, String nome, int lanceInicial) {
        this.numero = numero;
        this.nome = nome;
        this.lanceInicial = lanceInicial;
        this.lanceAtual = lanceInicial;
    }
    
    public ItemLeilao(int numero, String nome, int lanceInicial, int lanceAtual) {
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

    public void setLanceAtual(int lanceAtual) {
        this.lanceAtual = lanceAtual;
    }
    
    public ItemLeilao copy() {
    	return new ItemLeilao(this.numero, this.nome, this.lanceInicial, this.lanceAtual);
    }
}
