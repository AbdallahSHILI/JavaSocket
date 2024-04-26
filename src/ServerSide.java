import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import java.net.InetAddress;

public class ServerSide {
    private static int randomNumber;
    private static final Socket[] clients = new Socket[2];
    private static final PrintWriter[] outputs = new PrintWriter[2];
    private static final BufferedReader[] inputs = new BufferedReader[2];
    private static int currentClient = 0; // Index of the current client to make a guess
    private static CountDownLatch latch = new CountDownLatch(2); // To wait for all clients to connect

    public static int waitingClient() { // give the waiting player, not the current one.
        if (currentClient == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // This method is called just to show the current IP address of the server, for
    // easier client setup on dynamic IP.
    // this method imported.

    public static void main(String[] args) {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("[SERVER]: Server started. Waiting for Players to join...");

            // Print the IP address of the server
            try {
                InetAddress address = InetAddress.getLocalHost();
                System.out.println(address);
            } catch (UnknownHostException e) {
                System.out.println("Could not find this computer's address.");
            }

            // Accept two clients
            for (int i = 0; i < 2; i++) {
                Socket socket = serverSocket.accept();
                clients[i] = socket; // Store the socket in the array.
                // Create a print writer for each socket's output stream
                outputs[i] = new PrintWriter(socket.getOutputStream(), true);
                // Create a buffered reader for each socket's input stream.
                inputs[i] = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("[SERVER]: Player " + (i + 1) + " connected.");
                latch.countDown(); // Decrement the count of the latch
            }

            // Wait until all clients are connected
            latch.await(); // if = 0

            // 2 players joined

            // Initialize secret number.
            Random random = new Random();
            randomNumber = random.nextInt(21);

            System.out.println("[SERVER]: Both Players Connected!\n[SERVER]: Gam7e Started. Secret Number generated is "
                    + randomNumber);
            // Send welcome messages to both players (currentClient is the first connected
            // player).
            outputs[currentClient]
                    .println("[SERVER]: Hello player 1! the Game started, you must guess the right number!");
            outputs[waitingClient()].println(
                    "[SERVER]: Hello player 2! the Game started, you must guess the right number! your turn is next.");

            // Game loop
            while (true) {
                System.out.println("[SERVER]: Waiting for Player " + (currentClient + 1) + " to guess...");
                PrintWriter currentOut = outputs[currentClient];
                currentOut.println("[SERVER]: Your Turn! Guess the right number between 0 and 20.");
                outputs[waitingClient()].println("[SERVER]: Please wait the other Player's turn...");

                BufferedReader currentIn = inputs[currentClient];
                // Wait for client to guess

                String guess = currentIn.readLine(); // client input to string
                if (guess == null) {
                    continue; // retry if empty.
                }

                int guessedNumber = Integer.parseInt(guess); // Convert input string to integer

                System.out.println("[SERVER]: Players " + (currentClient + 1) + " guessed " + guessedNumber);

                // Check if Player's guess is correct
                if (guessedNumber == randomNumber) {
                    System.out.println("[SERVER]: Player " + (currentClient + 1) + " guessed the right number.");
                    currentOut.println("[SERVER]: You won! The game is over. replay? (yes/no)");
                    outputs[waitingClient()].println("[SERVER]: you lost, other player guessed it! [" + guessedNumber
                            + "]. replay? (yes/no)");

                    String vote1 = currentIn.readLine().toLowerCase();
                    String vote2 = inputs[waitingClient()].readLine().toLowerCase();
                    if (vote1.equals("yes") && vote2.equals("yes")) {
                        randomNumber = random.nextInt(21);
                        System.out.println("[SERVER]: The game is restarted, the new number is " + randomNumber);
                        currentOut.println("[SERVER]: The game is restarted");
                        outputs[waitingClient()].println("[SERVER]: The game is restarted");
                        continue;
                    } else {
                        break;
                    }
                } else if (guessedNumber > randomNumber) {
                    currentOut.println("[SERVER]: Sorry! The number is --lesser-- than [" + guessedNumber + "]");
                } else { // guessedNumber < randomNumber
                    currentOut.println("[SERVER]: Sorry! The number is ++greater++ than [" + guessedNumber + "]");
                }

                // Switch to the next client
                currentClient = waitingClient(); // Switch to the next client
            }

            // Close resources
            for (Socket socket : clients) {
                socket.close();
            }
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("[SERVER]: Error: " + e.getMessage());
        }
        System.out.println("[SERVER]: Server stopped.");
    }
}
