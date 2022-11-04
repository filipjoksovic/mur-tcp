package server;

import java.io.*;
import java.net.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        Socket socket = null;

        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(1234);
        System.out.println("Server listening on port: " + 1234);

        while (true) {
            System.out.println("Waiting for connection");

            socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress() + ":" + socket.getPort());

            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                handleExistingConnection(in, out, socket);
            } catch (Exception e) {
                System.out.println("Error occured: " + e.getMessage());
            }
        }
    }

    public static void handleExistingConnection(BufferedReader in, PrintWriter out, Socket socket) {
        while (true) {
            String received = null;
            boolean isConnected = true;
            try {
                received = in.readLine();
            } catch (IOException e) {
                break;
            }

            if (received.equals("bye")) {
                break;
            }
            String receivedChoice = received.substring(0, 1);
            System.out.println("Client: " + received);
            String response = respond(receivedChoice, received);
            System.out.println("Response: " + response);
            out.println(response);
        }
    }

    public static String respond(String receivedChoice, String received) {
        switch (receivedChoice) {

            case "A":
                return "Greetings!";
            case "B":
                return "Current date: " + LocalDate.now().toString();
            case "C":
                return "Working directory of the server: " + System.getProperty("user.dir");
            case "D":
                return received.substring(1);
            case "E":
                try {
                    return "Ime streznika: " + InetAddress.getLocalHost().getHostName() + ", operacijski sistem: " + System.getProperty("os.name");
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            case "F":
                return chess(received.trim());
            default:
                return "Napacen unos. Poskusite s crkami A-G.";
        }
    }

    public static String chess(String init) {
        String[] parsed = init.split(" ");
        String builtResult = "";

        //create matrix
        for (String row : parsed[0].split("/")) {
            for (Character figure : row.toCharArray()) {
                if (Character.isDigit(figure)) {
                    for (int i = 0; i < Integer.parseInt(figure.toString()); i++) {
                        builtResult += " ";
                    }
                } else {
                    builtResult += figure;
                }
            }
            builtResult += "#";
        }

        //figure out the turn
        builtResult += "#Na vrsti: ";
        if (parsed[1].equalsIgnoreCase("w")) {
            builtResult += "beli";
        } else if (parsed[1].equalsIgnoreCase("b")) {
            builtResult += "crni";
        }
        builtResult += "#Moznosti rokade: #";

        //figure out castling
        if (parsed[2].charAt(0) == 'K') {
            builtResult += "Beli, kraljeva stran #";
        }
        if (parsed[2].charAt(1) == 'Q') {
            builtResult += "Beli, damina stran #";

        }
        if (parsed[2].charAt(2) == 'k') {
            builtResult += "Crni, kraljeva stran #";
        }
        if (parsed[2].charAt(3) == 'q') {
            builtResult += "Crni, damina stran #";
        }

        builtResult += ("#Moznost en passant: " + (parsed[3].equalsIgnoreCase("-") ? "noben" : parsed[3]) + "#");

        builtResult += "#Stevilo polpotez: " + parsed[4] + "#";
        builtResult += "#Stevilo trenutne potez: " + parsed[5] + "#";

        return builtResult;
    }

}
