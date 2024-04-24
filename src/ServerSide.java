import java.net.ServerSocket;
import java.net.Socket;
import java.io.*; //for input and output streams
import java.util.Random; //for random number generation

public class ServerSide {

    public static void main(String[] args) {
        try {
            // Create a server socket on port 3000
            System.out.println("Server waiting for user to give a number");
            ServerSocket ss = new ServerSocket(3000);
            Socket soc = ss.accept();
            System.out.println("Connection Successful");
            // Create a BufferedReader and PrintWriter to read and write to the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            // Generate a random integer between 0 and 20
            Random random = new Random();
            int randomNumber = random.nextInt(21);
            while (true) {
                String str = in.readLine();
                if (str == null) {
                    // if the input is null, then the checking is cancelled
                    break;
                }
                int x = Integer.parseInt(str);
                System.out.println("Server received: " + str);
                // check if the number received is equal to the random number generated
                if (x == randomNumber) {
                    out.println("Server says: You won");
                    out.println("Random number generated: " + randomNumber);
                    break;
                } else if (x > randomNumber) {
                    out.println("Server says: your number is greater");
                } else if (x < randomNumber) {
                    out.println("Server says: you number is lesser");
                }
            }
            // close the socket and server socket
            soc.close();
            ss.close();
        } catch (Exception e) {
            // print the exception
            e.printStackTrace();
        }
    }
}
