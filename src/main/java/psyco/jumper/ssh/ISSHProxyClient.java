package psyco.jumper.ssh;

import java.net.InetSocketAddress;

/**
 * Created by lipeng on 15/11/3.
 */
public interface ISSHProxyClient extends ISSHClient {

    /***
     * proxy the remote host
     *
     * @param bindPort
     * @param remoteHost
     * @param remotePort
     * @return
     */
    void setProxySSH(int bindPort, String remoteHost, int remotePort) throws Exception;
    void setProxySSH(String remoteHost, int remotePort) throws Exception;

    InetSocketAddress getProxyAddress();
}
