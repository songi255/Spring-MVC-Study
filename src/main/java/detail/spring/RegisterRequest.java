package detail.spring;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank; // deprecate 된듯? 일단은 이렇게 적겠다.
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class RegisterRequest {
	/* Bean Validation
		Bean Validation 스펙에는 @Valid, @NotNull, @Digits, @Size 등 정의되어있어, Validator 작성 없이 이것만으로 검증할 수 있다.
			- 참고로 Bean Validation 2.0 을 JSR 380 이라고 한다. Java Specification Request 는 Java spec 을 기술한 문서를 말한다.
		우선 관련 의존설정을 pom.xml 에 해야 한다.

		이후, Bean Validation 을 수행할 수 있는 OptionalValidatorFactoryBean 클래스를 Bean 으로 등록한다.
			- @EnableWebMvc 적용했다면 자동으로 Optional~~ 를 Global Validator 로 등록하므로(Default) 추가로 할 건 없다.
			- 만약 Global Validator 를 따로 등록했다면 그걸 사용하므로, 해제해주도록 하자.
			- 참고로 기본 Error message 를 제공한다. 기본 메시지 대신 정의해서 사용하고 싶다면 label.properties 를 참고하자!
	*/

	/* 주요 Annotaion 정리
	아래는 null 도 유효하다고 판단한다. 필수입력값은 NotNull 과 Size(없으면 빈문자열 "" 을 통과시켜버린다. min = 1 로 라도 설정해야 함) 를 함께 사용해야 한다.
		- @AssertTrue / False : true / false 가 맞는지 확인.
		- @DecimalMax / Min : <= / >= 검사한다. (정수, charSequence)
			- String value
			- boolean inclusive
		- @Max / Min : > / < 검사 (정수만)
			- long value : 값이 정수타입으로 들어오는 것만 Decimal 과 다르다. 둘이 큰 차이는 없는듯. (어차피 Decimal 도 정수 지원한다.)
		- @Digits : 자릿수가 지정크기 아래인지 검사 (CharSequence / 정수)
			- int integer : 최대 정수자릿수
			- int fraction : 최대 소숫점 자릿수
		- @Size : 길이나 크기가 지정 범위 안인지? (charSequence / Collection / Map / Array 등 지원)
			- int min : 최소크기, 기본 0
			- int max : 최대크기, 기본 Integer.Max
		- @Null / @NotNull
		- @Pattern (charSequence)
			- String regexp

	Bean Validation 2.0 에서 추가된 것들 (Spring 5 지원)
		- @NotEmpty : null 아니고, 크기 0 이상 (Collection, CharSequence)
		- @NotBlack : notnull, 공백 아닌 문자 최소 1개 이상 (CharSequence)
		- @Positive/OrZero / @Negative/OrZero
		- @Email
		- @Future/OrPresent (Time 관련)
		- @Past/OrPresent
	*/

	// 아래 Bean Validation 을 적용한 모습.
	@NotBlank
	@Email
	private String email;
	@Size(min = 6)
	private String password;
	@NotEmpty
	private String confirmPassword;
	@NotEmpty
	private String name;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPasswordEqualToConfirmPassword() {
		return password.equals(confirmPassword);
	}
}
