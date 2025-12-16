// Improved ChatServer with multi-client support using threads and usernames

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 5000;
    // Keep track of all connected clients
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getRemoteSocketAddress());

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    static void broadcast(String message, ClientHandler excludeUser) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeUser) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // Remove client from set
    static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}

class ClientHandler implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Ask for username
            out.println("Enter your username:");
            username = in.readLine();

            System.out.println(username + " joined the chat.");
            ChatServer.broadcast("[SERVER] " + username + " has joined the chat.", this);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(username + ": " + message);
                ChatServer.broadcast(username + ": " + message, this);
            }
        } catch (IOException e) {
            System.out.println("Connection lost with " + username);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatServer.removeClient(this);
            ChatServer.broadcast("[SERVER] " + username + " left the chat.", this);
            System.out.println(username + " disconnected.");
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}
