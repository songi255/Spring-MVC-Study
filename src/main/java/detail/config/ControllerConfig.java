package detail.config;

import detail.date.MemberDetailController;
import detail.date.MemberListController;
import detail.json.RestMemberController;
import detail.session.*;
import detail.spring.ChangePasswordService;
import detail.spring.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import detail.controller.RegisterController;
import detail.spring.MemberRegisterService;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

@Configuration
public class ControllerConfig {

    @Autowired
    private MemberRegisterService memberRegSvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private ChangePasswordService changePasswordService;
    @Autowired
    private MemberDao memberDao;

    @Bean
    public RegisterController registerController(){
        RegisterController controller = new RegisterController();
        controller.setMemberRegisterService(memberRegSvc);
        return controller;
    }

    @Bean
    public LoginController loginController(){
        LoginController controller = new LoginController();
        controller.setAuthService(authService);
        return controller;
    }

    @Bean
    public LogoutController logoutController(){
        return new LogoutController();
    }

    @Bean
    public ChangePwdController changePwdController(){
        ChangePwdController controller = new ChangePwdController();
        controller.setChangePasswordService(changePasswordService);
        return controller;
    }

    @Bean
    public MemberListController memberListController(){
        MemberListController controller = new MemberListController();
        controller.setMemberDao(memberDao);
        return controller;
    }

    @Bean
    public MemberDetailController memberDetailController(){
        MemberDetailController controller = new MemberDetailController();
        controller.setMemberDao(memberDao);
        return controller;
    }

    @Bean
    public RestMemberController restApi(){
        RestMemberController controller = new RestMemberController();
        controller.setMemberDao(memberDao);
        controller.setRegisterService(memberRegSvc);
        return controller;
    }
}
