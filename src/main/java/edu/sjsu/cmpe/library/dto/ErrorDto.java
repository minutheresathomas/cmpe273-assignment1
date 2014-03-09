package edu.sjsu.cmpe.library.dto;

public class ErrorDto {
	private String errorCode = "error"; // default is 'error'
    private String errorMessage = "Message"; // default is 'Message'
    
    /**
     * @param errorCode
     * @param errorMessage
     */
    public ErrorDto(String errorCode, String errorMessage) {
	super();
	this.errorCode = errorCode;
	this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
