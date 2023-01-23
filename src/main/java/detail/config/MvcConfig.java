package detail.config;

import detail.validation.RegisterRequestValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/view/", ".jsp");
    }

    // 이 메서드는, 단순매핑을 전부 처리한다. 예를 들어, /main 으로 오면 main view 로 연결하는 것 같은 걸, Mapping 쓰지 않고 여기서 한줄로 처리 가능하다.
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/main").setViewName("main");
    }

    // messaging 을 사용하기 위해서 아래 Bean 을 추가해주자.
    @Bean
    public MessageSource messageSource(){ // bean 이름은 무조건 messageSource 여야 한다!!
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource(); // property file 을 사용하는 구현체이다.
        // 또한 java 의 resourceBundle 을 사용한다. 그래서 해당 파일이 classpath에 위치해야 한다.
        ms.setBasenames("message.label"); // resource/message/label.properties 를 읽어오게 된다!
        // resource 는 이미 classpath에 포함되어있기 떄문이다.
        // 또한, setBasenames는 가변인자로, 여러개를 전달할 수 있다.
        ms.setDefaultEncoding("UTF-8");
        return ms;

        // resource 폴더를 보면 label 종류가 3개이다. 무슨 의미일까?
        // 브라우저는 Accept-Language 헤더에 언어정보를 담아서 전송한다.
        // message 파일 이름에 _로케일문자 형식으로 붙이면 해당 언어를 자동으로 사용한다.
        // 매핑되는 locale 이 없으면 아무것도 없는 label 을 기본으로 사용하게 된다!

        // 실제로 MessageSource 인터페이스의 구현을 보면, getMessage() 는 Locale 을 인자로 받는다.
    }

    // Global Validator 설정하는 법 - 모든 Controller 에 적용된다.
    // 이를 사용하면 @Valid 를 사용할 수 있다.
    // 참고로 @Valid 는 Bean Validation API 에 포함되어있다. 의존 자체는 javax.validation 의 validation-api 이다.
    @Override
    public Validator getValidator() {
        return new RegisterRequestValidator();
    }
}
