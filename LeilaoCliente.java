import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoCliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6070);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Scanner scanner = new Scanner(System.in);

            while (true) {
                List<ItemLeilao> itens = (List<ItemLeilao>) in.readObject();
                for (ItemLeilao item : itens) {
                    System.out.println("Item #" + item.getNumero() + ": " + item.getNome() + " - Lance atual: $" + item.getLanceAtual());
                }

                System.out.print("Digite o número do item que deseja dar lance (ou -1 para sair): ");
                int numeroItem = scanner.nextInt();
                if (numeroItem == -1) break;

                System.out.print("Digite o valor do lance: $");
                int valorLance = scanner.nextInt();

                out.writeObject(new Lance(numeroItem, valorLance));
                out.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
