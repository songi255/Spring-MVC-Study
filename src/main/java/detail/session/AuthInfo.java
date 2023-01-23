package detail.session;

public class AuthInfo {
    // login 인증 상태 정보를 Session 에 보관할 때 사용할 Class
    private Long id;
    private String email;
    private String name;

    public AuthInfo(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
