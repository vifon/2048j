package pl.elka.game2048j.server;

import java.io.IOException;

public class Server {

    public static void main(String[] args) {
        Model m = new Model(4);

        try {
            Controller c = new Controller(m, 2048);
        } catch (IOException e) {
            System.out.println("* Cannot allocate socket, closing...");
        }
    }

}
