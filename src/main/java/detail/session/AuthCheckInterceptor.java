package detail.session;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* 만약 Login 하지 않았는데, 비밀번호 변경 Form 이 출력되는 것은 이상하다.
    Controller 들이 처리하기 전에 Session 을 확인하도록 Interceptor 를 사용할 수 있다.

    Interceptor 는 다음 3 시점에 공통기능을 삽입할 수 있다.
        - Controller 실행 전 : preHandle()
            - handle 파라미터는 Controller 객체이다.
            - Login 하지 않은 경우 Controller 미실행 가능
            - Controller 실행 전 실행에 필요한 정보 생성
            - Boolean 을 return 하는데, false 면 Controller 또는 다음 Interceptor 를 실행하지 않는다.
        - Controller 실행 후 ~ View 실행 전 : postHandle()
            - Controller 가 Exception 발생했다면 실행하지 않는다.
        - View 실행 후 : afterCompletion()
            - Client 에 응답을 전송한 뒤에 실행된다.
            - Controller 에서 Exception 이 발생했다면 4번째 parameter 로 전달된다.(안했으면 null)
            - 예기치 않은 Exception 을 Logging 하거나, 실행시간 기록 등 후처리에 적합하다.


*/
public class AuthCheckInterceptor implements HandlerInterceptor {
    // HandlerInterceptor 역시 Default Method 로 구성되어, 필요한 것만 재정의하면 된다.


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null){
            Object authInfo = session.getAttribute("authInfo");
            if (authInfo != null){
                return true;
            }
        }
        
        // session 정보가 없으면 로그인해라! redirect 해버리기
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
