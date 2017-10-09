package org.unicorn.framework.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * http工具类
 * 
 * @author xiebin
 * @since 2017-08-25
 */
public class CoreHttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(CoreHttpUtils.class);

	public static final int DEFAULT_TIMEOUT = 5000;
	
	public static final String CHARSET_UTF8="UTF-8";
	public static final List<Integer> HTTPSTATUS=new ArrayList<>();
	static{
		HTTPSTATUS.add(204);
		HTTPSTATUS.add(404);
		HTTPSTATUS.add(403);
	}

	public static String get(String requestUrl, Object body) throws IOException {
		return CoreHttpUtils.get(requestUrl, body, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String get(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.get(requestUrl, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String get(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "GET");
	}

	public static String get(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT, "GET");
	}

	public static String get(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "GET");
	}

	public static String post(String requestUrl, Object body) throws IOException {
		return CoreHttpUtils.post(requestUrl, body, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String post(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.post(requestUrl, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String post(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "POST");
	}

	public static String post(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT, "POST");
	}

	public static String post(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "POST");
	}

	public static String post(String requestUrl, Map<String, String> headerMap, Object body, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, CHARSET_UTF8, timeout, "POST");
	}

	public static String put(String requestUrl, Object body) throws IOException {
		return CoreHttpUtils.put(requestUrl, body, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String put(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.put(requestUrl, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String put(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "PUT");
	}

	public static String put(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT, "PUT");
	}

	public static String put(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "PUT");
	}

	public static String delete(String requestUrl, Object body) throws IOException {
		return CoreHttpUtils.delete(requestUrl, body, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String delete(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.delete(requestUrl, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String delete(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "DELETE");
	}

	public static String delete(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, CHARSET_UTF8, DEFAULT_TIMEOUT, "DELETE");
	}

	public static String delete(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, CHARSET_UTF8, DEFAULT_TIMEOUT, "DELETE");
	}


	public static String callHttp(String requestUrl, Map<String, String> headerMap, Object body, String contentTypeString, String encoding, int timeout,
			String method) throws IOException {
		Authenticator.setDefault(new MyAuthenticator());
		String result = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			// Https访问方式时，需要增加SSL相关设置
			if (requestUrl.startsWith("https://")) {
				SSLContext sslContext = null;
				try {
					sslContext = SSLContext.getInstance("TLS"); // 或SSL
					X509TrustManager[] xtmArray = new X509TrustManager[] { new MyX509TrustManager() };
					sslContext.init(null, xtmArray, new java.security.SecureRandom());
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
				if (sslContext != null) {
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
				}
				HttpsURLConnection.setDefaultHostnameVerifier(new myHostnameVerifier());
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式
			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "Mozilla/31.0 (compatible; MSIE 10.0; Windows NT; DigExt)"); //防止报403错误。  
			// 设置超时时间
			conn.setReadTimeout(timeout);
			// 设置请求头部
			if (contentTypeString != null && !contentTypeString.isEmpty()) {
				conn.setRequestProperty("Content-Type", contentTypeString);
			} else {
				conn.setRequestProperty("Content-Type", "application/json");
			}
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			conn.connect();
			if (null != body) {
				String outputStr = null;
				if (body instanceof String) {
					outputStr = (String) body;
				} else {
					outputStr = new Gson().toJson(body);
				}
				logger.info("请求参数+" + outputStr);
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes(encoding));
				outputStream.flush();
				outputStream.close();
			}
			// 从输入流读取返回内容
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, encoding);
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			result = buffer.toString();

		} catch (IOException ex) {
			int status=conn.getResponseCode();
			logger.error("接口调用失败:响应码：{}",conn.getResponseCode());
			if(!HTTPSTATUS.contains(status)){
				throw ex;
			}
			
		} finally {
			// 释放资源
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				conn.disconnect();
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static byte[] getInputStream(String requestUrl, Map<String, String> headerMap, Object body, String contentTypeString, String encoding, int timeout,
			String method) throws IOException {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			// Https访问方式时，需要增加SSL相关设置
			if (requestUrl.startsWith("https://")) {
				SSLContext sslContext = null;
				try {
					sslContext = SSLContext.getInstance("TLS"); // 或SSL
					X509TrustManager[] xtmArray = new X509TrustManager[] { new MyX509TrustManager() };
					sslContext.init(null, xtmArray, new java.security.SecureRandom());
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
				if (sslContext != null) {
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
				}
				HttpsURLConnection.setDefaultHostnameVerifier(new myHostnameVerifier());
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式
			conn.setRequestMethod(method);
			// 设置超时时间
			conn.setReadTimeout(timeout);
			// 设置请求头部
			if (contentTypeString != null && !contentTypeString.isEmpty()) {
				conn.setRequestProperty("Content-Type", contentTypeString);
			} else {
				conn.setRequestProperty("Content-Type", "application/json");
			}
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			conn.connect();
			if (null != body) {
				String outputStr = null;
				if (body instanceof String) {
					outputStr = (String) body;
				} else {
					outputStr = new Gson().toJson(body);
				}
				logger.info("请求参数+" + outputStr);
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes(encoding));
				outputStream.flush();
				outputStream.close();
			}
			// 从输入流读取返回内容
			inputStream = conn.getInputStream();
			return IOUtils.toByteArray(inputStream);
		} catch (IOException ex) {
			throw ex;
		} finally {
			// 释放资源
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				conn.disconnect();
			} catch (Exception e) {

			}
		}
	}

	static class MyX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 重写一个方法
	 */
	static class myHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	static class MyAuthenticator extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			String username = "guest";
			String password = "guest";
			return new PasswordAuthentication(username, password.toCharArray());
		}
	}
	
}
