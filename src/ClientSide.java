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
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("[CLIENT]: Enter the IP address of the server or leave empty for localhost");
            String ip = getUserInput();
            if (ip == "")
                ip = "localhost";
            Socket soc = new Socket(ip, 3000);
            System.out.println(
                    "[CLIENT]: Client Side started on port 3000\n[CLIENT]: Waiting for another player to join...");

            while (true) {

                String serverMessage = serverMessage(soc); // Read server message

                if (serverMessage.contains("Guess the right number")) {
                    // It's this client's turn to guess.
                    while (true) {// Loop until the user enters a valid number.
                        try {
                            String str = getUserInput();
                            int x = Integer.parseInt(str);
                            if (x > 20 || x < 0) {
                                System.out.println("[CLIENT]: Invalid number, only a number between 0 and 20");
                                continue;
                            }
                            sendToServer(soc, str); // Send the number to the server.
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("[CLIENT]: Must enter a Number between 0 and 20");
                            continue;
                        }
                    }
                } else if (serverMessage.contains("won")) {
                    // Handle winning message and ask if they want to play again.
                    System.out.println(serverMessage);
                    String userReplay = getUserInput();
                    sendToServer(soc, userReplay);
                    System.out.println(
                            "[CLIENT]: Replay Request sent to Server, Loading...");
                    continue;
                } else if (serverMessage.contains("lost")) {
                    // Handle losing message and ask if they want to play again.
                    System.out.println(serverMessage);
                    String userReplay = getUserInput();
                    sendToServer(soc, userReplay);
                    System.out.println(
                            "[CLIENT]: Replay Request sent to Server, Loading...");
                    continue;
                }
            }

            // soc.close();
        } catch (Exception e) {
            System.out.println("[CLIENT]: Connection error: " + e.getMessage());
        }
        System.out.println("[CLIENT]: Connection closed, shutting down...");
    }
}
