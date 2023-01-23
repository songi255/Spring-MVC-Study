package detail.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertyConfig {

    // 주의깊게 볼 것은, Bean 설정 method 가 static 이다. 이는 특수목적의 Bean 이기 때문이다. static이 아니면 정상동작하지 않는다.
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations( // setLocations() 를 사용해서 properties 파일들을 전달한다.
                new ClassPathResource("db.properties"), // Resource 타입은 Spring 에서 자원표현할 때 사용한다.
                new ClassPathResource("info.properties") // 대표적으로 ClassPath 랑 FileSystem 이 있다.
                // 지금처럼 ClassPath 에 있는 경우에는 ClassPathResource 사용하면 된다.
        );
        return configurer;
    }

    // 이 Bean 은 그저 properties 파일을 읽어올 뿐이다. 이를 @Value 애노테이션을 사용하면 해당하는 값을 필드에 주입할 수 있다.
    // DsConfigWithProp 에서 설정 예시가 있다!

    // 참고로 @Value 사용을 보면 알겠지만, 설정에만 쓰는 게 아니고 Bean 에 주입해서 사용할 수도 있다.
    // Info 클래스를 확인해보자!
}
