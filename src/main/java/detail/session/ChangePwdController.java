package detail.session;

import detail.spring.ChangePasswordService;
import detail.spring.WrongIdPasswordException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/edit/changePassword")
public class ChangePwdController {
    private ChangePasswordService changePasswordService;

    public void setChangePasswordService(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @GetMapping
    public String form(@ModelAttribute("command") ChangePwdCommand pwdCmd){
        return "edit/changePwdForm";
    }

    @PostMapping
    public String submit(@ModelAttribute("command") ChangePwdCommand pwdCmd, Errors errors, HttpSession session){
        new ChangePwdCommandValidator().validate(pwdCmd, errors);
        if (errors.hasErrors()){
            return "edit/changePwdForm";
        }

        // 로그인된 상태에서 비번을 변경하므로, 기존 Session 정보를 사용 ( 없으면 에러 )
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        // 세션이 파기되었다면 (서버 재시작도 포함) NullPointerException 발생

        try {
            changePasswordService.changePassword(
                    authInfo.getEmail(),
                    pwdCmd.getCurrentPassword(),
                    pwdCmd.getNewPassword()
            );
            return "edit/changePwd";
        }catch (WrongIdPasswordException e){
            errors.rejectValue("currentPassword", "notMatching");
            return "edit/changePwdForm";
        }
    }
}
