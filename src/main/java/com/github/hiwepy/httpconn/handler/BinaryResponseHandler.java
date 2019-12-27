package com.github.hiwepy.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.github.hiwepy.httpconn.HttpIOUtils;
import com.github.hiwepy.httpconn.HttpStatus;
import com.github.hiwepy.httpconn.exception.HttpResponseException;

public class BinaryResponseHandler implements ResponseHandler<byte[]> {

	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
	}
	
	@Override
	public byte[] handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			InputStream input = null;
			byte[] content = null;
			try {
				// 调用HttpURLConnection连接对象的getInputStream()函数,将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。 并获取 request中的输入流
				input = httpConn.getInputStream();
				content = HttpIOUtils.toByteArray(input);
			} finally {
				// 释放资源
				HttpIOUtils.closeQuietly(input);
			}
			return content;
		} else {
			String error = HttpIOUtils.toInputText(httpConn.getErrorStream(), charset);
			if(error != null && error.trim().length() > 0) {
				throw new HttpResponseException(status, error);
			}
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}

}
