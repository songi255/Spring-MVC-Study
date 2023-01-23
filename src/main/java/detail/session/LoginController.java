package detail.session;

import detail.spring.WrongIdPasswordException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    private AuthService authService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /* HttpSession
        로그인 유지는 어떻게 구현할까? 크게 Session 과 Cookie 로 구분된다.
        외부 DB에 Session Data 를 보관하는 것도 큰 틀에서 보면 2가지로 나뉘는 것이다.

        우선 HttpSession 을 사용해보자. 다음 중 하나의 방법을 사용하면 된다.
            - 요청 메서드에 HttpSession 파라미터 추가
                - 없으면 무조건 생성해서 전달한다.
            - 요청 메서드에 HttpServletRequest 파라미터 추가 + getHttpSession();
                - 1번째 방법과 다른것은, 항상 생성하지 않고 필요한 시점에만 생성할 수 있다.
    */

    @GetMapping
    public String form(LoginCommand loginCommand, HttpSession httpSession,
                       @CookieValue(value = "REMEMBER", required = false) Cookie rCookie){
        // Cookie 사용하기 위해서는 @CookieValue 를 사용하면 된다.
        //  value 는 Cookie 이름
        //  required 는 해당 이름 쿠키가 존재하지 않을 수도 있다면 선택한다. (기본값은 true)
        //    - true 일떄 없다면 Exception 발생한다.
        if (rCookie != null){
            loginCommand.setEmail(rCookie.getValue());
            loginCommand.setRememberEmail(true);
        }
        return "login/loginFrom";
    }

    @PostMapping
    public String submit(LoginCommand loginCommand, Errors errors, HttpSession session,
                         HttpServletResponse response){ // Cookie 생성을 위한 매개변수
        new LoginCommandValidator().validate(loginCommand, errors);
        if (errors.hasErrors()){
            return "login/loginForm";
        }
        try {
            AuthInfo authInfo = authService.authenticate(
                    loginCommand.getEmail(),
                    loginCommand.getPassword()
            );

            session.setAttribute("authInfo", authInfo); // 인증 성공 시 session 에 인증정보 담기

            // Login 성공시 실제로 브라우저에 Cookie 를 생성하도록 한다.
            Cookie rememberCookie = new Cookie("REMEMBER", loginCommand.getEmail());
            // 예제이기 때문에, Email 을 평문 그대로 저장했다. 실제 서비스에서는 암호화해서 보안을 높여야 한다.
            rememberCookie.setPath("/");
            if (loginCommand.isRememberEmail()){
                rememberCookie.setMaxAge(60 * 60 * 24 * 30);
            } else{
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);

            return "login/loginSuccess";
        }catch (WrongIdPasswordException e){
            errors.reject("idPPasswordNotMatching");
            return "login/loginForm";
        }
    }


}
