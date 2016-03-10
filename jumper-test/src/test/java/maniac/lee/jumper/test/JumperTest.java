package maniac.lee.jumper.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Test
    public void jumper() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from TG_ThirdPartyPaymentAudit where referId  = 294607482");
        System.out.println(list);
    }
}
