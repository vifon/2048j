package pl.elka.game2048j;

import java.io.IOException;

import pl.elka.game2048j.client.View;
import pl.elka.game2048j.server.Model;

public class Standalone {

    public static void main(String[] args) {
        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pl.elka.game2048j.server.Controller c = 
                            new pl.elka.game2048j.server.Controller(new Model(4), 2048);
                } catch (IOException e) {
                    ;
                }
            }
        });
        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pl.elka.game2048j.client.Controller c = 
                            new pl.elka.game2048j.client.Controller(new View(4), "localhost", 2048);
                } catch (IOException e) {
                    ;
                }
            }
        });

        server.start();
        client.start();
        try {
            client.join();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
