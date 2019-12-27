package com.github.hiwepy.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.github.hiwepy.httpconn.HttpIOUtils;
import com.github.hiwepy.httpconn.HttpStatus;
import com.github.hiwepy.httpconn.exception.HttpResponseException;
import com.thoughtworks.xstream.XStream;

public class ObjectResponseHandler implements ResponseHandler<Object> {

	protected XStream xstream = new XStream();
	
	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
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
			String error = HttpIOUtils.toInputText(httpConn.getErrorStream(), charset);
			if(error != null && error.trim().length() > 0) {
				throw new HttpResponseException(status, error);
			}
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}
 
}
