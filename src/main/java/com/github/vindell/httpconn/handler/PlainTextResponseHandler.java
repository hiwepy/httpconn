package com.github.vindell.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpStatus;
import com.github.vindell.httpconn.exception.HttpResponseException;

public class PlainTextResponseHandler implements ResponseHandler<String> {

	@Override
	public void handleConn(HttpURLConnection httpConn) {
		
	}

	@Override
	public String handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			 InputStream input = null;
			 InputStreamReader reader = null;
			 try {
				 // 从request中取得输入流
				 input = httpConn.getInputStream(); 
				 reader = new InputStreamReader(input, charset);
				 return HttpIOUtils.toString(reader);
			} finally{
				// 释放资源
				HttpIOUtils.closeQuietly(input);
				HttpIOUtils.closeQuietly(reader);
			}
		} else {
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}

}
