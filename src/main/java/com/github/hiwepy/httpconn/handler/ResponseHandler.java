package com.github.hiwepy.httpconn.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;


/**
 * Handler that encapsulates the process of generating a response object
 * from a {@link java.net.HttpURLConnection}.
 */
public interface ResponseHandler<T> {

	/**
	 * 对HttpURLConnection进行预处理
	 * @param httpConn {@link java.net.HttpURLConnection} 对象
	 */
	void preHandle(HttpURLConnection httpConn);
	
    /**
     * 
     * Processes an {@link OutputStream} and returns some value corresponding to that response.
     * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
     * @param httpConn {@link java.net.HttpURLConnection} 对象
     * @param charset 字符集编码
     * @return 处理后的对象
     * @throws IOException in case of a problem or the connection was abortedTODO
     */
    T handleResponse(HttpURLConnection httpConn, String charset) throws IOException;

}
