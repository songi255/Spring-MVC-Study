package detail.spring;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class Member {

	private Long id;
	private String email;
	@JsonIgnore // 이 Annotation 을 이용하여 Json 직렬화에서 제외했다.
	private String password;
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING) // 이 어노테이션은 ISO-8601 형식으로 날짜를 변환한다.
	//@JsonFormat(pattern = "yyyyMMddHHmmss") // 특정 형식으로 바꾸고 싶으면 pattern 을 사용한다.
	// pattern 은 DateTimeFormatter나 SimpleDateFormat Class 의 패턴을 따른다.
	private LocalDateTime registerDateTime;

	public Member(String email, String password, 
			String name, LocalDateTime regDateTime) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.registerDateTime = regDateTime;
	}

	void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getRegisterDateTime() {
		return registerDateTime;
	}

	public void changePassword(String oldPassword, String newPassword) {
		if (!password.equals(oldPassword))
			throw new WrongIdPasswordException();
		this.password = newPassword;
	}

	public boolean matchPassword(String password){
		return this.password.equals(password);
	}

}
