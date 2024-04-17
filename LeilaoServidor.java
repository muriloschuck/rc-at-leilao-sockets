import config.Config;
import model.Item;
import model.Jogador;
import model.Lance;
import model.SOCKET_TYPE;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoServidor {
    private final static List<Item> itens = new ArrayList<>();
    private static Item itemAtual;
    private static int qtdConexoes = 0;
    private static volatile int contador = 15;
    public static void main(final String[] args) {
        // Lista de itens do Leilão
        itens.add(new Item(1, "Computador", 500));
        itens.add(new Item(2, "Mouse", 50));
        itens.add(new Item(3, "Telefone", 200));
        itens.add(new Item( 4, "Teclado", 150));

        itemAtual = itens.get(0);

        try (final ServerSocket serverSocket = new ServerSocket(Config.SERVER_PORT)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket);

                final Thread t = new Thread(new ClienteHandler(clientSocket));
                t.start();

                if (qtdConexoes == 0) {
                    comecaContadorAsync();
                }
                qtdConexoes++;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean fazerLance(final Lance lance) {
    	if (itemAtual.getLanceAtual() >= lance.getValor()) {
    		return false;
    	}
    	
    	itemAtual.setLanceAtual(lance.getValor());
    	return true;
    }

    public static void comecaContadorAsync() {
        final Thread asyncCounterThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    contador--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        asyncCounterThread.start();
    }

    public static synchronized int getContador() {
        return contador;
    }

    public static synchronized Item getItemAtual() {
    	return itemAtual;
    }
    
}