package com.yundongse.network;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
/**
 * @author zhanghang �������ӻ�����
 */
public class BaseHttpClient {
	private final int timeout=100*1000;//��ʱʱ��Ϊ100��
    
	private DefaultHttpClient defalutHttpClient;
	
	public BaseHttpClient(){
		try {
			initHttpClient();//��ʼ��HttpClient
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʼ��HttpClient��֧��http��https����Э��
	 * @throws NoSuchAlgorithmException 
	 */
	private void  initHttpClient() throws NoSuchAlgorithmException {
		final SchemeRegistry supportedSchemes = new SchemeRegistry();//Э��ע��
		supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		
//	    SSLSocketFactory ssf = SSLContext.getInstance("TLS").getSocketFactory();
//	    ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);//����������������֤
		supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		
		HttpParams httpParams = new BasicHttpParams();
		
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout * 1000); // ���ӳ�ʱ
        HttpConnectionParams.setSoTimeout(httpParams, timeout * 1000); // ����ʱ
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);

        ConnManagerParams.setTimeout(httpParams, 1000); // �����ӳ���ȡ���ӵĳ�ʱʱ��
                                                    // ��1.4.0.2�汾����
        ConnManagerParams.setMaxTotalConnections(httpParams, 128); //
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(32);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
		
		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
	    defalutHttpClient = new DefaultHttpClient(ccm, httpParams);
	}
	
	public HttpResponse doPost(String url,List list) throws ClientProtocolException, IOException {
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setEntity(new UrlEncodedFormEntity(list,HTTP.UTF_8));
		return defalutHttpClient.execute(httpRequest);
	}
	
	public HttpResponse doGet(String url,Map list) throws ClientProtocolException, IOException {
		Iterator interator = list.entrySet().iterator();//��������
		while(interator.hasNext()){
			if(url.indexOf("?")==-1) url+="?";
			Entry<String,String> entry = (Entry<String,String>)interator.next();
			url+=entry.getKey()+"="+entry.getValue()+"&";
		}
		url = url.substring(0,url.length()-1);
		HttpGet httpRequest = new HttpGet(url);
		return defalutHttpClient.execute(httpRequest);
	}
	
}
