import java.net.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiThreadedServer {
    // The Registry: A thread-safe list of all active handlers
    // This is static - it belongs to the Class.
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String args[]) throws Exception {

        try (ServerSocket ss = new ServerSocket(2000)) { // auto closable
            System.out.println("Server started...");

            // NEW: Thread to listen to the SERVER'S keyboard
        new Thread(() -> {
            try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    String cmd = console.readLine();
if (cmd != null) {
                // FIXED: We send the clean "EXEC:" prefix so the client recognizes it
                broadcastToAll("EXEC:" + cmd); 
                System.out.println("Master: Command '" + cmd + "' sent to all nodes.");
            }
                }
            } catch (IOException e) { e.printStackTrace(); }
        }).start();


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
        public static void broadcastToAll(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
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

if (this.username != null) {
    // VISIBILITY: This will now show up in Terminal 1 (Server)
    System.out.println(">>> [NODE CONNECTED]: " + this.username); 
    broadcast("--- " + this.username + " has joined the chat! ---");
}

            String s;
while ((s = cin.readLine()) != null) {
    if (s.equalsIgnoreCase("END")) break;

    // ADD THIS LINE: Prints the Node's output to the Master's Terminal
    System.out.println(this.username + " response: " + s); 
    
    // Optional: Still broadcast if you want other nodes to see the output
    broadcast(this.username + ": " + s);
}
            sk.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            clients.remove(this);
            broadcast("--- " + this.username + " has left the chat ---");
            try {
                sk.close();
            } catch (IOException e) {
            }
        }
    }


}
