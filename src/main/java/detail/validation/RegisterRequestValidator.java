package detail.validation;

import detail.spring.RegisterRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 객체 검증
    Spring은 Command 검증, 에러코드에 저장 -> JSP에서 메시지 출력 을 지원한다.

    검증 : Validator, Errors 두개를 이용해서 검증한다.
    Validator 를 정의하고, Controller 에서 사용한다.
*/

public class RegisterRequestValidator  implements Validator { // 우선 Validator 를 구현한다.
    private static final String emailRegExp = "^[_A-Zz-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;

    public RegisterRequestValidator() {
        this.pattern = Pattern.compile(emailRegExp);
    }

    // Validator 가 검증할 수 있는 타입인지 검사한다. Spring 이 호출한다.
    @Override
    public boolean supports(Class<?> clazz) {
        // RegisterRequest 를 검증하기 때문에, 이 타입으로 변환할 수 있는지 검사한다.
        return RegisterRequest.class.isAssignableFrom(clazz);
    }

    // supports() 라면, 검증 후 결과를 Errors 에 담는다.
    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequest regReq = (RegisterRequest) target;
        if (regReq.getEmail() == null || regReq.getEmail().trim().isEmpty()){
            errors.rejectValue("email", "required");
            // reject, rejectValue 등은 (에러코드, default 메시지) 를 사용했다.
            // 즉, 에러코드에 해당하는 message 는 원래 properties 파일에 규칙에 맞게 적으면 그걸 사용한다.
        }else{
            Matcher matcher = pattern.matcher(regReq.getEmail());
            if (!matcher.matches()){
                errors.rejectValue("email", "bad");
            }
        }

        // ValidationUtils 는 검증을 편하게 할 수 있는 유틸이다.
        // 여기서 보면 target은 전달하지 않았는데, Errors 에 내포되어 있기 떄문에 가능하다.(getFieldValue())
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
        ValidationUtils.rejectIfEmpty(errors, "password", "required");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "required");
        if (!regReq.getPassword().isEmpty()){
            if (!regReq.isPasswordEqualToConfirmPassword()){
                errors.rejectValue("confirmPassword", "nomatch");
            }
        }
    }

    // @Valid 를 사용하기
    //  1. 설정 Class 에서 WebMvcConfigurer#getValidator() 가 Validator 구현객체를 return 하도록 하기
    //  2. 해당 Global Validator 를 사용할 Command 객체에 @Valid 붙이기.
    //  @Valid 를 사용하기 위해서는 javax.validation 의 validation-api 를 의존해야 한다. pom 에서 설정해주자.
    // 글로벌 범위 혹은 단일 Controller 범위로 적용할 수 있다.
}
