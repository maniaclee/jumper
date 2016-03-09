package maniac.lee.jumper.spring;

import maniac.lee.jumper.ssh.SSHConfig;
import maniac.lee.jumper.ssh.impl.Jumper;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.PreDestroy;

/**
 * Created by peng on 16/3/9.
 */
public class JumperFactoryBean implements FactoryBean<Jumper> {

    private SSHConfig sshConfig;
    private Jumper client;
    private int bindPort;

    private JumperFactoryBean(SSHConfig sshConfig) throws Exception {
        this.sshConfig = sshConfig;
        Validate.notNull(sshConfig, "ssh config is required.");
        client = Jumper.from(sshConfig);
        client.setBindLocalPort(bindPort);
        /** start the connection first */
        client.start();
    }

    public static JumperFactoryBean createDataSourceJumperProxy(SSHConfig sshConfig) throws Exception {
        return new JumperFactoryBean(sshConfig);
    }


    @Override
    public Jumper getObject() throws Exception {
        return client;
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
        return Jumper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public SSHConfig getSshConfig() {
        return sshConfig;
    }

    public void setSshConfig(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
    }

    public int getBindPort() {
        return bindPort;
    }

    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }
}

