package detail.date;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 만약 Controller 단위가 아닌, 공통된 Exception 을 처리하고 싶으면 아래 ControllerAdvice 를 사용한다.
// Controller 단위가 더 우선순위가 높기 때문에, 없을떄만 적용된다.
@ControllerAdvice("spring")
// 지정한 범위에 공통으로 지정할 수 있다. 위의 spring 은 "spring" 패키지 아래의 모든 Controller 에 적용된다.
//  - 범위지정 매개변수는 아래와 같다.
//      - value / basePackages (String[]) : Controller 가 속할 기준 패키지
//      - annotations (Class<? extends Annotation>[]) : 특정 어노테이션 붙은 애들에 적용
//      - assignableTypes (Class<?>[]) : 특정 Type 혹은 하위 Type Controller 대상.
//  결국 value 나 annotations 를 사용하겠네.

// 마찬가지로 사용 시 Bean 등록 해야한다.
public class CommonExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(){
        // Exception Handler 의 매개변수 
        //   Parameter:
        //      - HttpServletRequest
        //      - HttpServletResponse
        //      - HttpSession
        //      - Model
        //      - Exception
        //  Returns:
        //      - ModelAndView
        //      - String : view 이름
        //      - 임의객체 (@ResponseBody 사용한 경우)
        //      - ResponseEntity
        return "error/commonException";
    }
}
