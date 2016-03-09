package maniac.lee.jumper.ssh.impl;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import maniac.lee.jumper.ssh.ISSHClient;
import maniac.lee.jumper.ssh.SSHConfig;

import java.util.Hashtable;

/**
 * Created by lipeng on 15/11/2.
 */
public class SSHClient implements ISSHClient {
    Session session;

    @Override
    public void setSSH(SSHConfig sshConfig) throws Exception {
        final JSch jsch = new JSch();
        session = jsch.getSession(sshConfig.getUser(), sshConfig.getHost(), sshConfig.getPort() <= 0 ? 22 : sshConfig.getPort());
        session.setPassword(sshConfig.getPassword());

        final Hashtable config = new Hashtable();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        jsch.addIdentity(sshConfig.getRsaFilePath());
    }

    public void start() throws Exception {
        if (session != null)
            session.connect();
    }

    public void stop() throws Exception {
        if (session != null)
            session.disconnect();
    }

    public Session getSession() {
        return session;
    }
}
