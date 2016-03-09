package maniac.lee.jumper.ssh.impl;


import maniac.lee.jumper.ssh.ISSHProxyClient;
import maniac.lee.jumper.ssh.SSHConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Created by lipeng on 15/11/3.
 */
public class SSHProxyClient extends SSHClient implements ISSHProxyClient {

    InetSocketAddress proxyAddress;

    public static SSHProxyClient from(SSHConfig sshConfig) throws Exception {
        SSHProxyClient re = new SSHProxyClient();
        re.setSSH(sshConfig);
        return re;
    }

    public void proxyRemote(int bindPort, String remoteHost, int remotePort) throws Exception {
        int assigned_port = getSession().setPortForwardingL(bindPort, remoteHost, remotePort);
        proxyAddress = new InetSocketAddress("127.0.0.1", assigned_port);
    }

    @Override
    public void proxyRemote(String remoteHost, int remotePort) throws Exception {
        proxyRemote(findUnusedPort(), remoteHost, remotePort);
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
