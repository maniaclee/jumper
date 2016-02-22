package psyco.jumper.ssh;

/**
 * Created by lipeng on 15/11/3.
 */
public interface ISSHClient extends IClient {

    void setSSH(String sshHost, String userName, String password, String rsaFilePath) throws Exception;
    void setSSH(SSHConfig sshConfig) throws Exception;
}
