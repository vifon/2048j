package pl.elka.game2048j.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import pl.elka.game2048j.common.Event;
import pl.elka.game2048j.common.Matrix;
import pl.elka.game2048j.common.Response;

/**
 * The server-side part of the 2048 controller.
 * Receives events from the client and interacts with the model.
 */
public class Controller {

    private Model model;

    private ServerSocket server;
    private Socket client;
    private ObjectOutputStream socket_out;
    private ObjectInputStream socket_in;
    private boolean gameover;

    /**
     * Create a server listening on a given port.
     * @param m
     * @param port
     * @throws IOException
     */
    public Controller(Model m, int port) throws IOException {
        model = m;

        gameover = false;

        server = new ServerSocket(port);
        client = server.accept();
        System.out.println("* A client has connected");

        socket_out = new ObjectOutputStream(client.getOutputStream());
        socket_in = new ObjectInputStream(client.getInputStream());

        Thread thread = new Thread(new EventLoop());
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            ;
        }
    }

    /**
     * Send a response back to the client.
     * @param o
     * @throws IOException
     */
    private void sendResponse(Object o) throws IOException {
        socket_out.writeObject(o);
    }

    /**
     * Read the event from client.
     * @return a single event from the client
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Object readEvent() throws ClassNotFoundException, IOException {
        return socket_in.readObject();
    }

    /**
     * Reads the events from the client and reacts to them.
     */
    class EventLoop implements Runnable {
        public void run() {
            try {
                try {
                    model.addTile();
                } catch (NoFreeSpaceException e2) {
                    // will never happen
                }
                while (true) {
                    Matrix snapshot = model.getSnapshot();
                    sendResponse(snapshot);

                    Event e = (Event)readEvent();

                    switch (e) {
                    case DOWN:
                    case UP:
                    case LEFT:
                    case RIGHT:
                        if (!gameover) {
                            model.setGravity(e.toDirection());
                        }
                        break;
                    case RESTART:
                        model = new Model(model.getSize());
                        gameover = false;
                        socket_out.writeObject(Response.NO_GAMEOVER);
                        break;
                    case CLOSE:
                        // never received
                        break;
                    }

                    if (!model.isMovePossible()) {
                        gameover = true;
                        sendResponse(Response.GAMEOVER);
                    }

                    try {
                        Matrix new_snapshot = model.getSnapshot();
                        if (!new_snapshot.equals(snapshot)) {
                            model.addTile();
                        }
                    } catch (NoFreeSpaceException e1) {
                        ;
                    }
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("* Client has disconnected, closing...");
            }
        }
    }
}
