 package com.github.hiwepy.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.hiwepy.httpconn.HttpIOUtils;
import com.github.hiwepy.httpconn.HttpStatus;
import com.github.hiwepy.httpconn.exception.HttpResponseException;

/**
 * http请求响应处理：返回org.w3c.dom.Document对象
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class XMLResponseHandler implements ResponseHandler<Document> {

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
	}
	
	@Override
    public Document handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			try(// 从request中取得输入流
		            InputStream input = httpConn.getInputStream(); 
		            InputStreamReader reader = new InputStreamReader(input, charset);) {
	            DocumentBuilder docBuilder = factory.newDocumentBuilder();
				// 响应内容
				return docBuilder.parse(HttpIOUtils.toString(reader));
	        } catch (ParserConfigurationException ex) {
	            throw new IllegalStateException(ex);
	        } catch (SAXException ex) {
	            throw new HttpResponseException("Malformed XML document", ex);
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

 
