package maniac.lee.jumper.test;

import com.alibaba.fastjson.JSON;
import maniac.lee.jumper.spring.JumperFactoryBean;
import maniac.lee.jumper.ssh.SSHConfig;
import maniac.lee.jumper.ssh.impl.Jumper;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by peng on 16/3/10.
 */

@Configuration
public class Config {

    @Bean
    public SSHConfig sshConfig() throws IOException {
        return JSON.parseObject(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("json.ignore")), SSHConfig.class);
    }

    @Bean
    public JumperFactoryBean sshdbUrlProxyClient(SSHConfig sshConfig) throws Exception {
        return JumperFactoryBean.createDataSourceJumperProxy(sshConfig);
    }


    @Bean
    public DataSource dataSource(Jumper jumper) throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        /** let Jumper to delegate the url */
        dataSource.setUrl(jumper.setUpDbUrl("jdbc:mysql://10.1.101.158:3306/PCTChannel?user=cobain.li&password=dp!@4REjjBdcz"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}


