package com.github.vindell.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.github.vindell.httpconn.ContentType;
import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpHeaders;
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
@SuppressWarnings("unchecked")
public class JSONResponseHandler implements ResponseHandler<JSONObject> {

	// 读取输入流
	protected SAXReader reader = new SAXReader();
	
	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
	}
	
	@Override
	public JSONObject handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			String contentType = httpConn.getHeaderField(HttpHeaders.CONTENT_TYPE);
			// xml转换成JSON对象
			if (contentType.startsWith(ContentType.APPLICATION_XML)) {
				// 将解析结果存储在JSONObject中
				JSONObject resultXML = new JSONObject();
				InputStream input = null;
				try {
					// 从request中取得输入流
					input = httpConn.getInputStream();
					Document document = reader.read(input);
					// 得到xml根元素
					Element root = document.getRootElement();
					// 得到根元素的所有子节点
					List<Element> elementList = root.elements();
					List<Element> childElements = null;
					// 遍历所有子节点
					for (Element e : elementList) {
						childElements = e.elements();
						if (childElements != null && !childElements.isEmpty()) {
							resultXML.put(e.getName(),parseJSONObject(childElements));
						} else {
							resultXML.put(e.getName(), e.getTextTrim());
						}
					}
				} catch (DocumentException ex) {
					throw new HttpResponseException("Malformed XML document",ex);
				} finally {
					// 释放资源
					HttpIOUtils.closeQuietly(input);
				}
				return resultXML;
			} else if (contentType.startsWith(ContentType.APPLICATION_JSON)) {
				 InputStream input = null;
				 InputStreamReader reader = null;
				 try {
					 // 从request中取得输入流
					 input = httpConn.getInputStream(); 
					 reader = new InputStreamReader(input, charset);
					 return JSONObject.parseObject(HttpIOUtils.toString(reader));
				} finally{
					// 释放资源
					HttpIOUtils.closeQuietly(input);
					HttpIOUtils.closeQuietly(reader);
				}
			} else {
				throw new HttpResponseException("Unexpected content type:" + contentType);
			}
		} else {
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}

	private static JSONObject parseJSONObject(List<Element> childElements) {
		// 将解析结果存储在JSONObject中
		JSONObject resultXML = new JSONObject();
		if (childElements != null && !childElements.isEmpty()) {
			// 遍历所有子节点
			for (Element e2 : childElements) {
				// 得到根元素的所有子节点
				List<Element> childElements2 = e2.elements();
				if (childElements2 != null && !childElements2.isEmpty()) {
					resultXML.put(e2.getName(), parseJSONObject(childElements2));
				} else {
					resultXML.put(e2.getName(), e2.getTextTrim());
				}
			}
		}
		return resultXML;
	}
}
