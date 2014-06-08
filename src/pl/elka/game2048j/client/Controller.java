package pl.elka.game2048j.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pl.elka.game2048j.common.Event;
import pl.elka.game2048j.common.Matrix;
import pl.elka.game2048j.common.Response;

/**
 * The client-side part of the 2048j controller.
 * Reads events from the view and passes them to the server.
 */
public class Controller {

    private final View view;

    private Socket server;
    private ObjectOutputStream socket_out;
    private ObjectInputStream socket_in;


    /**
     * Create a client bound to a given host and port.
     * @param v
     * @param host
     * @param port
     * @throws IOException
     */
    public Controller(View v, String host, int port) throws IOException {
        view = v;

        server = new Socket(host, port);
        System.out.println("* Connected to server");

        socket_out = new ObjectOutputStream(server.getOutputStream());
        socket_in = new ObjectInputStream(server.getInputStream());

        view.setVisible(true);

        Thread eventloop = new Thread(new EventLoop());
        eventloop.start();

        Thread serverlistener = new Thread(new ResponseListener());
        serverlistener.start();

        try {
            serverlistener.join();
            eventloop.interrupt();
            eventloop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pass the event to server.
     * @param e
     * @throws IOException
     */
    private void sendEvent(Event e) throws IOException {
        socket_out.writeObject(e);
    }

    /**
     * Read a response from the server.
     * @return server response (must be cast)
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Object readResponse() throws ClassNotFoundException, IOException {
        return socket_in.readObject();
    }

    /**
     * Read the events from the view and pass them to the server.
     */
    class EventLoop implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Event e;
                    try {
                        e = view.getEvent();
                    } catch (InterruptedException e1) {
                        view.close();
                        server.close();
                        return;
                    }

                    switch (e) {
                    case DOWN:
                    case UP:
                    case LEFT:
                    case RIGHT:
                        sendEvent(e);
                        break;
                    case CLOSE:
                        view.close();
                        server.close();
                        return;
                    case RESTART:
                        sendEvent(e);
                        view.gameover(false);
                        break;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the server response messages and updates the view. 
     */
    class ResponseListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Object response = readResponse();
                    if (response instanceof Matrix) {
                        Matrix matrix = (Matrix)response;
                        for (int i = 0; i < matrix.getSize(); i++) {
                            for (int j = 0; j < matrix.getSize(); j++) {
                                view.setTile(i, j, matrix.get(i,j));
                            }
                        }
                    } else if (response instanceof Response) {
                        Response r = (Response)response;
                        switch (r) {
                        case GAMEOVER:
                            view.gameover(true);
                            break;
                        case NO_GAMEOVER:
                            view.gameover(false);
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("* Disconnected from server, closing...");
            }
        }
    }
}