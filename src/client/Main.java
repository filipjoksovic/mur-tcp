package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Socket socket = null;

        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket("localhost", 1234);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            while (true) {
                String message = sc.nextLine();

                out.println(message);
                String resp = in.readLine();
                System.out.println(resp.replace('#','\n'));
                if (message.equals("bye")) {
                    System.out.println("Shutting down.");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
