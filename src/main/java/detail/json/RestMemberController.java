package detail.json;

import detail.spring.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

// JSON 변환은 Jackson 라이브러리를 사용하면 좋다. pom 에 설정하자.
// Jackson 에 대한 자세한 설명은 https://github.com/FasterXML/jackson-docs 참고하자.

// Json 응답에는 Controller 대신 RestController 사용하면 된다.
@RestController
// 이게 있기 전에는 @Controller 에 @ResponseBody 를 메소드에 사용했다. Spring 4 부터는 @RestController 를 사용하면 된다.
public class RestMemberController {
    private MemberDao memberDao;
    private MemberRegisterService registerService;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void setRegisterService(MemberRegisterService registerService) {
        this.registerService = registerService;
    }

    // 기존 메서드와 다른 점은, View 를 return 하지 않고, 일반객체를 그대로 return 했다.
    @GetMapping("/api/members")
    public List<Member> members(){
        return memberDao.selectAll();
    }
    // 이렇게 하면, Spring MVC 가 알아서 변환해서 응답데이터로 전송한다.
    // Classpath 에 Jackson 이 존재하기 때문에, JSON String 으로 응답한다.

    @GetMapping("/api/members/{id}")
    public Member member(@PathVariable Long id, HttpServletResponse response) throws IOException{
        Member member = memberDao.selectById(id);
        if (member == null){
            // 아래와 같이 response 객체로 404 응답을 하면 JSON 이 아닌, Server의 기본 HTML 로 응답하게 되는 문제가 있다.
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return member;
        // Member 에는 비번 등도 있는데, 이는 반환하면 안된다. Member Class 필드에 @JsonIgnore 를 적용해서 직렬화 제외할 수 있다.
        // 또한, 날짜 같은 경우 Class 에 따라 Array 나 Unix Timestamp 등으로 변환되거나 해버린다.
        //  - Unix Timestamp : 1970 / 01 / 01 이후 흘러간 millisecond
        //  - 이 경우, 마찬가지로 Member 날짜 필드에 @JsonFormat(Shape.String) 으로 특정 형식 String 으로 바꿔 응답할 수 있다.
    }

    // ResponseEntity 를 사용하는 JSON 응답은 아래와 같다.
    @GetMapping("/api/members/{id}")
    public ResponseEntity<Object> member(@PathVariable Long id){
        Member member = memberDao.selectById(id);
        if (member == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("no member"));
            // 이렇게 직접 처리하지말고, 중복된다면 아래처럼 예외처리해버리자.
            // throw new MemberNotFoundException(); // ExceptionHandler 가 처리하게 하자.
        }
        return ResponseEntity.status(HttpStatus.OK).body(member);
        // ResponseEntity.ok(member); // 한번에 처리하는 메서드도 있다.
        // ResponseEntity.ok().build(); // 몸체 내용이 없으면 바로 build 하면 된다.

        // Spring MVC 는 return 이 ResponseEntity 면 status와 body 를 사용해서 반환처리한다.

        // 마찬가지로, ExceptionHandler를 사용한다면 ResponseEntity 반환 대신 member 를 직접 반환하면 된다.
        //return member;
    }

    // 이번엔 반대로 JSON 요청을 Java Command 객체로 바꿔서 받아보자. @RequestBody 를 쓰면 된다.
    @PostMapping("/api/members")
    public void newMember(
            @RequestBody @Valid RegisterRequest regReq,
            Errors errors,
            HttpServletResponse response) throws IOException{

        // 알고있겠지만, body 에 들어오기 때문에 요청형식이 application/json 이어야 한다.
        // 만약 query 로 전송된다면 application/x-www-form-urlencoded 형식이다. 이러면 안되고, postman 이나 httpie(CLI) 등을 사용해야 한다.
        // Chrome 확장 프로그램에는 Advanced REST client 가 있다. 걍 Postman 쓰겠다.

        // @Valid 에서 발생하는 Exception 은 Errors 에 담겨오므로, 필요하면 Errors 를 받으면 된다.
        if (errors.hasErrors()){
            String errorCodes = errors.getAllErrors().stream()
                    .map(error -> error.getCodes()[0])
                    .collect(Collectors.joining(","));

            /* return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("errorCodes = " + errorCodes));
             */
        }
        // Errors 를 받지 않는데 @Valid 에서 걸리면 MethodArgumentNotValidException 발생한다.
        // 즉, @ExceptionHandler 로 처리해줘도 된다.

        try {
            Long newMemberId = registerService.regist(regReq);
            response.setHeader("Location", "/api/members/" + newMemberId);
            response.setStatus(HttpServletResponse.SC_CREATED);
            // 응답받을 때, 201(created) 코드와 함꼐 Location 헤더를 잘 받은 것을 볼 수 있다.

            // 만약 ResponseEntity 를 사용한다면 아래와 같이 하면 된다.
            URI uri = URI.create("/api/members/" + newMemberId); // Location 에 자동으로 들어간다는건지? Created 와 연관이 있는 듯 하다.
            //return ResponseEntity.created(uri).build();
        }catch (DuplicateMemberException dupEx){
            response.sendError(HttpServletResponse.SC_CONFLICT);
            //return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    // 동일 오류는 ExceptionHandler 로 처리하자!
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoData(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("no member"));
    }
}
