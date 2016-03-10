package maniac.lee.jumper.tool;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lipeng on 16/3/10.
 */
public enum UsablePortScanner {
    instance;

    private static Set<Integer> usedPort = Sets.newConcurrentHashSet();
    private static final AtomicInteger startPort = new AtomicInteger(2000);

    public synchronized int findUnusedPort() {
        final int startingPort = startPort.get();
        final int endingPort = 65535;
        for (int port = startingPort; port < endingPort; port++) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                startPort.set(port + 1);
                return port;
            } catch (IOException e) {
                System.out.println("Port " + port + "is currently in use, retrying port " + port + 1);
            } finally {
                // Clean up
                if (serverSocket != null) try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to close socket on port" + port, e);
                }
            }
        }

        throw new RuntimeException("Unable to find open port between " + startingPort + " and " + endingPort);
    }
}
