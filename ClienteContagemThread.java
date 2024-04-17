import model.Contagem;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClienteContagemThread implements Runnable {
    private ObjectInputStream in;

    public ClienteContagemThread(final ObjectInputStream countDownIn) {
        this.in = countDownIn;
    }

    @Override
    public void run() {
        Contagem contagem;
        try {
            while(true) {
                contagem = (Contagem) in.readObject();
                System.out.println("Timeout: " + contagem);
                Thread.sleep(100L);
            }
        } catch (final IOException | InterruptedException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
