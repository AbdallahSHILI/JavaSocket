import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;

public class ServerSide {

    public static void main(String[] args) {
        try {
            System.out.println("Server waiting for user to give a number");
            ServerSocket ss = new ServerSocket(3000);
            Socket soc = ss.accept();
            System.out.println("Connection Successful");
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            // Generate a random integer between 0 and 20
            Random random = new Random();
            int randomNumber = random.nextInt(21);

            while (true) {
                String str = in.readLine();
                if (str == null) {
                    break;
                }
                int x = Integer.parseInt(str);
                System.out.println("Server received: " + str);
                if (x == randomNumber) {
                    out.println("Server says: You won");
                    out.println("Random number generated: " + randomNumber);
                    break;
                } else if (x > randomNumber) {
                    out.println("Server says: the number is lesser");
                } else if (x < randomNumber) {
                    out.println("Server says: the number is greater");
                }
            }
            soc.close();
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
