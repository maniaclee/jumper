package maniac.lee.jumper.ssh;

import java.net.InetSocketAddress;

/**
 * Created by lipeng on 15/11/3.
 */
public interface ISSHProxyClient extends ISSHClient {

    /***
     * proxy the remote host
     */
    void proxyRemote( String remoteHost, int remotePort,int bindPort) throws Exception;

    InetSocketAddress getProxyAddress();
}
