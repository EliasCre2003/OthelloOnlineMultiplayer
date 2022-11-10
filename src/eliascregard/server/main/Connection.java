package eliascregard.server.main;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public int id;
    private EventListener listener;
    private boolean running = false;

    public Connection(Socket socket) {
        this.socket = socket;
        id = 0;

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            listener = new EventListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
       try {
           running = true;
           while (running) {
               try {
                   Object data = in.readObject();
                   listener.received(data, this);
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public void close() {
        try {
            running = false;
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
