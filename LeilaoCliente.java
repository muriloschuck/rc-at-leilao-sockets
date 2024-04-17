import config.Config;
import model.Item;
import model.Jogador;
import model.Lance;
import model.SOCKET_TYPE;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoCliente {
    public static void main(String[] args) {
        try (final Socket dataSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
             final ObjectOutputStream dataOut = new ObjectOutputStream(dataSocket.getOutputStream());
             final ObjectInputStream dataIn = new ObjectInputStream(dataSocket.getInputStream());

             final Socket countDownSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
             final ObjectInputStream countDownIn = new ObjectInputStream(countDownSocket.getInputStream());
             final ObjectOutputStream countDownOut = new ObjectOutputStream(countDownSocket.getOutputStream())) {

            dataOut.flush();
            dataOut.writeObject(SOCKET_TYPE.DATA_SOCKET);
            countDownOut.flush();
            countDownOut.writeObject(SOCKET_TYPE.COUNTDOWN_SOCKET);

            final Scanner scanner = new Scanner(System.in);

            final Thread t = new Thread(new ClienteContagemThread(countDownIn));
            t.start();

            while (true) {
                // #1 Recebe o item atual
                final Item itemAtual = (Item) dataIn.readObject();
                System.out.println("Item #" + itemAtual.getNumero() + ": " + itemAtual.getNome() + " - Lance atual: $" + itemAtual.getLanceAtual());

                // #2 Faz um lance
                System.out.print("Digite o valor do lance: $");
                final int valorLance = scanner.nextInt();

                dataOut.flush();
                dataOut.writeObject(new Lance(itemAtual.getNumero(), valorLance));

                // #3 Espera resposta do lance
                final boolean lanceFoiValido = dataIn.readBoolean();

                if (lanceFoiValido) {
                    System.out.println("Lance deu certo!");
                } else {
                    System.out.println("Lance não foi aceito :(");
                }

            }

        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
