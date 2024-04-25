import java.net.Socket;
import java.io.*;

public class ClientSide {

    public static void main(String[] args) {
        try {
            Socket soc = new Socket("localhost", 3000);
            System.out.println("[CLIENT]: Client Side started on port 3000 ");

            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String serverMessage = in.readLine();
            System.out.println(serverMessage);

            while (true) {
                String str = userInput.readLine();
                try {
                    int x = Integer.parseInt(str); //check if the input is a number
                } catch (NumberFormatException e) {
                    System.out.println("[CLIENT]: Must enter a Number between 0 and 20");
                    continue;
                }
                if (Integer.parseInt(str) > 20 || Integer.parseInt(str) < 0) {
                    System.out.println("[CLIENT]: Invalid number, only a number between 0 and 20");
                    continue;
                }
                out.println(str);

                serverMessage = in.readLine();
                System.out.println(serverMessage);

                if (serverMessage.contains("[SERVER]: You won!")) {
                    String userReplay = userInput.readLine();
                    out.println(userReplay);
                    if (userReplay.equalsIgnoreCase("yes")) {
                        System.out.println("[CLIENT]: sending replay request to the server.");
                        System.out.println(in.readLine());
                        continue;
                    } else {
                        serverMessage = in.readLine();
                        System.out.println(serverMessage);
                        System.out.println("[CLIENT]: Exiting the game.");
                        break;
                    }
                }
            }

            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
