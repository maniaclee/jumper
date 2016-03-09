package maniac.lee.jumper.ssh.impl;

import maniac.lee.jumper.ssh.SSHConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 16/3/8.
 */
public class Jumper extends JumperClient {

    private Pattern p = Pattern.compile("//(\\S+):(\\d+)/");

    private int bindLocalPort;

    public static Jumper from(SSHConfig sshConfig) throws Exception {
        Jumper jumper = new Jumper();
        jumper.setSSH(sshConfig);
        return jumper;
    }


    public String setUpDbUrl(String url) throws Exception {
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            String ip = matcher.group(1);
            String port = matcher.group(2);
            Validate.isTrue(StringUtils.isNoneBlank(ip) && StringUtils.isNoneBlank(port), "can't parse datasource url:" + url);

            proxyRemote(ip, Integer.parseInt(port), bindLocalPort);
            InetSocketAddress proxyAddress = getProxyAddress();
            url = replace(url, matcher.start(2), matcher.end(2), String.valueOf(proxyAddress.getPort()));
            url = replace(url, matcher.start(1), matcher.end(1), proxyAddress.getHostName());
        }
        return url;
    }

    private String replace(String s, int start, int end, String to) {
        return s.substring(0, start) + to + s.substring(end);
    }

    public int getBindLocalPort() {
        return bindLocalPort;
    }

    public void setBindLocalPort(int bindLocalPort) {
        this.bindLocalPort = bindLocalPort;
    }
}
