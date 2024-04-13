import model.Item;
import model.Lance;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoServidor {
    private final static List<Item> itens = new ArrayList<>();
    private static Item itemAtual;
    public static void main(final String[] args) {
        // Lista de itens do Leilão
        itens.add(new Item(1, "Computador", 500));
        itens.add(new Item(2, "Mouse", 50));
        itens.add(new Item( 3, "Telefone", 200));

        itemAtual = itens.get(0);

        try (ServerSocket serverSocket = new ServerSocket(Config.SERVER_PORT)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);

                Thread t = new Thread(new ClienteHandler(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean fazerLance(Lance lance) {
    	if (itemAtual.getLanceAtual() >= lance.getValor()) {
    		return false;
    	}
    	
    	itemAtual.setLanceAtual(lance.getValor());
    	return true;
    }

    public static synchronized Item getItemAtual() {
    	return itemAtual;
    }
    
}