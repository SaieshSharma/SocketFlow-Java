import java.net.*;
import java.io.*;

public class MultiThreadedServer {
    public static void main(String args[]) throws Exception {

        try (ServerSocket ss = new ServerSocket(2000)) { // auto closable
    while (true) {
        Socket sk = ss.accept();
        new Thread(new ClientHandler(sk)).start();
    }
} catch (IOException e) {
    System.err.println("Server Exception: " + e.getMessage());
}

    }
}

class ClientHandler implements Runnable {
    private Socket sk;

    public ClientHandler(Socket socket) {
        this.sk = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader cin = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            PrintStream cout = new PrintStream(sk.getOutputStream());
        ) {
            String s;
            while (true) {
                s = cin.readLine();
                if (s == null || s.equalsIgnoreCase("END")) {
                    cout.println("BYE");
                    break;
                }
                System.out.println("Received: " + s);
                cout.println("Echo: " + s); 
            }
            sk.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
