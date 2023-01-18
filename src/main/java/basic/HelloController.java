package basic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    // GetMapping 의 값은 Servlet Context 경로를 기준으로 한다. 아마 여기선 springmvcstudy/ 가 될 것 같다...
    @GetMapping("/hello")
    public String hello(
            Model model, // model 은 처리결과를 view 에 전달하는 데 사용된다.
            @RequestParam(value = "name", required = false) String name){ // 요청의 body에 담겨오는 param 을 읽을 수 있다.
        // addAttribute 등으로 Model 에 등록을 해주면, Spring Framework 가 HttpServletRequest 객체에 옮겨준다. 그래서 JSP에서 접근이 가능하다.
        model.addAttribute("greeting", "안녕하세요" + name);
        return "hello";
        // 이렇게 뷰 이름을 return 했을 때 view 구현을 찾아주는 것이 ViewResolver 의 역할이다.
    }
}
