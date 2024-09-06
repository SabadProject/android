package farayan.commons;

public class WebResponse {
	private String Response;
	private boolean ConnectionProblem;

	public WebResponse(String result, boolean connectionProblem) {
		this.Response = result;
		this.ConnectionProblem = connectionProblem;
	}

	public String getResponse() {
		return Response;
	}

	public void setResponse(String response) {
		Response = response;
	}

	public boolean isConnectionProblem() {
		return ConnectionProblem;
	}

	public void setConnectionProblem(boolean connectionProblem) {
		ConnectionProblem = connectionProblem;
	}
}
