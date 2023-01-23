package detail.date;

import detail.spring.Member;
import detail.spring.MemberDao;
import detail.spring.MemberNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MemberDetailController {
    private MemberDao memberDao;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    
    // PathVariable 을 사용하여 요청 경로 변수 처리
    @GetMapping("/members/{id}")
    public String detail(@PathVariable("id") Long memId, Model model){
        Member member = memberDao.selectById(memId);
        if (member == null){
            throw new MemberNotFoundException();
        }
        model.addAttribute("member", member);
        return "member/memberDetail";
    }

    // 만약 @PathVariable 에서 타입변환에 실패하면 어떻게 처리해야 할까? (Controller 처리 전이기 떄문에 try-catch 를 못쓴다...)
    // @ExceptionHandler 를 사용하면 같은 Controller 내의 해당타입 Exception 을 처리할 수 있다. (에러응답 대신 아래 메서드 실행)
    @ExceptionHandler(TypeMismatchException.class)
    public String handleTypeMismatchException(){
        return "member/invalid";
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public String handleNotFoundException(MemberNotFoundException ex){
        // Exception 매개변수는 꼭 안받아도 되지만, 익셉션에 관한 정보, 로그 남기기 등의 작업을 하고 싶다면 받으면 된다.
        return "member/noMember";
    }
}
