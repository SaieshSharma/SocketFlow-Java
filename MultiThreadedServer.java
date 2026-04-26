import java.net.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;

public class MultiThreadedServer {
    // The Registry: A thread-safe list of all active handlers
    // This is static - it belongs to the Class.
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String args[]) throws Exception {

        System.out.println("Bastion Master starting...");
        logEvent("Server initialized in Docker container.");

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
                            logEvent("MASTER EXEC: Sent command '" + cmd + "' to all active nodes.");
                            System.out.println("Master: Command '" + cmd + "' sent to all nodes.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public static void logEvent(String message) {
        String logPath = "/app/logs/bastion.log";
        try (PrintWriter out = new PrintWriter(new FileWriter(logPath, true))) {
            out.println(java.time.LocalDateTime.now() + " : " + message);
            out.flush();
            System.out.println(">>> [LOG SUCCESS]: Written to " + logPath); // Added for debugging
        } catch (Exception e) {
            // This will now show up in your PowerShell window
            System.err.println("!!! [LOG ERROR]: " + e.getMessage());
            e.printStackTrace();
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

    private void handlePacket(String rawPacket) {
        MultiThreadedServer.logEvent("INCOMING [" + this.username + "]: " + rawPacket);
        try {
            // Split the packet into parts: [TYPE:X, FROM:Y, DATA:Z]
            String[] parts = rawPacket.split("\\|");
            String type = "", from = "", data = "";

            for (String part : parts) {
                if (part.startsWith("TYPE:"))
                    type = part.substring(5);
                if (part.startsWith("FROM:"))
                    from = part.substring(5);
                if (part.startsWith("DATA:"))
                    data = part.substring(5);
            }

            // ROUTING LOGIC
            switch (type) {
                case "HEARTBEAT":
                    // System.out.print("."); // Just a dot to show it's alive without filling the
                    // screen
                    break;

                case "CHAT":
                    // Standard Log Format: [TIMESTAMP] [TYPE] [FROM] : MESSAGE
                    String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
                    System.out.println("[" + timestamp + "] [INFO] " + from + ": " + data);

                    broadcast(from + ": " + data);
                    break;

                case "COMMAND_RESULT":
                    System.out.println(data);
                    break;
            }
        } catch (Exception e) {
            // If it's not a packet, treat it as a legacy chat message for safety
            broadcast(this.username + ": " + rawPacket);
        }
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
                MultiThreadedServer.logEvent("AUTH: Node '" + this.username + "' connected to Bastion.");
                broadcast("--- " + this.username + " has joined the chat! ---");
            }

            String s;
            while ((s = cin.readLine()) != null) {
                // CRITICAL: Pass the raw string to the Parser
                handlePacket(s);
            }
        } catch (Exception e) {
            System.out.println("Node disconnected.");
        } finally {
            clients.remove(this);
        }
    }

}
