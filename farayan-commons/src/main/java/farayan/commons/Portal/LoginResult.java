package farayan.commons.Portal;

public class LoginResult {
    private String AuthCookie;
    private boolean Success;
    private LoginResults Result;
    private String Message;

    public LoginResult(LoginResults result) {
        this.Result = result;
        if (result == LoginResults.Success)
            Success = true;
    }

    public LoginResult(LoginResults result, String authCookie) {
        this(result);
        AuthCookie = authCookie;
    }

    public String getAuthCookie() {
        return AuthCookie;
    }

    public void setAuthCookie(String authCookie) {
        AuthCookie = authCookie;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public LoginResults getResult() {
        return Result;
    }

    public void setResult(LoginResults result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
