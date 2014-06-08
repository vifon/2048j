package pl.elka.game2048j.client;

import java.io.IOException;

public class Client {

    public static void main(String[] args) {
        View v = new View(4);

        try {
            Controller c = new Controller(v, "localhost", 2048);
        } catch (IOException e) {
            System.out.println("* Cannot connect to server, closing...");
        }
    }
}
