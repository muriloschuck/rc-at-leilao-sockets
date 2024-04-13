import model.Item;
import model.Lance;

import java.io.*;
import java.net.*;
public class ClienteHandler implements Runnable {
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClienteHandler(final Socket socket) {
        this.clientSocket = socket;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                final Item itemAtual = LeilaoServidor.getItemAtual();
                
                out.writeObject(itemAtual.copy());
                out.flush();

                // todo: timeout
                
                System.out.println("Aguardando lances...");
                final Lance lanceFeito = (Lance) in.readObject();
                
                boolean lanceAceito = LeilaoServidor.fazerLance(lanceFeito);

                if (lanceAceito) {
                    System.out.println("Novo lance recebido: $" + lanceFeito.getValor());
                } else {
                    System.out.println("Lance rejeitado. O lance atual é maior ou igual.");
                }
            }
        } catch (final IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
