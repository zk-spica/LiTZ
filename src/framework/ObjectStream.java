package framework;

import java.io.IOException;

public class ObjectStream {
    public static <T> T recv(SocketStream s) throws IOException {
        if (s == null || !s.isValid()) return null;
        try {
            return (T)s.istream.readUnshared();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static void send(SocketStream s, Object... objects) throws IOException {
        if (s == null || !s.isValid()) return;
        for (Object object : objects) {
            s.ostream.writeUnshared(object);
            s.ostream.flush();
        }
    }
    public static void sendWithLock(SocketStream s, Object... objects) throws IOException {
        if (s == null || !s.isValid()) return;
        s.acquire();
        send(s, objects);
        s.release();
    }
}
