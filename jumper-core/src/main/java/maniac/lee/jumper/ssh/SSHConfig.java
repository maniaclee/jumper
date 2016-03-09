package maniac.lee.jumper.ssh;

import java.io.Serializable;

/**
 * Created by lipeng on 15/11/3.
 */
public class SSHConfig  implements Serializable{

    private String host;
    private String user;
    private String password;
    private String rsaFilePath;

    public SSHConfig() {
    }

    public SSHConfig(String host, String user, String password, String rsaFilePath) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.rsaFilePath = rsaFilePath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getRsaFilePath() {
        return rsaFilePath;
    }

    public void setRsaFilePath(String rsaFilePath) {
        this.rsaFilePath = rsaFilePath;
    }

    @Override
    public String toString() {
        return "SSHConfig{" +
                "host='" + host + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", rsaFilePath='" + rsaFilePath + '\'' +
                '}';
    }
}
