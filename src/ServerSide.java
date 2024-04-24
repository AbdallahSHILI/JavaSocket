import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Random;

public class ServerSide {

    public static void main(String[] args) {
        try {
            System.out.println("Server waiting user to give a number ");
            ServerSocket ss = new ServerSocket(3000);
            Socket soc = ss.accept();
            System.out.println("Connection Successful");
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String str = in.readLine();
            
            // Generate a random integer between 0 and 20
            Random random = new Random();
            int randomNumber = random.nextInt(21);
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println("Server says: " + str);
            out.println("Random number generated: " + randomNumber);
            if (Integer.parseInt(str) == randomNumber) {
                out.println("Server says: " + "You won");
            } else if (Integer.parseInt(str) > randomNumber) {
                out.println("Server says: the number is greater");
            } else {
                out.println("Server says: the number is lesser");
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}