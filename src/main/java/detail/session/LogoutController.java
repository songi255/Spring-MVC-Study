package detail.session;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        // logout 시 session 파기 하는 법
        session.invalidate();
        return "redirect:/main";
    }
}
