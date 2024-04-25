import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

public class ServerSide {
    private static final int MAX_CLIENTS = 2;
    private static int randomNumber;
    private static final Socket[] clients = new Socket[MAX_CLIENTS];
    private static final PrintWriter[] outputs = new PrintWriter[MAX_CLIENTS];
    private static final BufferedReader[] inputs = new BufferedReader[MAX_CLIENTS];
    private static int currentClient = 0; // Index of the current client to make a guess
    private static CountDownLatch latch = new CountDownLatch(MAX_CLIENTS); // To wait for all clients to connect

    public static int waitingClient() { // give the waiting player, not the current one.
        if (currentClient == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void myip() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // Check if the interface is up and is not a loopback interface
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    // Check for names typically associated with Ethernet and WiFi interfaces
                    if (Pattern.matches("^(en|eth|wlan|wifi).*", networkInterface.getName().toLowerCase())) {
                        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            InetAddress inetAddress = inetAddresses.nextElement();
                            // Filter to get only IPv4 addresses
                            if (inetAddress.getAddress().length == 4) {
                                System.out.println("Interface: " + networkInterface.getName());
                                System.out.println("IPv4 Address: " + inetAddress.getHostAddress());
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("[SERVER]: Server started. Waiting for Players to join...");
            myip();

            // Accept two clients
            for (int i = 0; i < MAX_CLIENTS; i++) {
                Socket socket = serverSocket.accept();
                clients[i] = socket;
                outputs[i] = new PrintWriter(socket.getOutputStream(), true);
                inputs[i] = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("[SERVER]: Player " + (i + 1) + " connected.");
                latch.countDown(); // Decrement the count of the latch
            }

            // Wait until all clients are connected
            latch.await();
            // Initialize game
            Random random = new Random();
            randomNumber = random.nextInt(21);
            System.out.println("[SERVER]: Both Players Connected!\n[SERVER]: Game Started. Secret Number generated is "
                    + randomNumber);

            outputs[currentClient]
                    .println("[SERVER]: Hello player 1! the Game started, you must guess the right number!");
            outputs[waitingClient()].println(
                    "[SERVER]: Hello player 2! the Game started, you must guess the right number! your turn next.");

            // Game loop
            while (true) {
                System.out.println("[SERVER]: Waiting for Player " + (currentClient + 1) + " to guess...");

                PrintWriter currentOut = outputs[currentClient];
                currentOut.println("[SERVER]: Your Turn! Guess the right number between 0 and 20.");

                BufferedReader currentIn = inputs[currentClient];
                // Wait for client to guess

                outputs[waitingClient()].println("[SERVER]: waiting for the other Player to guess...");

                String guess = currentIn.readLine();
                if (guess == null) {
                    continue;
                }
                int guessedNumber = Integer.parseInt(guess);
                System.out.println("[SERVER]: Players " + (currentClient + 1) + " guessed " + guessedNumber);

                if (guessedNumber == randomNumber) {
                    currentOut.println("[SERVER]: You won! The game is over.");
                    outputs[waitingClient()].println("[SERVER]: you lost...");
                    break;
                } else if (guessedNumber > randomNumber) {
                    currentOut.println("[SERVER]: Sorry! The number is --lesser-- than [" + guessedNumber + "]");
                } else {
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
    }
}
