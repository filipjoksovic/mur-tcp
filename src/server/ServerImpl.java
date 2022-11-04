package server;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;

public class ServerImpl implements Server {

    private final int port;

    private PrintWriter out = null;
    private BufferedReader in = null;

    private Socket socket = null;
    ServerSocket serverSocket = null;

    public ServerImpl(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        serverSocket = new ServerSocket(1234);
        System.out.println("Server listening on port: " + this.port);

        while (true) {
            System.out.println("Waiting for connection");
            socket = serverSocket.accept();

            System.out.println("Client connected: " + socket.getInetAddress() + ":" + socket.getPort());

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.handleConnection();
        }
    }

    @Override
    public void handleConnection() throws IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        while (true) {
            String received;
            received = this.in.readLine();

            if (received.equals("bye")) {
                break;
            }
            String receivedChoice = received.substring(0, 1);
            System.out.println("Client: " + received);
            String response = respond(receivedChoice, received);
            System.out.println("Response: " + response);
            this.out.println(response);
        }
    }

    @Override
    public String respond(String receivedChoice, String received) throws UnknownHostException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return switch (receivedChoice) {
            case "A" -> "Greetings!";
            case "B" -> "Current date: " + LocalDate.now();
            case "C" -> "Working directory of the server: " + System.getProperty("user.dir");
            case "D" -> received.substring(1);
            case "E" ->
                    "Ime streznika: " + InetAddress.getLocalHost().getHostName() + ", operacijski sistem: " + System.getProperty("os.name");
            case "F" -> this.chess(received.trim());
            case "G" -> this.encrypt(received.trim());
            default -> "Napacen unos. Poskusite s crkami A-G.";
        };
    }

    public String encrypt(String phrase) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] secretKey = "ifgodexistsit'sfuckingme".getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        byte[] iv = "21102002".getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] secretMessagesBytes = phrase.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessagesBytes);

        return Base64.getEncoder().encodeToString(encryptedMessageBytes);
    }

    public String chess(String init) {
        String[] parsed = init.split(" ");
        StringBuilder builtResult = new StringBuilder();

        //create matrix
        for (String row : parsed[0].split("/")) {
            for (Character figure : row.toCharArray()) {
                if (Character.isDigit(figure)) {
                    builtResult.append(" ".repeat(Math.max(0, Integer.parseInt(figure.toString()))));
                } else {
                    builtResult.append(figure);
                }
            }
            builtResult.append("#");
        }

        //figure out the turn
        builtResult.append("#Na vrsti: ");
        if (parsed[1].equalsIgnoreCase("w")) {
            builtResult.append("beli");
        } else if (parsed[1].equalsIgnoreCase("b")) {
            builtResult.append("crni");
        }
        builtResult.append("#Moznosti rokade: #");

        //figure out castling
        if (parsed[2].charAt(0) == 'K') {
            builtResult.append("Beli, kraljeva stran #");
        }
        if (parsed[2].charAt(1) == 'Q') {
            builtResult.append("Beli, damina stran #");

        }
        if (parsed[2].charAt(2) == 'k') {
            builtResult.append("Crni, kraljeva stran #");
        }
        if (parsed[2].charAt(3) == 'q') {
            builtResult.append("Crni, damina stran #");
        }

        builtResult.append("#Moznost en passant: ").append(parsed[3].equalsIgnoreCase("-") ? "noben" : parsed[3]).append("#");

        builtResult.append("#Stevilo polpotez: ").append(parsed[4]).append("#");
        builtResult.append("#Stevilo trenutne potez: ").append(parsed[5]).append("#");

        return builtResult.toString();
    }

    @Override
    public void stop() {
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
            this.serverSocket.close();
        } catch (Exception e) {
            System.out.println("Error while stopping");
            System.exit(69);
        }
    }
}
