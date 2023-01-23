package detail.session;

public class LoginCommand {
    /* Cookie 사용
        사용자 편의를 위해 아이디 기억하기 기능을 제공하는 경우, 어떻게 구현할까? 바로 쿠키를 이용한다.
            - login Form 에 기억옵션 추가
            - 기억하기 선택했다면 login 성공 시 Cookie 에 email 추가
                - 이때 브라우저를 닫아도 삭제되지 않도록 유효시간 길게 설정
            - 이후 Login Form 에서는 Cookie 를 탐색하여 존재하면 입력폼에 삽입


    */

    private String email;
    private String password;
    private boolean rememberEmail;

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

    public boolean isRememberEmail() {
        return rememberEmail;
    }

    public void setRememberEmail(boolean rememberEmail) {
        this.rememberEmail = rememberEmail;
    }
}
