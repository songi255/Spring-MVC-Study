package detail.profile;

import detail.spring.MemberDao;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// 중첩 클래스를 사용하여 Profile 설정을 한곳에 모을 수 있다.
@Configuration
public class MemberConfigWithProfile {
    @Autowired
    private DataSource dataSource;

    @Bean
    public MemberDao memberDao(){
        return new MemberDao(dataSource);
    }

    // 아래는 중첩 Class 를 이용하여 Profile 들을 설정했다. 주의할 점은 static class 이여야 한다.
    @Configuration
    @Profile("dev")
    public static class DsDevConfig{
        @Bean(destroyMethod = "close")
        public DataSource dataSource(){
            DataSource ds = new DataSource();
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUrl("jdbc:mysql://localhost/springwebstudy?characterEncoding=utf-8");
            ds.setUsername("sin");
            ds.setPassword("1234");
            ds.setInitialSize(2);
            ds.setMaxActive(10);
            ds.setTestWhileIdle(true);
            ds.setMinEvictableIdleTimeMillis(60000 * 3);
            ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
            return ds;
        }
    }

    @Configuration
    @Profile("real")
    public static class DsRealConfig{
        @Bean(destroyMethod = "close")
        public DataSource dataSource(){
            DataSource ds = new DataSource();
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUrl("jdbc:mysql://localhost/springwebstudy?characterEncoding=utf-8");
            ds.setUsername("sin");
            ds.setPassword("1234");
            ds.setInitialSize(2);
            ds.setMaxActive(10);
            ds.setTestWhileIdle(true);
            ds.setMinEvictableIdleTimeMillis(60000 * 3);
            ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
            return ds;
        }
    }
}
