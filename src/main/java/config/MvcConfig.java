package config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import detail.session.AuthCheckInterceptor;
import detail.validation.RegisterRequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
// Spring Mvc 활성화. 다양한 설정을 생성한다.
@EnableWebMvc
/* Spring MVC 를 사용하려면 다양한 구성요소를 설정해야 한다.
    처음부터 끝까지 직접 구성하면 설정이 매우 복잡해진다.(Spring 2, 3 에서 그랬음)
    Spring5 부터 이 애노테이션 덕분에 내부적으로 다양한 bean 설정을 추가해준다. (직접하면 수십줄 써야 한다.)

    그렇다면 WebMvcConfigurer 인터페이스는 뭘까?
    Spring MVC의 개별설정을 조정할 때 사용한다.
        - 예시로, 아래 configureDefaultServletHandling(), configureViewResolvers() 의 경우에도 마찬가지로, 직접하면 2~30줄이다.
        - @Controller 처리(handler mapping/adapter bean 등록) 를 자동화하기 위한 Class가 포함되어있다.
            - RequestMappingHandlerMapping
            - RequestMappingHandlerAdapter
        - 그리고 아래에 WebMvcConfigurer 를 구현했는데, 이 타입의 Bean 을 만들어서 Mvc 설정을 추가로 생성하는 기능도 담당한다.
            - 해당타입 Bean 의 메서드를 호출해서 설정하는 것이다. 그래서 아래에 보면 오버라이드를 했다.
            - 참고로 interface의 default method 를 사용한 method 들이고, 기본구현은 모두 공백이다. 그래서 설정안해주면 view 를 못찾는다.
*/
public class MvcConfig implements WebMvcConfigurer {


    // Default 는 "/" 경로 매핑을 의미한다.
    // DispatcherServlet 의 매핑을 "/" 로 줄 때, 리소스파일을 올바르게 처리하기 위한 설정을 한다.
    // 무슨말이냐면, web.xml 에서 dispatcher 의 매핑경로가 / 인데, 이런 경우, .jsp 는 resolver 가 처리하도록 했지만, 그 외 나머지 파일들은
    // dispatcher 가 처리해야 한다. 이에 관한 설정이다.
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // 아래 메서드는 2개의 Bean 을 등록한다.
        //  - DefaultServletHttpRequestHandler
        //      - Client 의 모든 요청을 WAS 가 제공하는 Default Servlet 에 전달한다.
        //  - SimpleUrlHandlerMapping
        //      - 모든경로 ("/**") 를 위 Handler 를 이용해서 처리하게 한다.
        //  물론 @EnableWebMvc 로 등록되는 RequestMappingHandlerMapping 이 Simple~~ 보다 우선순위가 높아서,
        //  아래 Default Servlet Handler 를 enable 할 경우, 등록되지 않은 모든 요청에 대해서 Default Servlet 이 담당하게 된다.
        configurer.enable();
    }

    // ViewResolver 설정.
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // registry.jsp(접두어, 접미사) 는 JSP 를 view 구현으로 등록하겠다는 뜻이다.
        // 이 pattern 으로 view 를 resolve 한다.
        registry.jsp("/WEB-INF/view/", ".jsp");
    }

    // 위 registry.jsp() 는 정확히 어떤 일을 할까? 바로 아래 설정 Bean 을 추가한다.
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("/WEB-INF/view/");
        vr.setSuffix(".jsp");
        return vr;
        // 이 InternalResource view reslover 는 InternalResourceView 를 찾아서 return 한다.
        // 이 View 는 request 속성에 전달받은 map 을 저장해서 해당경로의 JSP 를 실행한다. 이게 JSP 로의 전달경로인 것이다.
    } // 정리를 위해 적은것이지, 실제로는 자동생성되는 코드인것이다.

    // Dispatcher 설정을 하기 위해 web.xml 을 작성해주어야 한다. (요청을 받기위한 설정)


    // Global Validator 설정
    @Override
    public Validator getValidator() {
        // 사실 RegisterRequestValidator 는 global Validator 로는 적절하지 않다.
        return new RegisterRequestValidator();
    }


    // HandlerInterceptor 설정
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor())
                .addPathPatterns("/edit/**")
                .excludePathPatterns("/edit/help/**");
        // 경로는 Ant 패턴을 사용한다. 2개 이상의 경로는 ,로 구분하면 된다.
        // Ant Pattern
        //  * : 0개 이상의 글자
        //  ? : 1개 글자
        //  ** : 0개 이상의 폴더경로
    }

    @Bean AuthCheckInterceptor authCheckInterceptor(){
        return new AuthCheckInterceptor();
    }


    // 아래는 HttpMessageConverter 를 추가로 설정할 때 사용하는 메서드이다.
    // 이를 통해 뭘 할 수 있냐면, 모든 변환대상 날짜에 @JsonFormat 붙이는 대신 일괄변환할 수 있다.

    // Spring MVC 는 java 객체를 HTTP 응답으로 변환 시 HttpMessageConvertor 를 사용한다.
    //   - Jackson 을 사용한다면 MappingJackson2HttpMessageConvertor를 사용하고, Jaxb 를 사용할떄는...~~
    // 고로 convertor 를 새롭게 등록해서 날짜변환을 설정하면 굳이 @JsonFormat 하지 않아도 자동으로 변환된다.

    // @EnableWebMvc가 이미 한번 등록하는데, 이렇게 미리 등록된 애들을 converters 파라미터로 받는다.

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Spring 이 쉽게 쓰라고 제공하는 Builder 를 사용한다.
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .featuresToDisable(
                        // Unix TimeStamp 로 출력하는 기능이다. 이걸 해제한다.
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                        // 해제되면, ISO-8601 로 출력한다.
                )
                .simpleDateFormat("yyyyMMddHHmmss") // Date 클래스들에 원하는 형식을 쓰고싶으면 다음 행을 사용한다.
                // Date 에만 적용되기 때문에, 그 외의 것은 Type 별로 지정해서 사용하면 된다.
                .serializerByType(LocalDateTime.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        // converter List 에는 기존 Jackson 사용하는 것도 포함되있기 때문에, 먼저 적용하기 위해 0번 index 에 넣어준다.
        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));

        // 참고로 이렇게 적용해도 @JsonFormat 이 더 우선되기 때문에, 이걸 사용한다면 @JsonFormat 은 지워주자.
    }
}
