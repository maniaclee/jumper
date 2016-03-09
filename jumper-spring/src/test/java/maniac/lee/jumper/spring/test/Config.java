package maniac.lee.jumper.spring.test;

import maniac.lee.jumper.spring.JumperFactoryBean;
import maniac.lee.jumper.ssh.SSHConfig;
import maniac.lee.jumper.ssh.impl.Jumper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by peng on 16/3/10.
 */

@Configuration
public class Config {

    @Bean
    public SSHConfig sshConfig() {
        return new SSHConfig();
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
        dataSource.setUrl(jumper.setUpDbUrl(""));
        return dataSource;
    }
}


