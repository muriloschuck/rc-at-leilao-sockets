import model.Item;
import model.Jogador;
import model.Lance;
import model.RespostaLance;
import utils.Pair;

import java.io.*;
import java.net.*;
import java.util.List;

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
                // #1 Envia o item atual para o cliente
                final Item itemAtual = LeilaoServidor.getItemAtual();
                this.out.writeObject(itemAtual.copy());
                this.out.flush();

                // #2 Envia a contagem atual
                final int contagemAtual = LeilaoServidor.getContador();
                this.out.writeInt(contagemAtual);
                this.out.flush();

                // #3 Espera por Lance do cliente
                System.out.println("Aguardando lances...");
                final Lance lanceRecebido = (Lance) in.readObject();
                final Jogador jogador = (Jogador) in.readObject();

                final RespostaLance respostaLance = LeilaoServidor.fazerLance(lanceRecebido, jogador);

                // #4 Envia resultado do lance para o cliente
                out.writeObject(respostaLance);
                out.flush();

                if (respostaLance.ehValido()) {
                    System.out.println("Novo lance recebido: $" + lanceRecebido.getValor());
                } else if (respostaLance.getMotivo().equals("Fim dos itens disponiveis")) {
                    // caso seja o fim da lista, envie a lista de itens vendidos
                    out.writeObject(respostaLance.getApendice());
                    out.flush();
                    System.out.println("Desconectando " + clientSocket);
                    clientSocket.close();
                } else {
                    System.out.println("Lance rejeitado. Motivo: [" + respostaLance.getMotivo() + "]");
                }

            }
        } catch (final IOException | ClassNotFoundException ex) {
            if (ex instanceof SocketException) {
                System.out.println("> Socket closed.");
            } else {
                ex.printStackTrace();
            }
        }
    }
}
