import java.net.Socket;
import java.io.*;

public class ClientSide {

    public static void main(String[] args) {
        try{
      System.out.println("Client Side start ");
      Socket soc = new Socket("localhost",3000);
      BufferedReader userInput = new BufferedReader (new InputStreamReader(System.in));
      System.out.println("Enter a number between 0 and 20 :");
      String str = userInput.readLine();
      PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
      out.println(str);
      BufferedReader in = new BufferedReader (new InputStreamReader(soc.getInputStream()));
      System.out.println(in.readLine());
}catch(Exception e){
 e.printStackTrace();
}
    }
}