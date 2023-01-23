package detail.profile;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/* 개발에 실제 운영중인 DB 를 이용할 수는 없다. 개발용 DB를 따로 사용해야 한다.
    실수를 방지하기 위해서, 개발용 설정과 서비스용 설정을 처음부터 구분해서 작성할 수 있다. 그 기능이 바로 Profile 이다.

    Profile 은 논리적인 이름으로, 설정 집합에 지정할 수 있다.
    해당 논리적 이름을 선택해서 Container 를 초기화할 수 있다. ("dev" 와 "real" 중 "dev" 프로필을 사용하여 개발)

    Profile 선택은 어디서 할까? context 선택 시, context.setActiveProfiles("dev") 를 하면 된다.
        - Environment 는 실행환경 설정에 사용되는 Class 이다.
        - 주의할 점은, context.register(Aconfig.class, Bconfig.class....) 로 설정정보 전달하기 전에 지정해야 한다.
        - 두개 이상 활성화하고 싶다면, ("dev", "mysql") 같이 , 로 작성한다.

    또 다른 방법은 spring.profiles.active 라는 java의 System Property 에 직접 등록해버리는 것이다.
        - 명령행 사용 시 : java -Dspring.profiles.active=dev,mysql main.Main
        - java 코드 사용 시 : System.setProperty("spring.profiles.active", "dev");
    
    OS 의 spring.profiles.active 환경변수에 때려박아도 된다 ㄷㄷ
        - 우선순위 : setActiveProfiles() > java System Property > OS 환경변수

    web.xml 에서도 초기화 파라미터를 사용해서 설정할 수 있다.

    외부 properties 파일을 사용해서 등록할 수도 있다. db.properties 와 PropertyConfig 파일을 참고하자!
        - PropertySourcesPlaceholderConfigurer Bean 등록 후, @Value 로 사용하면 된다.
*/
@Configuration
@Profile("dev")
// @Profile("real,test") // 이렇게 다중 설정도 가능하다.
// @Profile("!real") // Not 사용도 가능하다. 보통 ! 프로필 사용하지 않을 때 Default 설정용도로 사용한다.
public class DsDevConfig {
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
