package client;

public class Main {
    public static void main(String[] args) {
        ClientImpl client = new ClientImpl(1234);
        try {
            client.start();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            System.exit(420);
        }
    }
}
