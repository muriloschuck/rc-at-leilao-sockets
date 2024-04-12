import java.io.*;
import java.net.*;
import java.util.*;



public class Cliente implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Cliente(Socket socket) {
        this.clientSocket = socket;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ItemLeilao itemAtual = LeilaoServidor.getItemAtual();
                
                out.flush();
                out.writeObject(itemAtual.copy());
                
                // todo: timeout
                
                System.out.println("Aguardando lances...");
                Lance lanceFeito = (Lance) in.readObject();
                
                boolean lanceAceito = LeilaoServidor.fazerLance(lanceFeito);

                if (lanceAceito) {
                    System.out.println("Novo lance recebido: $" + lanceFeito.getValor());
                } else {
                    System.out.println("Lance rejeitado. O lance atual é maior ou igual.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
