package server;
import java.net.*;
import java.io.*;

public class ServerImpl implements Server {

    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ServerImpl(int port) {
        this.port = port;

    }

    @Override
    public void start() throws IOException {
        System.out.println("Here");
        serverSocket = new ServerSocket(this.port);
        clientSocket = serverSocket.accept();
        printWriter = new PrintWriter(clientSocket.getOutputStream(),true);
        bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String message = bufferedReader.readLine();
        System.out.println("Server listening on port: " + this.port);
        while(message != null){
            if(".".equals(message)){
                System.out.println("Quitting server");
                break;
            }
            printWriter.println(message);
        }
    }

    @Override
    public void stop() throws IOException {
        this.printWriter.close();
        this.bufferedReader.close();
        this.serverSocket.close();
        this.clientSocket.close();
    }
}
