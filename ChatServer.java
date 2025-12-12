import java.io.*;
import java.net.*;

public class ChatServer1 {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started... Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader inputFromClient =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outputToClient =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader keyboard =
                    new BufferedReader(new InputStreamReader(System.in));

            String clientMessage, serverMessage;

            while (true) {
                // Receive message from client
                clientMessage = inputFromClient.readLine();
                if (clientMessage == null || clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected.");
                    break;
                }
                System.out.println("Client: " + clientMessage);

                // Send message to client
                System.out.print("Server: ");
                serverMessage = keyboard.readLine();
                outputToClient.write(serverMessage);
                outputToClient.newLine();
                outputToClient.flush();

                if (serverMessage.equalsIgnoreCase("exit"))
                    break;
            }

            socket.close();
            serverSocket.close();
            System.out.println("Server closed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
