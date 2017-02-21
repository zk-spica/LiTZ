package framework;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected volatile SocketStream server = null;

    public boolean connect(String serverCode) {
        try {
            server = new SocketStream();
            server.bind(new Socket(InvitationCode.getInetAddress(serverCode), InvitationCode.getPort(serverCode)));
        } catch (IOException ex) {
            ex.printStackTrace();
            close();
            return false;
        }
        return true;
    }

    public void close() {
        if (server == null) return;
        server.close();
        server = null;
    }

}
