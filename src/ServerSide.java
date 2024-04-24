import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class ServerSide {

    public static void main(String[] args) {
        try{
      System.out.println("Server waiting user to give a number ");
      ServerSocket ss = new ServerSocket(3000);
      Socket soc = ss.accept();
      System.out.println("Connection Succefuly");
      BufferedReader in = new BufferedReader (new InputStreamReader(soc.getInputStream()));
      String str = in.readLine();
      PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
      out.println("server says : " + str);
}catch(Exception e){
 e.printStackTrace();
}
    }
}