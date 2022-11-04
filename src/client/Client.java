package client;

import java.io.IOException;

public interface Client {
    public void startConnection() throws IOException;

    public String sendMessage(String msg) throws IOException;

    public void stopConnection() throws IOException;
}
