import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSide {
    private static final int MAX_CLIENTS = 2;
    private static AtomicInteger connectedClients = new AtomicInteger(0);
    private static int randomNumber;
    private static boolean multiplayerMode = false;
    private static final Object gameStartLock = new Object();

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(3000)) {
            System.out.println("[SERVER]: Server started. Waiting for Clients to join...");

            ExecutorService executorService = Executors.newFixedThreadPool(MAX_CLIENTS);

            while (connectedClients.get() < MAX_CLIENTS) {
                Socket soc = ss.accept();
                executorService.execute(new ClientHandler(soc));
            }

            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println("[SERVER]: " + ip + " connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Increment connected clients
                int clientNumber = connectedClients.incrementAndGet();

                if (clientNumber == 1) {
                    out.println("[SERVER]: Do you want to play in multiplayer mode? (yes/no)");
                    String response = in.readLine();
                    if ("yes".equalsIgnoreCase(response)) {
                        multiplayerMode = true;
                    } else {
                        multiplayerMode = false;
                    }
                }

                synchronized (gameStartLock) {
                    if (multiplayerMode && clientNumber == 1) {
                        out.println("[SERVER]: Waiting for another player to join...");
                        while (connectedClients.get() < MAX_CLIENTS) {
                            gameStartLock.wait(); // Wait until all clients are connected
                        }
                    }
                    if (clientNumber == MAX_CLIENTS && multiplayerMode) {
                        gameStartLock.notifyAll(); // Notify all waiting threads
                    }
                }

                Random random = new Random();
                randomNumber = random.nextInt(21);
                out.println("[SERVER]: Game started. Guess a number between 0 and 20.");
                System.out.println("[SERVER]: Number generated is " + randomNumber);

                while (true) {
                    String inputString = in.readLine();
                    if (inputString == null) {
                        continue;
                    }
                    int guessedNumber = Integer.parseInt(inputString);
                    System.out.println("[SERVER]: Received " + inputString + " from " + ip);

                    if (guessedNumber == randomNumber) {
                        out.println("[SERVER]: You won! Game over. Replay again? (yes/no)");
                        String userReplay = in.readLine();
                        if ("yes".equalsIgnoreCase(userReplay)) {
                            randomNumber = random.nextInt(21);
                            System.out.println("[SERVER]: Number generated is " + randomNumber);
                            out.println("[SERVER]: Game started. Guess a number between 0 and 20.");
                            continue;
                        } else {
                            System.out.println("[SERVER]: " + ip + " refused replay request, shutting down...");
                            break;
                        }
                    } else if (guessedNumber > randomNumber) {
                        out.println("[SERVER]: The number is lesser than " + guessedNumber);
                    } else {
                        out.println("[SERVER]: The number is greater than " + guessedNumber);
                    }
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
