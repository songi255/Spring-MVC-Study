package detail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import detail.survey.Question;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/survey")
public class SurveyController {
    @GetMapping
    public String form(Model model){
        List<Question> questions = createQuestions();
        model.addAttribute("questions", questions);
        return "survey/surveyForm";
    }

    // 아래처럼 Question 을 DB 에서 읽어 와 동적으로 생성할 수 있다.
    private List<Question> createQuestions(){
        Question q1 = new Question("당신의 역할은 무엇입니까?", Arrays.asList("서버", "프론트", "풀스택"));
        Question q2 = new Question("많이 사용하는 개발도구는 무엇입니까?", Arrays.asList("Eclipse", "IntelliJ", "sublime"));
        Question q3 = new Question("하고싶은 말을 적어주세요");
        return Arrays.asList(q1, q2, q3);
    }

    // Mapping 메서드에 Model And View 를 곧바로 return 하면 Model 을 안써도 된다...만 큰 차이는 없다.
    @GetMapping
    public ModelAndView form2(){
        List<Question> questions = createQuestions();
        ModelAndView mav = new ModelAndView();
        mav.addObject("questions", questions);
        mav.setViewName("survey/surveyForm");
        return mav;
    }
}
