package hr.demo.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@JsonInclude(NON_DEFAULT)
public class HttpResponse {
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;

    public HttpResponse(String timeStamp, int statusCode, HttpStatus status, String reason, String developerMessage) {
    	this.timeStamp = timeStamp; 
    	this.statusCode = statusCode;
    	this.status = status; 
    	this.reason = reason; 
    	this.developerMessage = developerMessage;
    }

	public HttpResponse() {

	}

	@Override
	public String toString() {
		return "HttpResponse [timeStamp=" + timeStamp + ", statusCode=" + statusCode + ", status=" + status
				+ ", reason=" + reason + ", message=" + message + ", developerMessage=" + developerMessage + "]";
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
    
    
}





















