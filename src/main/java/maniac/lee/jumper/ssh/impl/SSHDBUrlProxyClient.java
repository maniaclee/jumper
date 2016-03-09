package maniac.lee.jumper.ssh.impl;

import maniac.lee.jumper.ssh.SSHConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 16/3/8.
 */
public class SSHDBUrlProxyClient extends SSHProxyClient {

    private Pattern p = Pattern.compile("//(\\S+):(\\d+)/");


    public static SSHDBUrlProxyClient from(SSHConfig sshConfig) throws Exception {
        SSHDBUrlProxyClient sshdbUrlProxyClient = new SSHDBUrlProxyClient();
        sshdbUrlProxyClient.setSSH(sshConfig);
        return sshdbUrlProxyClient;
    }

    //jdbc:mysql://localhost:3366/PCTChannel?user=cobain.li&password=dp!@4REjjBdcz
    public String setUpDbUrl(String url) throws Exception {
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            String ip = matcher.group(1);
            String port = matcher.group(2);
            assert StringUtils.isNoneBlank(ip) && StringUtils.isNoneBlank(port);
            System.out.println(ip);
            System.out.println(port);
            proxyRemote(ip, Integer.parseInt(port));
            InetSocketAddress proxyAddress = getProxyAddress();
            url = replace(url, matcher.start(2), matcher.end(2), String.valueOf(proxyAddress.getPort()));
            url = replace(url, matcher.start(1), matcher.end(1), proxyAddress.getHostName());
        }
        return url;
    }

    private String replace(String s, int start, int end, String to) {
        return s.substring(0, start) + to + s.substring(end);
    }

    @Test
    public void sdf() throws Exception {
        String s = "jdbc:mysql://10.1.1.3:3366/PCTChannel?user=cobain.li&password=dp!@4REjjBdcz";
        System.out.println(setUpDbUrl(s));
    }


}
