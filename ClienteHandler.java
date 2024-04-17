import model.Item;
import model.Lance;
import model.SOCKET_TYPE;

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
            // Espera a conexao se identificar, sempre recebe o jogador e em seguida se é o socket principal ou de contagem
            final SOCKET_TYPE socketType = (SOCKET_TYPE) in.readObject();

            if (socketType == SOCKET_TYPE.COUNTDOWN_SOCKET) {
                final Thread t = new Thread(new ServidorContagemThread(this.out));
                t.start();
                return;
            }

            System.out.println("works until here");

            while (true) {
                // #1 Envia o item atual para o cliente
                final Item itemAtual = LeilaoServidor.getItemAtual();
                this.out.writeObject(itemAtual.copy());
                this.out.flush();

                // #2 Espera por Lance do cliente
                System.out.println("Aguardando lances...");
                final Lance lanceRecebido = (Lance) in.readObject();

                boolean lanceAceito = LeilaoServidor.fazerLance(lanceRecebido);

                if (lanceAceito) {
                    System.out.println("Novo lance recebido: $" + lanceRecebido.getValor());
                } else {
                    System.out.println("Lance rejeitado. O lance atual é maior ou igual.");
                }

                // #3 Envia resultado do lance para o cliente
                out.writeBoolean(lanceAceito);
                out.flush();
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

    private void comecaContagem() {
        final Thread contagemThread = new Thread(() -> {
        });

        contagemThread.start();
    }
}
