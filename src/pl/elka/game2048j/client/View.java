package pl.elka.game2048j.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import pl.elka.game2048j.common.Event;

/**
 * The Swing-based GUI of 2048j.
 */
public class View extends JFrame {
    private static final long serialVersionUID = 4904423755658577816L;

    private final BlockingQueue<Event> queue;
    private final int size;
    private JLabel[][] tiles;

    /**
     * Create a view of a size x size game.
     * @param size_
     */
    public View(int size_) {
        queue = new LinkedBlockingQueue<Event>();
        size = size_;
        tiles = new JLabel[size][size];

        setTitle("2048j");
        setSize(500,500);
        setLayout(new GridLayout(size, size));
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent arg0) { }

            @Override
            public void windowIconified(WindowEvent arg0) { }

            @Override
            public void windowDeiconified(WindowEvent arg0) { }

            @Override
            public void windowDeactivated(WindowEvent arg0) { }

            @Override
            public void windowClosing(WindowEvent arg0) {
                queue.add(Event.CLOSE);
            }

            @Override
            public void windowClosed(WindowEvent arg0) { }

            @Override
            public void windowActivated(WindowEvent arg0) { }
        });

        KeyListener keylistener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_DOWN:
                    queue.add(Event.DOWN);
                    break;
                case java.awt.event.KeyEvent.VK_UP:
                    queue.add(Event.UP);
                    break;
                case java.awt.event.KeyEvent.VK_LEFT:
                    queue.add(Event.LEFT);
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT:
                    queue.add(Event.RIGHT);
                    break;
                case java.awt.event.KeyEvent.VK_Q:
                    queue.add(Event.CLOSE);
                    break;
                case java.awt.event.KeyEvent.VK_R:
                    queue.add(Event.RESTART);
                    break;
                }
            }
        };

        Font font = new Font("Serif", Font.BOLD, 48);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tiles[i][j].setFont(font);
                tiles[i][j].setOpaque(true);
                add(tiles[i][j]);
            }
        }

        gameover(false);

        addKeyListener(keylistener);
    }

    /**
     * Sets the (x,y) tile to a given value.
     * @param x
     * @param y
     * @param value
     */
    public void setTile(int x, int y, Integer value) {
        JLabel l = tiles[y][x];
        if (value != null) {
            l.setBackground(new Color(value/16f+0.05f, value/16f+0.05f, value/16f+0.05f));
            value = 1 << value;
            l.setText(value.toString());
        } else {
            l.setText("");
            l.setBackground(Color.BLACK);
        }
    }

    /**
     * Turns on/off the gameover indicator.
     * @param b
     */
    public void gameover(boolean b) {
        if (b) {
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles.length; j++) {
                    tiles[i][j].setForeground(Color.RED);
                }
            }
        } else {
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles.length; j++) {
                    tiles[i][j].setForeground(Color.WHITE);
                }
            }
        }
    }

    /**
     * The blocking read of a event.
     * @return UI event
     * @throws InterruptedException
     */
    public Event getEvent() throws InterruptedException {
        return queue.take();
    }

    /**
     * Closes the view.
     */
    public void close() {
        setVisible(false);
        dispose();
    }

}
