package com.github.hiwepy.httpconn.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.alibaba.fastjson.JSONObject;
import com.github.hiwepy.httpconn.HttpIOUtils;
import com.github.hiwepy.httpconn.HttpStatus;
import com.github.hiwepy.httpconn.exception.HttpResponseException;

/**
 * http请求响应处理：返回JSONObject对象
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class JSONResponseHandler implements ResponseHandler<JSONObject> {

	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
	}
	
	@Override
	public JSONObject handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			String result = HttpIOUtils.toInputText(httpConn.getInputStream(), charset);
			return JSONObject.parseObject(result);
		} else {
			String result = HttpIOUtils.toInputText(httpConn.getErrorStream(), charset);
			if(result != null && result.length() > 0) {
				throw new HttpResponseException(status, result.toString());
			}
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}
	
}
