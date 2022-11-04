package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientImpl implements Client {

    private final int port;

    Socket socket = null;

    PrintWriter out = null;
    BufferedReader in = null;
    Scanner sc = null;

    public ClientImpl(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        socket = new Socket("localhost", this.port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sc = new Scanner(System.in);

        keepConnection();
    }

    public void keepConnection() throws IOException {
        while (true) {
            String message = sc.nextLine();
            out.println(message);

            String response = in.readLine();
            System.out.println(response.replace('#', '\n'));

            if (message.equals("bye")) {
                System.out.println("Shutting down.");
                break;
            }
        }

    }

    public void stop() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
