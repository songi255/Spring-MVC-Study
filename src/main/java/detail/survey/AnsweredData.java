package detail.survey;

import java.util.List;

public class AnsweredData {
    // 이 Command 객체는 Collection 타입, 중첩 프로퍼티를 가진다.
    // 이 경우에, Spring 이 알아서 처리해 줄 수 있다. view 에서 문법을 쓰면 된다.
    private List<String> responses; // responses[i] 형태로 쓰면 된다.
    private Respondent res; // res.name, res.email 등 형태로 쓰면 된다.

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }

    public Respondent getRes() {
        return res;
    }

    public void setRes(Respondent res) {
        this.res = res;
    }
}
