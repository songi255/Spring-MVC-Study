package detail.json;

// Error 상태일 때 JSON 응답으로 사용할 Class 이다.
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
