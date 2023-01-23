package detail.date;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ListCommand {
    // <input> 에서 String 으로 입력 될 날짜 데이터를 LocalDateTime 클래스로 매핑해준다.
    @DateTimeFormat(pattern = "yyyyMMddHH")
    private LocalDateTime from;
    @DateTimeFormat(pattern = "yyyyMMddHH")
    private LocalDateTime to;


    /* 그렇다면 누가 변환처리할까? WebDataBinder 이다. (Validator 에도 관여하지만, 값 변환에도 관여한다.)
        Spring 은 RequestMappingHandlerAdapter 를 중간에 둔 것을 기억할 것이다.
            - 이 Adapter 는 Command 객체로의 Mapping 을 위해 WebDataBinder 를 사용한다.
                - WebDataBinder 는 직접 변환하지는 않고, ConversionService 에 위임한다.
                    - <form:input /> 의 path 값을 String 으로 변환해서 value 값을 생성할 떄도 사용된다.
                - @EnableWebMvc 를 사용하면 DefaultFormattingConversionService 를 사용한다.




    */


    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
