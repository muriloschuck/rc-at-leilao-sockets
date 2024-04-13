import model.Item;
import model.Lance;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoCliente {
    public static void main(String[] args) {
        try (final Socket socket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            final Scanner scanner = new Scanner(System.in);

            while (true) {
                final Item itemAtual = (Item) in.readObject();
                System.out.println("Item #" + itemAtual.getNumero() + ": " + itemAtual.getNome() + " - Lance atual: $" + itemAtual.getLanceAtual());

                System.out.print("Digite o valor do lance: $");
                final int valorLance = scanner.nextInt();

                out.flush();
                out.writeObject(new Lance(itemAtual.getNumero(), valorLance));
            }

        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
