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
                if (Integer.parseInt(str) > 20 || Integer.parseInt(str) < 0) {
                    System.out.println("[CLIENT]: Invalid number, only a number between 0 and 20");
                    continue;
                }
                out.println(str);

                serverMessage = in.readLine();
                System.out.println(serverMessage);

                if (serverMessage.equals("[SERVER]: You won! replay? yes/no")) {
                    String replayMessage = in.readLine();
                    System.out.println(replayMessage);
                    String userReplay = userInput.readLine();
                    out.println(userReplay);
                    if (!userReplay.equalsIgnoreCase("yes")) {
                        System.out.println("[CLIENT]: sending replay request to the server.");
                        continue;
                    } else {
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
