import config.Config;
import model.Item;
import model.Jogador;
import model.Lance;
import model.RespostaLance;
import utils.Pair;

import java.io.*;
import java.net.*;
import java.util.*;

public class LeilaoCliente {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);
        System.out.println("> Qual seu nome de usuario?");
        final String nomeUsuario = scanner.nextLine();
        final Jogador jogadorUsuario = new Jogador(nomeUsuario);

        try (final Socket dataSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
             final ObjectOutputStream dataOut = new ObjectOutputStream(dataSocket.getOutputStream());
             final ObjectInputStream dataIn = new ObjectInputStream(dataSocket.getInputStream())) {

            while (true) {
                // #1 Recebe o item atual
                final Item itemAtual = (Item) dataIn.readObject();
                System.out.println("=========================================");
                System.out.println("Item #" + itemAtual.getNumero() + ": " + itemAtual.getNome() + " - Lance atual: " +
                        Config.YELLOW + "$" + itemAtual.getLanceAtual() + Config.RESET);

                // #2 Recebe o tempo atual do contador
                final int contagem = dataIn.readInt();
                System.out.println("Tempo restante: " + Config.YELLOW + contagem + Config.RESET + " segundos");
                System.out.println("=========================================");

                // #3 Envia lance, jogador
                System.out.print("> Digite o valor do lance: $");
                final int valorLance = scanner.nextInt();

                dataOut.flush();
                dataOut.writeObject(new Lance(itemAtual.getNumero(), valorLance));
                dataOut.flush();
                dataOut.writeObject(jogadorUsuario);

                // #4 Espera resposta do lance
                final RespostaLance resultadoLance = (RespostaLance) dataIn.readObject();

                if (resultadoLance.ehValido()) {
                    System.out.println(Config.GREEN + "Lance aceito!" + Config.RESET);
                } else if (resultadoLance.getMotivo().equals("Fim dos itens disponiveis")) {
                    // caso seja o fim da lista, receba a lista de itens vendidos
                    System.out.println(Config.YELLOW + "Fim dos itens disponiveis" + Config.RESET);
                    final List<Pair<Item, Jogador>> listaFinal = (List<Pair<Item, Jogador>>) dataIn.readObject();
                    listaFinal.forEach(
                            vendaLista -> System.out.printf("Item [%s] vendido por [%s] para [%s]%n",
                                    vendaLista.getLeft().getNome(), vendaLista.getLeft().getLanceAtual(), vendaLista.getRight().getNome()));
                } else {
                    System.out.println(Config.RED + "Lance rejeitado." + Config.RESET + " Motivo: [" + resultadoLance.getMotivo() + "]");
                }

            }

        } catch (final IOException | ClassNotFoundException e) {
            if (e instanceof SocketException || e instanceof java.io.EOFException) {
                System.out.println("> Socket closed.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
