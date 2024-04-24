import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;

public class ServerSide {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("[SERVER]: Server started. Waiting for Client response...");
            Socket soc = ss.accept();
            System.out.println("[SERVER]: Connection Successful");

            Random random = new Random();
            int randomNumber = random.nextInt(21);
            System.out.println("[SERVER]: number generated is " + randomNumber);

            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            out.println("[SERVER]: Guess a number between 0 and 20.");

            while (true) {
                String inputString = in.readLine();
                if (inputString == null) {
                    continue;
                }
                int guessedNumber = Integer.parseInt(inputString);
                System.out.println("[SERVER]: received " + inputString);

                if (guessedNumber == randomNumber) {
                    System.out.println("[SERVER]: Client won, sending 'winning' message to client");
                    out.println("[SERVER]: You won! replay? yes/no");

                    System.out.println("[SERVER]: waiting for replay message...");
                    String replayMessage = in.readLine();
                    if (replayMessage != null && replayMessage.equalsIgnoreCase("yes")) {
                        System.out.println("[SERVER]: Client accepted replay request");
                        randomNumber = random.nextInt(21);
                        System.out.println("[SERVER]: New number generated is " + randomNumber);
                        out.println("[SERVER]: New game started. Guess a number between 0 and 20.");
                        continue;
                    } else {
                        out.println("[SERVER]: Server shutting down...");
                        System.out.println("[SERVER]: Client Refused to replay, shutting down...");
                        break;
                    }
                } else if (guessedNumber > randomNumber) {
                    out.println("[SERVER]: The number is lesser than " + guessedNumber);
                } else if (guessedNumber < randomNumber) {
                    out.println("[SERVER]: The number is greater than " + guessedNumber);
                }
                // soc.close();
            }

            soc.close();
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
