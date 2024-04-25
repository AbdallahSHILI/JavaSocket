import java.net.Socket;
import java.io.*;

public class ClientSide {

    public static String serverMessage(Socket soc) {
        String message = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            message = in.readLine();
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static void sendToServer(Socket soc, String message) {
        try {
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserInput() {
        String input = "";
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            input = userInput.readLine();
            return input;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            Socket soc = new Socket("localhost", 3000);
            System.out.println("[CLIENT]: Client Side started on port 3000 ");

            while (true) {
                String serverMessage = serverMessage(soc); // Read server message

                if (serverMessage.contains("Guess a number")) {
                    // It's this client's turn to guess.
                    String str = getUserInput();
                    try {
                        int x = Integer.parseInt(str);
                        if (x > 20 || x < 0) {
                            System.out.println("[CLIENT]: Invalid number, only a number between 0 and 20");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[CLIENT]: Must enter a Number between 0 and 20");
                        continue;
                    }
                    sendToServer(soc, str);
                } else if (serverMessage.contains("won")) {
                    // Handle winning message and ask if they want to play again.
                    System.out.println("[CLIENT]: You won! Do you want to play again? (yes/no)");
                    String userReplay = getUserInput();
                    sendToServer(soc, userReplay);
                    if (!userReplay.equalsIgnoreCase("yes")) {
                        System.out.println("[CLIENT]: Exiting the game...");
                        break;
                    }
                } else if (serverMessage.contains("lesser") || serverMessage.contains("greater")) {
                    // Wait for the next prompt from the server.
                    continue;
                } else {
                    // If the server sends any other prompts, handle them generically.
                    System.out.println("[CLIENT]: " + serverMessage);
                }
            }

            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
