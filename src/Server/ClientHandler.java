package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// ClientHandler-klassen. Hanterar inkommande anslutningar från servern för varje ansluten klient och styr läsning och skrivning av meddelanden till klienten.
class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;

    // Konstruktorn, tar in en socket och server som medlemmar och anger in/ut-ström
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run-metoden. Hantera kommunikationen med en enskild klient och skickar inkommande meddelanden till alla andra klienter genom server.broadcast()-metoden.
    public void run() {
        try {
            String nickName = in.readLine();
            server.broadcast("SERVER: " + nickName + " joined the chat.");

            String message;
            while ((message = in.readLine()) != null) {
                server.broadcast(nickName + ": " + message);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metod för att skicka meddelandet
    public void sendMessage(String message) {
        out.println(message);
    }
}

