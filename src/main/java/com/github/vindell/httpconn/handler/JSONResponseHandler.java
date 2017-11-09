package com.github.vindell.httpconn.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.alibaba.fastjson.JSONObject;
import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpStatus;
import com.github.vindell.httpconn.exception.HttpResponseException;

/**
 * 
 * @className	： JSONResponseHandler
 * @description	： http请求响应处理：返回JSONObject对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月13日 下午9:19:46
 * @version 	V1.0
 */
public class JSONResponseHandler implements ResponseHandler<JSONObject> {

	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
	}
	
	@Override
	public JSONObject handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			String result = HttpIOUtils.toInputText(httpConn.getErrorStream(), charset);
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
