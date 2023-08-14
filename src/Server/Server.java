package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

// Serverklassen. Lyssnar efter inkommande anslutningar från klienter.
public class Server {
    private List<ClientHandler> clients;
    private ServerSocket serverSocket;

    // Server-konstruktorn tar portNr som inparameter
    public Server(int port) {
        try {
            // ServerSocket skapas för att lyssna efter klienter som vill ansluta
            serverSocket = new ServerSocket(port);
            // arraylista som håller alla ClientHandler-objekt vi instansierar
            clients = new ArrayList<>();
            System.out.println("Server startad på port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metod för att hålla igång servern
    public void start() {
        while (true) {
            try {
                // .accept(); lyssnar efter en anslutning till denna socket och accepterar inkommande anslutning.
                // Metoden blockar den egna tråden eftersom den väntar på anrop
                Socket socket = serverSocket.accept();
                System.out.println("Ny klient ansluten: " + socket);
                // skapa ett ClientHandler-objekt från ClientHandler-klassen...
                ClientHandler client = new ClientHandler(socket, this);
                //...och addera det till listan...
                clients.add(client);
                //...och starta tråden i ClientHandler-klassen
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Metod för att skicka ut meddelanden till alla klienter
    public void broadcast(String message) {
        // För varje ClientHandler-objekt i listan, skicka ut meddelandet
        for (ClientHandler client : clients) {
            client.sendMessage(message); //anropar ClientHandlder-klassens send-metod på varje klient i listan
        }
    }

    // Starta server-programmet på port 1234
    public static void main(String[] args) {
        Server s = new Server(1234);
        s.start();
    }
}
