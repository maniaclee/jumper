package psyco.jumper.ssh.impl;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import psyco.jumper.ssh.ISSHClient;
import psyco.jumper.ssh.SSHConfig;

import java.util.Hashtable;

/**
 * Created by lipeng on 15/11/2.
 */
public class SSHClient implements ISSHClient {
    Session session;
    String ssh_user;
    String ssh_server;
    String ssh_password;
    String ssh_ras_file_path;

    public void setSSH(String sshHost, String userName, String password, String rsaFilePath) throws Exception {
        this.ssh_server = sshHost;
        this.ssh_user = userName;
        this.ssh_password = password;
        this.ssh_ras_file_path = rsaFilePath;

        final JSch jsch = new JSch();
        session = jsch.getSession(ssh_user, ssh_server, 22);
        session.setPassword(ssh_password);

        final Hashtable config = new Hashtable();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        jsch.addIdentity(ssh_ras_file_path);
    }

    @Override
    public void setSSH(SSHConfig sshConfig) throws Exception {
        setSSH(sshConfig.getHost(),sshConfig.getUser(),sshConfig.getPassword(),sshConfig.getRsaFilePath());
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
