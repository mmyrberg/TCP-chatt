package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

// KlientKlassen. Hanterar klientens anslutning till servern och kommunicerar med servern.
public class Client extends JFrame implements ActionListener {
    private JTextField messageField;
    private JTextArea chatArea;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JButton logoutButton;
    private PrintWriter out;
    private BufferedReader in;

    // Konstruktorn, tar in serverns ip- och portnummer för anslutning och sätter upp in/ut-ström samt det grafiska gränssnittet
    public Client(String host, int port) {
        try {
            // Socket-uppkoppling mot servern
            Socket socket = new Socket(host, port);
            // Sätter upp in- och utströmmar för att kunna läsa och skriva
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Börja med att fråga användaren om nickname
            String nickName = JOptionPane.showInputDialog("Enter your nick name:");
            out.println(nickName);

            // Ställ in programfönstret
            setTitle("Chat Client");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(250, 250);

            // Ange grafiska komponenter:
            // TextArea (ska inte gå att skriva i)
            chatArea = new JTextArea();
            chatArea.setEditable(false);

            // Scroller
            scrollPane = new JScrollPane(chatArea);
            add(scrollPane, BorderLayout.CENTER);

            // Panel för att lägga skrivfält
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            add(panel, BorderLayout.SOUTH);
            messageField = new JTextField();
            panel.add(messageField, BorderLayout.CENTER);

            // Logga ut knapp för att stänga ner chatten
            logoutButton = new JButton("Koppla ner");
            add(logoutButton, BorderLayout.NORTH);

            // Lägg en actionLyssnare på skrivFältet, när användare trycker "ENTER" ska meddelandet skickas
            messageField.addActionListener(this);
            logoutButton.addActionListener(this);

            // Gör all grafik synlig
            setVisible(true);

            // Ta emot meddelanden
            receiveMessages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // När användaren trycker Enter ska meddelandet skickas
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == messageField) {
            // Hämta texten från meddelandefältet
            String message = messageField.getText();
            // Skicka iväg meddelandet i utströmmen
            out.println(message);
            // Töm skrivfältet efter varje utskick
            messageField.setText("");

        // När användare trycker på "Koppla ner"
        } else if (e.getSource() == logoutButton) {
            out.println("Lämnade chatten.");
            System.exit(0);
        }
    }

    // Ta emot meddelanden från in-strömmen
    public void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Skapa upp client-objekt för att köra programmet
        Client c = new Client("localhost", 1234);
    }
}

