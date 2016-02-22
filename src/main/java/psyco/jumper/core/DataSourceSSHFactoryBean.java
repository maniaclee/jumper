package psyco.jumper.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import psyco.jumper.ssh.SSHConfig;
import psyco.jumper.ssh.impl.SSHProxyClient;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 15/11/4.
 */
public class DataSourceSSHFactoryBean implements FactoryBean<DataSource>, ApplicationListener<ApplicationEvent> {

    private SSHConfig sshConfig;
    private SSHProxyClient client;
    private String url;
    private String user;
    private String password;
    private String jdbcDriver = "com.mysql.jdbc.Driver";
    private int bindPort;


    @Override
    public DataSource getObject() throws Exception {
        if (sshConfig == null)
            throw new Exception("ssh config is required.");
        InetSocketAddress addr = addr(url);
        if (addr == null)
            throw new Exception("invalid url");
        client = SSHProxyClient.from(sshConfig);
        System.out.println("fuck:\t" + bindPort);
        client.start();
        if (bindPort > 0)
            client.setProxySSH(bindPort, formatIp(addr.getHostName()), addr.getPort());
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setPassword(password);
        /** proxy url */
        dataSource.setUrl(url.replaceAll("//[^/]+/", "//" + formatIp(client.getProxyAddress().toString()) + "/"));
        dataSource.setUsername(user);
        return dataSource;
    }

    private String formatIp(String s) {
        return s.replace("/", "");
    }

    public InetSocketAddress addr(String s) {
        Matcher m = Pattern.compile("//(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)/").matcher(s);
        if (m.find()) {
            return new InetSocketAddress(m.group(1), Integer.parseInt(m.group(2)));
        }
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SSHConfig getSshConfig() {
        return sshConfig;
    }

    public void setSshConfig(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
    }

    public SSHProxyClient getClient() {
        return client;
    }

    public void setClient(SSHProxyClient client) {
        this.client = client;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public int getBindPort() {
        return bindPort;
    }

    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }
}
