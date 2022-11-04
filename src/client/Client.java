package client;

import java.io.IOException;

public interface Client {

    void start() throws IOException;

    void stop() throws IOException;

    void keepConnection() throws IOException;

}