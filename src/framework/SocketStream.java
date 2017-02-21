package framework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class SocketStream {
    private Socket socket = null;
    public ObjectOutputStream ostream = null;
    public ObjectInputStream istream = null;
    private Semaphore lock = null;
    public boolean bind(Socket socket) {
        this.socket = socket;
        try {
            this.ostream = new ObjectOutputStream(socket.getOutputStream());
            this.istream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            close();
            return false;
        }
        lock = new Semaphore(1);
        return true;
    }
    public void acquire() {
    	if (lock != null) lock.acquireUninterruptibly();
    }
    public void release() {
    	if (lock != null) lock.release();
    }
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.socket = null;
        this.ostream = null;
        this.istream = null;
        this.lock = null;
    }
    public boolean isValid() {
        return socket != null;
    }
}
