import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ServerSide {
    private static final int MAX_CLIENTS = 2;
    private static int randomNumber;
    private static final Socket[] clients = new Socket[MAX_CLIENTS];
    private static final PrintWriter[] outputs = new PrintWriter[MAX_CLIENTS];
    private static final BufferedReader[] inputs = new BufferedReader[MAX_CLIENTS];
    private static int currentClient = 0; // Index of the current client to make a guess
    private static CountDownLatch latch = new CountDownLatch(MAX_CLIENTS); // To wait for all clients to connect

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("[SERVER]: Server started. Waiting for Clients to join...");

            // Accept exactly two clients
            for (int i = 0; i < MAX_CLIENTS; i++) {
                Socket socket = serverSocket.accept();
                clients[i] = socket;
                outputs[i] = new PrintWriter(socket.getOutputStream(), true);
                inputs[i] = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("[SERVER]: Client " + (i + 1) + " connected.");
                latch.countDown(); // Decrement the count of the latch
            }

            // Wait until all clients are connected
            latch.await();

            // Initialize game
            Random random = new Random();
            randomNumber = random.nextInt(21);
            System.out.println("[SERVER]: Number generated is " + randomNumber);

            for (PrintWriter out : outputs) {
                out.println("[SERVER]: Game started. Guess a number between 0 and 20.");
            }

            // Game loop
            boolean gameRunning = true;
            while (gameRunning) {
                PrintWriter currentOut = outputs[currentClient];
                BufferedReader currentIn = inputs[currentClient];

                String guess = currentIn.readLine();
                if (guess == null) {
                    continue;
                }
                int guessedNumber = Integer.parseInt(guess);
                System.out.println("[SERVER]: Client " + (currentClient + 1) + " guessed " + guessedNumber);

                if (guessedNumber == randomNumber) {
                    currentOut.println("[SERVER]: You won! The game is over.");
                    gameRunning = false;
                } else if (guessedNumber > randomNumber) {
                    currentOut.println("[SERVER]: The number is lesser than " + guessedNumber);
                } else {
                    currentOut.println("[SERVER]: The number is greater than " + guessedNumber);
                }

                // Switch to the next client
                currentClient = (currentClient + 1) % MAX_CLIENTS;
            }

            // Close resources
            for (Socket socket : clients) {
                socket.close();
            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
