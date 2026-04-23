import java.net.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiThreadedServer {
    // The Registry: A thread-safe list of all active handlers
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String args[]) throws Exception {

        try (ServerSocket ss = new ServerSocket(2000)) { // auto closable
            System.out.println("Server started...");
            while (true) {
                Socket sk = ss.accept();
                ClientHandler handler = new ClientHandler(sk, clients);
                clients.add(handler); // Add to Registry
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server Exception: " + e.getMessage());
        }

    }
}

class ClientHandler implements Runnable {
    private Socket sk;
    private CopyOnWriteArrayList<ClientHandler> clients;
    private PrintStream cout; // class level so sendMessage can see it
    private String username;

    public ClientHandler(Socket socket, CopyOnWriteArrayList<ClientHandler> clients) {
        this.sk = socket;
        this.clients = clients;
    }

    private void broadcast(String message) {
        for (ClientHandler client : clients) {

            if (client != this) {
                client.sendMessage(message);
            }
        }
    }

    public void sendMessage(String message) {
        cout.println(message);
    }

    @Override
    public void run() {
        try (
                BufferedReader cin = new BufferedReader(new InputStreamReader(sk.getInputStream()));
                PrintStream cout = new PrintStream(sk.getOutputStream());) {
            this.cout = cout;

            this.cout.println("Enter your username:");
            this.username = cin.readLine();

            if (this.username == null) return;

            broadcast("--- " + this.username + " has joined the chat! ---");

            String s;
            while (true) {
                s = cin.readLine();
                if (s == null || s.equalsIgnoreCase("END")) {
                    cout.println("BYE");
                    break;
                }
                broadcast(this.username + ": " + s);
            }
            sk.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            clients.remove(this);
            try {
                sk.close();
            } catch (IOException e) {
            }
        }
    }
}
