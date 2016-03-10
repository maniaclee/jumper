package maniac.lee.jumper.ssh.impl;


import maniac.lee.jumper.ssh.ISSHProxyClient;
import maniac.lee.jumper.ssh.SSHConfig;
import maniac.lee.jumper.tool.UsablePortScanner;

import java.net.InetSocketAddress;

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
            bindPort = UsablePortScanner.instance.findUnusedPort();
        int assigned_port = getSession().setPortForwardingL(bindPort, remoteHost, remotePort);
        proxyAddress = new InetSocketAddress("127.0.0.1", assigned_port);
    }

    /***
     * use this after proxyRemote
     */
    @Override
    public InetSocketAddress getProxyAddress() {
        return proxyAddress;
    }


}
