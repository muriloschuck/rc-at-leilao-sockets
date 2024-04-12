import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoCliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6071);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Scanner scanner = new Scanner(System.in);

            while (true) {
                ItemLeilao itemAtual = (ItemLeilao) in.readObject();
                System.out.println("Item #" + itemAtual.getNumero() + ": " + itemAtual.getNome() + " - Lance atual: $" + itemAtual.getLanceAtual());

                System.out.print("Digite o valor do lance: $");
                int valorLance = scanner.nextInt();

                out.flush();
                out.writeObject(new Lance(itemAtual.getNumero(), valorLance));
               
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
