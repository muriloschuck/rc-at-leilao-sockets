import model.Jogador;
import utils.Pair;
import config.Config;
import model.Item;
import model.Lance;
import model.RespostaLance;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoServidor {
    private final static List<Item> itens = new ArrayList<>();
    private final static List<Pair<Item, Jogador>> listaVendas = new ArrayList<>();
    private static Item itemAtual;
    private static int numeroItemAtual;
    private static int qtdConexoes = 0;
    private static volatile int contador = 21;
    public static void main(final String[] args) {
        // Lista de itens do Leilão
        itens.add(new Item(1, "Computador", 500));
        itens.add(new Item(2, "Mouse", 50));
        itens.add(new Item(3, "Telefone", 200));
        itens.add(new Item(4, "Teclado", 150));
        listaVendas.add(0, new Pair<Item, Jogador>(new Item(1, "Computador", 500), new Jogador("ninguem")));
        listaVendas.add(1, new Pair<Item, Jogador>(new Item(2, "Mouse", 50), new Jogador("ninguem")));
        listaVendas.add(2, new Pair<Item, Jogador>(new Item(3, "Telefone", 200), new Jogador("ninguem")));
        listaVendas.add(3, new Pair<Item, Jogador>(new Item(4, "Teclado", 150), new Jogador("ninguem")));

        numeroItemAtual = 0;
        itemAtual = itens.get(numeroItemAtual);

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

    public static synchronized RespostaLance fazerLance(final Lance lance, final Jogador jogador) {
        if (contador <= 0) {
            if (numeroItemAtual < itens.size() - 1) {
                resetaContador();
                numeroItemAtual++;
                itemAtual = itens.get(numeroItemAtual);
            } else {
                numeroItemAtual++;
                System.out.println("\n=========================================");
                listaVendas.forEach(
                        vendaLista -> System.out.printf("Item [%s] vendido por [%s] para [%s]%n",
                                vendaLista.getLeft().getNome(), vendaLista.getLeft().getLanceAtual(), vendaLista.getRight().getNome()));

                return new RespostaLance(false, "Fim dos itens disponiveis", listaVendas);
            }
        }

        if (lance.getNumeroItem() != itemAtual.getNumero()) {
            return new RespostaLance(false, "O item já foi vendido");
        }

        if (itemAtual.getLanceAtual() >= lance.getValor()) {
    		return new RespostaLance(false, "Valor abaixo do lance atual");
    	}

        listaVendas.set(numeroItemAtual, new Pair<>(itemAtual, jogador));
        itemAtual.setLanceAtual(lance.getValor());
        resetaContador();
        return new RespostaLance(true);
    }

    public static void comecaContadorAsync() {
        final Thread asyncCounterThread = new Thread(() -> {
            try {
                while (true) {
                    contador--;
                    Thread.sleep(1000);
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

    private static void resetaContador() {
        contador = 21;
    }
    
}