package detail.controller;

import detail.spring.WrongIdPasswordException;
import detail.validation.RegisterRequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import detail.spring.DuplicateMemberException;
import detail.spring.MemberRegisterService;
import detail.spring.RegisterRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
// 아래처럼 Request Mapping 을 위에 적어주면, 해당경로를 공통으로 가지는 하위 router 를 적을 수 있다.
@RequestMapping("/register")
public class RegisterController {

    @RequestMapping("/step1") // 이렇게 하면 최종 routing 경로는 /register/step1 이 된다.
    // 참고로, Get, Post Mapping 이 정의되지 않은 경우, RequestMapping 이 있으면 둘 다 여기서 처리한다.
    // 예전에는 method 속성으로 GET, POST 를 분류했지만, 지금은 알다시피 전용 어노테이션이 있다.
    public String handleStep1(){
        return "register/step1";
    }

    // 요청 parameter 사용
    @PostMapping("/step2")
    public String handleStep2(HttpServletRequest request){ // 이렇게 request 객체를 매개변수로 요청해서 사용하는 방법이 1,
        String agreeParam = request.getParameter("agree");
        if (agreeParam == null || !agreeParam.equals("true")){
            return "register/step1";
        }else return "register/step2";
    }

    // @RequestParam 은 param 개수가 몇개 없을 떄 사용하면 좋다. param 수가 많다면 command 객체를 사용하도록 한다.
    @PostMapping("/step3")
    public String handleStep3(@RequestParam(value = "agree", defaultValue = "false") Boolean agree, Model model){ // JSP 에 값을 주기 위해 Model 도 받앗다.
        // defaultValue는 parameter 가 안왔을때 기본값이다. (지정안하면 값이 없다.)
        // required 도 있는데, 기본값은 true 이다. 이때 값이 없으면 Exception 된다.
        // 또한 Spring MVC에서 문자열을 자동 형변환 해준 것을 볼 수 있다.
        model.addAttribute("registerRequest", new RegisterRequest()); // form:form 을 사용하기 위해 command 객체를 넣어주는 모습이다.
        return agree ? "regitster/step1" : "register/step2";
    }

    // redirect
    // post 요청이 get 으로 온다거나, 등등.. 잘못 온 경우, redirect 하는 것이 더 나을수도 있다.
    @GetMapping("/step2")
    public String handleStep2Get(){
        return "redirect:/register/step1"; // 이렇게 redirect:경로 하면 된다.
        // 참고로 redirect: 후에 / 가 없으면 현재 경로를 기준으로 상대경로를 사용한다.
        // 예를 들어, 여기서는 step2 를 기준으로 같은 level 로 사용되므로, redirect:step1 으로 쓰면 동일해진다.
    }

    private MemberRegisterService memberRegisterService;
    public void setMemberRegisterService(MemberRegisterService memberRegisterService){
        this.memberRegisterService = memberRegisterService;
    }

    // command 객체
    // param 값들을 field 로 가지고 있는 객체를 command 객체라고 한다.
    // 예를 들어, param key 가 name 이면 command 객체는 name 과 setName() 을 가지고 있으면 된다. 그럼 Spring 이 알아서 넣어준다.
    @PostMapping("/step4")
    public String handleStep3(@ModelAttribute("formData") RegisterRequest regReq, Errors errors){ // Command 객체를 매개변수로 넣어서, 여기에 담도록 요청했다!
        // @ModelAttribute 는 필수가 아니다. 첫글자만 소문자로 바꾼 커맨드객체가 View에 전달되는데, 이 이름을 바꿀 수 있다.


        // Validator 를 사용해서 커멘드를 검증한 모습. 이를 위해서 매개변수에 Errors 를 받았다.
        // 위와 같이, Command 객체 바로 뒤에 Errors 객체를 받도록 하면 연결해서 전달된다. (순서를 바꾸면 Exception 발생)
        // 참고로 Errors 를 상속한 BindingResult 를 받게 해도 된다.
        new RegisterRequestValidator().validate(regReq, errors);
        if(errors.hasErrors()){
            return "register/step2";
        }

        try {
            memberRegisterService.regist(regReq); // 이렇게 Command 객체 단위로 넘겨주면 깔끔해진다.
            return "register/step3";
        }catch (DuplicateMemberException ex){
            errors.rejectValue("email", "duplicate"); // Errors 객체를 사용해서 valid 없이 오류정보를 넘겨줄 수도 있다!
            return "register/step2";
        }catch (WrongIdPasswordException ex){
            // ID와 Password 가 매치되지 않는, 즉 특정 프로퍼티가 아닌 Command 객체 자체가 잘못 된 경우에는 reject() 를 사용한다
            // 이런경우, ID가 잘못된건가? pass 가 잘못된건가? 아니 걍 command 자체가 잘못된거다.
            // Global Error 라고 부르며, 객체 자체에 에러코드가 부여된다.
            errors.reject("notMatchingIdPassword");
            return "login/loginForm";
        }
    }

    // @Valid 를 사용하여 Global Validator 바로 적용해버리기
    @PostMapping("/step5")
    public String handleStep5(@Valid RegisterRequest regReq, Errors errors){
        // 검증 결과는 같이 준 errors 에 담긴다. 검증은 요청 메서드 실행 전에 적용된다.
        // 즉, 메서드 안에서는 errors 만 확인하면 된다.
        // Errors 매개변수가 없으면 400 검증실패 에러를 응답해버린다. 주의하자.
        if (errors.hasErrors()) return "error!";
        return "anyway";
    }

    // 아래는 Controller 범위 Validator 를 적용하는 방법이다.
    // @InitBinder 가 붙은 메서드는 Controller 요청 메서드 실행 전 매번 실행된다.
    // @InitBinder 에 전달되는 WebDataBinder 에는 내부적으로 Validator 목록을 갖는다. 여기엔 Global Validator 도 포함되어있따.
    @InitBinder
    protected void InitBinder(WebDataBinder binder){
        // setValidator 는 덮어쓰기 해버린다. 즉, global Validator 대신 특정 Validator 를 사용하게 된다.
        // 만약, Global 도 실행하고, 특정 Validator 도 실행하고 싶다면 set 대신 add 를 사용하면 된다.
        binder.setValidator(new RegisterRequestValidator());
    }

    // 어노테이션을 이용해서, Command 객체에 검증을 바로 적용할 수 있다. RegisterRequest 를 참고하자!

    // 참고로, Get과 Post 를 활용해서 다음과 같이 구현할 수 있다.
    // Get 으로 login 하면 form 보여주기를, Post 로 login 하면 로그인 처리를 하도록 할 수 있다.
}
