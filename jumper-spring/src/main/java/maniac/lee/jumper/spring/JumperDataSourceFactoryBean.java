package maniac.lee.jumper.spring;

import maniac.lee.jumper.ssh.SSHConfig;
import maniac.lee.jumper.ssh.impl.Jumper;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

/**
 * Created by lipeng on 15/11/4.
 */
public class JumperDataSourceFactoryBean implements FactoryBean<DataSource>, ApplicationListener<ApplicationEvent> {

    private SSHConfig sshConfig;
    private Jumper client;
    private String url;
    private String user;
    private String password;
    private String jdbcDriver = "com.mysql.jdbc.Driver";
    /**
     * optional
     */
    private int bindPort;


    @Override
    public DataSource getObject() throws Exception {
        Validate.notNull(sshConfig, "ssh config is required.");
        client = Jumper.from(sshConfig);
        client.setBindLocalPort(bindPort);
        String urlProxy = client.setUpDbUrl(url);
        /** start the connection first */
        client.start();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setPassword(password);
        dataSource.setUsername(user);
        /** proxy url */
        dataSource.setUrl(urlProxy);

        return dataSource;
    }

    @PreDestroy
    public void close() {
        try {
            client.stop();
        } catch (Exception e) {
            throw new RuntimeException("failed to close client", e);
        }
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
