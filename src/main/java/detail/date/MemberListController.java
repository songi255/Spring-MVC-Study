package detail.date;

import detail.spring.Member;
import detail.spring.MemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MemberListController {
    private MemberDao memberDao;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @RequestMapping("/members")
    public String list(@ModelAttribute("cmd") ListCommand listCommand, Errors errors, Model model){
        if (errors.hasErrors()){
            // 만약 형식에 맞지 않게 입력하면 Error 가 발생한다. 이때는 Errors 를 사용해서 처리해주도록 하자.
            // @DateTimeFormat 은 형식이 맞지 않으면 typeMismatch 코드를 추가한다.
            return "member/memberList";
        }
        if (listCommand.getFrom() != null && listCommand.getTo() != null){
            List<Member> members = memberDao.selectByRegdate(listCommand.getFrom(), listCommand.getTo());
            model.addAttribute("members", members);
        }
        return "member/memberList";
    }
}
