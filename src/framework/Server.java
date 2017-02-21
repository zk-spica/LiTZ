package framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server {
    protected volatile ArrayList<SocketStream> clients = null;
    private final Semaphore lockClients = new Semaphore(1);
    private int useAvailableClientIndex(SocketStream client) {
        lockClients.acquireUninterruptibly();
        int size = clients.size();
        for (int clientIndex = 0; clientIndex < size; ++clientIndex) {
            if (clients.get(clientIndex) == null) {
                clients.set(clientIndex, client);
                lockClients.release();
                return clientIndex;
            }
        }
        clients.add(client);
        lockClients.release();
        return size;
    }

    private volatile ServerSocket listener = null;
    public void ready() {
        try {
            listener = new ServerSocket(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        clients = new ArrayList<>();
    }
    public String getCode() {
        if (listener != null) {
            return InvitationCode.getCode(listener.getInetAddress(), listener.getLocalPort());
        } else {
            return "";
        }
    }

    private class ListenerThread extends Thread {
        private volatile boolean running;
        @Override
        public void run() {
            running = true;
            while (running && (maxClientNumber() < 0 || clients.size() < maxClientNumber())) {
                try {
                    SocketStream client = new SocketStream();
                    if (client.bind(listener.accept())) {
                        onConnect(client);
                        onArrayed(useAvailableClientIndex(client));
                    }
                } catch (SocketException ex) {
                    if (running) ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        void finish() {
            running = false;
            try {
                listener.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            listener = null;
        }
    }
    private volatile ListenerThread listenerThread = null;
    public void listen() {
        if (listenerThread != null) return;
        listenerThread = new ListenerThread();
        listenerThread.start();
    }
    public void finish() {
        if (listenerThread == null) return;
        listenerThread.finish();
        listenerThread = null;
    }

    public void close(int clientIndex) {
    	if (clients == null) return;
        if (clientIndex >= 0 && clientIndex < clients.size()) {
            lockClients.acquireUninterruptibly();
            SocketStream client = clients.get(clientIndex);
            if (client != null) {
                client.close();
                clients.set(clientIndex, null);
            }
            lockClients.release();
        }
    }
    public void close() {
        finish();
        if (clients == null) return;
        for (SocketStream client : clients) {
            if (client != null) client.close();
        }
        clients = null;
    }

    protected int maxClientNumber() {
        return -1;
    }
    protected void onConnect(SocketStream client) {
    }
    protected void onArrayed(int clientIndex) {
    }

}
