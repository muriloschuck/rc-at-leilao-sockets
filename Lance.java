import java.io.Serializable;

public class Lance implements Serializable {
    private static final long serialVersionUID = 1L;
    private int numeroItem;
    private int valor;

    public Lance(int numeroItem, int valor) {
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