package com.github.vindell.httpconn.exception;

import java.io.IOException;

/**
 * 
 * *******************************************************************
 * @className	： HttpResponseException
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">vindell</a>
 * @date		： Dec 15, 2016 11:46:21 AM
 * @version 	V1.0 
 * *******************************************************************
 */
@SuppressWarnings("serial")
public class HttpResponseException extends IOException {
	
	private int statusCode = 200;

	public HttpResponseException(String message) {
		super(message);
	}
	
	public HttpResponseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HttpResponseException(final int statusCode, final String s) {
		super(s);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

}
