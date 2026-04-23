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

            // THREAD 1: The Reader (Using a Lambda)
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    // TASK: Continuously read from 'in' and print to console
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("\nServer: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });
            readerThread.start();

            // THREAD 2: The Writer (Main Thread)
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if (userInput.equalsIgnoreCase("END")) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}