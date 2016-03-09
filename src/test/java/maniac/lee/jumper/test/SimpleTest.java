package maniac.lee.jumper.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import maniac.lee.jumper.ssh.SSHConfig;
import org.junit.Test;

/**
 * Created by lipeng on 16/3/7.
 */
public class SimpleTest {

    @Test
    public void sdf(){
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setHost("sdf");
        sshConfig.setPassword("sd");
        sshConfig.setRsaFilePath("sd");
        sshConfig.setUser("xxx");
        System.out.println(JSON.toJSONString(sshConfig, SerializerFeature.PrettyFormat));
    }
}
