# jumper

###通过跳板机/堡垒机连接远程数据库
典型场景为连接生产环境的datasource

如果想将测试环境的datasource替换为线上环境的datasource，可以用***jumper***代理一下

###代理目标datasource

首先需要配置跳板机：

```java
	@Bean
    public SSHConfig sshConfig() throws IOException {
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setHost("192.168.2.2");//跳板机ip
        sshConfig.setPort(33);//跳板机port
        sshConfig.setUser("user");
        sshConfig.setPassword("pwd");
        sshConfig.setRsaFilePath("~/.ssh/rsa");
        return sshConfig;
    }
```

然后使用jumper的Datasource FactoryBean，ok.

```java
 		@Bean
        public JumperDataSourceFactoryBean dataSource() throws IOException {
            JumperDataSourceFactoryBean fb = new JumperDataSourceFactoryBean();
            fb.setPassword("pwd");
            fb.setUser("maniac.lee");
            fb.setSshConfig(sshConfig());
            fb.setUrl("jdbc:mysql://10.1.111.222:3306/Db");
            return fb;
        }
```

如果使用自己的datasource，可以通过代理url的方式来:

```java
	//产生一个Jumper
	@Bean
    public JumperFactoryBean sshdbUrlProxyClient(SSHConfig sshConfig) throws Exception {
        return JumperFactoryBean.createDataSourceJumperProxy(sshConfig);
    }


    @Bean
    public DataSource dataSource1(Jumper jumper) throws Exception {
        DataSource dataSource = null;//你的datasource
        /** 用Jumper来代理原来的db url */
        dataSource.setUrl(jumper.setUpDbUrl("jdbc:mysql://10.1.111.222:3306/Db?user=user&password=pwd"));
        return dataSource;
    }
```

That's it.
