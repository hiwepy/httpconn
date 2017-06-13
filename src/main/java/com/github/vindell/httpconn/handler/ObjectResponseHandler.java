package com.github.vindell.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpStatus;
import com.github.vindell.httpconn.exception.HttpResponseException;
import com.thoughtworks.xstream.XStream;

public class ObjectResponseHandler implements ResponseHandler<Object> {

	protected XStream xstream = new XStream();
	
	@Override
	public void handleConn(HttpURLConnection httpConn) {
		
	}
	
	@Override
	public Object handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			InputStream input = null;
			try {
				// 从request中取得输入流
				input = httpConn.getInputStream();
				return xstream.fromXML(input);
			} finally {
				// 释放资源
				HttpIOUtils.closeQuietly(input);
			}
		} else {
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}
 
}
