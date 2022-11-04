package server;

public class Main {

    public static void main(String[] args) {
        ServerImpl server = new ServerImpl(1234);
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Exception ocurred: " + e.getMessage());
        } finally {
            server.stop();
        }
    }

}
