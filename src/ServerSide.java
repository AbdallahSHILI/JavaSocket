import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSide {
    private static final int MAX_CLIENTS = 2;
    private static int connectedClients = 0;
    private static int randomNumber;
    private static boolean multiplayerMode = false;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("[SERVER]: Server started. Waiting for Clients to join...");

            ExecutorService executorService = Executors.newFixedThreadPool(MAX_CLIENTS);

            while (true) {
                Socket soc = ss.accept();
                connectedClients++;

                executorService.execute(new ClientHandler(soc));

                if (connectedClients == MAX_CLIENTS || !multiplayerMode) {
                    break;
                }
            }

            executorService.shutdown();
            ss.close();
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

                if (connectedClients == 1) {
                    out.println("[SERVER]: Do you want to play in multiplayer mode? (yes/no)");
                    String response = in.readLine();

                    Random random = new Random();
                    randomNumber = random.nextInt(21);
                    
                    if (response.equalsIgnoreCase("yes")) {
                        multiplayerMode = true;
                        out.println("[SERVER]: Waiting for another player to join...");
                        while (connectedClients < MAX_CLIENTS) {
                            Thread.sleep(1000); // Wait for 1 second before checking again
                        }
                        out.println("[SERVER]: Another player has joined. Game started. Guess a number between 0 and 20.");
                    } else {
                        multiplayerMode = false;
                        
                        out.println("[SERVER]: Game started. Guess a number between 0 and 20.");
                    }
                } else {
                    out.println("[SERVER]: Game started. Guess a number between 0 and 20.");
                }
                System.out.println("[SERVER]: Number generated is " + randomNumber);
                while (true) {
                    String inputString = in.readLine();
                    if (inputString == null) {
                        continue;
                    }
                    int guessedNumber = Integer.parseInt(inputString);
                    System.out.println("[SERVER]: Received " + inputString + " from " + ip);

                    if (guessedNumber == randomNumber) {
                        System.out.println("[SERVER]: " + ip + " won, sending 'winning' message to client");
                        out.println("[SERVER]: You won! Game over.");
                        break;
                    } else if (guessedNumber > randomNumber) {
                        out.println("[SERVER]: The number is lesser than " + guessedNumber);
                    } else if (guessedNumber < randomNumber) {
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
