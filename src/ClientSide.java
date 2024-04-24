import java.net.Socket;
import java.io.*;

public class ClientSide {

    public static void main(String[] args) {
        try{
            //starting the client side
            System.out.println("Client Side start ");
            Socket soc = new Socket("localhost",3000);
            //loop to keep the client side waiting for new inputs
            while (true) {
                //reading the user keybaord input
                BufferedReader userInput = new BufferedReader (new InputStreamReader(System.in));
                System.out.println("Enter a number between 0 and 20 :");
                String str = userInput.readLine();
                //check if the input is valid between 0 and 20 if not, ask again to fix the input
                if (Integer.parseInt(str) > 20 || Integer.parseInt(str) < 0) {
                    System.out.println("Invalid number: please enter a number between 0 and 20");
                    continue; //return to loop while from the begning
                }
                //sending the input into bytes to the server via a stream
                PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
                out.println(str);
                BufferedReader in = new BufferedReader (new InputStreamReader(soc.getInputStream()));
                System.out.println(in.readLine());
            }
        }catch(Exception e){
            //if any error occurs, print the error message
            e.printStackTrace(); 
        }
    }
}