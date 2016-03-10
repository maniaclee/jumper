package maniac.lee.jumper.test;

import com.alibaba.fastjson.JSON;
import maniac.lee.jumper.ssh.SSHConfig;
import maniac.lee.jumper.ssh.impl.Jumper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 16/3/10.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
@ContextConfiguration(classes = Config.class)
public class JumperTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * demo of jumper client , the basic one
     */
    @Test
    public void jumperCore() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("json.ignore"));
        Jumper client = Jumper.from(JSON.parseObject(json, SSHConfig.class));

        String url = client.setUpDbUrl("jdbc:mysql://10.1.101.158:3306/PCTChannel?user=cobain.li&password=dp!@4REjjBdcz");
        client.start();

        printTable(DriverManager.getConnection(url));

        client.stop();
    }

    /***
     * demo of datasource
     */
    @Test
    public void printTable() throws SQLException {
        printTable(jdbcTemplate.getDataSource().getConnection());
    }

    public void printTable(Connection connection) throws SQLException {
        java.sql.DatabaseMetaData metadata = connection.getMetaData();

        // Get all the tables and views
        String[] tableType = {"TABLE", "VIEW"};
        java.sql.ResultSet tables = metadata.getTables(null, null, "%", tableType);
        while (tables.next()) {
            String tableName = tables.getString(3);
            System.out.println("--------" + tableName);
            java.sql.ResultSet columns = metadata.getColumns(null, tableName, null, null);

            String columnName;
            while (columns.next()) {
                columnName = columns.getString(4);
                int dataType = columns.getInt(5);
                System.out.println(columnName);
            }
        }
    }

    @Test
    public void jumper() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from TG_ThirdPartyPaymentAudit where referId  = 294607482");
        System.out.println(list);
    }


}
