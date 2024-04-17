import java.io.IOException;
import java.io.ObjectOutputStream;

class ServidorContagemThread implements Runnable {
    private final ObjectOutputStream out;

    public ServidorContagemThread(final ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                out.writeObject(LeilaoServidor.getContador());
                out.flush();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}