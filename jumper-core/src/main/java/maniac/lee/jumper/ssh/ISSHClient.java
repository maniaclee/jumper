package maniac.lee.jumper.ssh;

/**
 * Created by lipeng on 15/11/3.
 */
public interface ISSHClient extends IClient {

    void setSSH(SSHConfig sshConfig) throws Exception;
}
