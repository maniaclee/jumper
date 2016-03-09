package maniac.lee.jumper.ssh.impl;


import maniac.lee.jumper.ssh.ISSHProxyClient;
import maniac.lee.jumper.ssh.SSHConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Created by lipeng on 15/11/3.
 */
public class JumperClient extends SSHClient implements ISSHProxyClient {

    InetSocketAddress proxyAddress;

    public static JumperClient from(SSHConfig sshConfig) throws Exception {
        JumperClient re = new JumperClient();
        re.setSSH(sshConfig);
        return re;
    }

    /**
     * jump to the remote server
     *
     * @param remoteHost
     * @param remotePort
     * @param bindPort   optional,the local port that the jumper uses
     * @throws Exception
     */
    @Override
    public void proxyRemote(String remoteHost, int remotePort, int bindPort) throws Exception {
        if (bindPort <= 0)
            bindPort = findUnusedPort();
        int assigned_port = getSession().setPortForwardingL(bindPort, remoteHost, remotePort);
        proxyAddress = new InetSocketAddress("127.0.0.1", assigned_port);
    }

    public InetSocketAddress getProxyAddress() {
        return proxyAddress;
    }

    int findUnusedPort() {
        final int startingPort = 3333;
        final int endingPort = 6000;
        for (int port = startingPort; port < endingPort; port++) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
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
