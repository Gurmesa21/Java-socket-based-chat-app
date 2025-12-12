import java.io.*;
import java.net.*;

public class ChatClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server.");

            BufferedReader inputFromServer =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outputToServer =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader keyboard =
                    new BufferedReader(new InputStreamReader(System.in));

            String serverMessage, clientMessage;

            while (true) {
                // Send message to server
                System.out.print("Client: ");
                clientMessage = keyboard.readLine();
                outputToServer.write(clientMessage);
                outputToServer.newLine();
                outputToServer.flush();

                if (clientMessage.equalsIgnoreCase("exit"))
                    break;

                // Receive from server
                serverMessage = inputFromServer.readLine();
                if (serverMessage == null || serverMessage.equalsIgnoreCase("exit"))
                    break;

                System.out.println("Server: " + serverMessage);
            }

            socket.close();
            System.out.println("Disconnected from server.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
