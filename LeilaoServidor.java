import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoServidor {
    private static List<ItemLeilao> itens = new ArrayList<>();
    private static int lanceAtual = 0;

    public static void main(String[] args) {
        // Adiciona alguns itens de exemplo
        adicionarItem(new ItemLeilao(1, "Computador", 500));
        adicionarItem(new ItemLeilao(2, "Telefone", 200));

        try (ServerSocket serverSocket = new ServerSocket(6070)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);

                Thread t = new Thread(new Cliente(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void adicionarItem(ItemLeilao item) {
        itens.add(item);
    }

    public static synchronized boolean fazerLance(int numeroItem, int valor) {
        for (ItemLeilao item : itens) {
            if (item.getNumero() == numeroItem) {
                if (valor > item.getLanceAtual()) {
                    item.setLanceAtual(valor);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public static synchronized int getLanceAtual(int numeroItem) {
        for (ItemLeilao item : itens) {
            if (item.getNumero() == numeroItem) {
                return item.getLanceAtual();
            }
        }
        return 0;
    }

    public static synchronized List<ItemLeilao> getItens() {
        return new ArrayList<>(itens);
    }
}