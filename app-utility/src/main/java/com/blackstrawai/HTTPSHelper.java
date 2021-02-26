package com.blackstrawai;



import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

public class HTTPSHelper {

	private static HTTPSHelper httpsHelper;
	

private HTTPSHelper()
{
	
}
public static HTTPSHelper getInstance()
{
	if(httpsHelper==null)
	{
		httpsHelper=new HTTPSHelper();
	}
	return httpsHelper;
}

public URLConnection getConnection(String apiUrl)throws ApplicationException
{
	HttpsURLConnection conn=null;
	try
	{
	 TrustManager[] trustAllCerts = new TrustManager[]{
             new X509ExtendedTrustManager()
             {
                 @Override
                 public java.security.cert.X509Certificate[] getAcceptedIssuers()
                 {
                     return null;
                 }

                 @Override
                 public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                 {
                 }

                 @Override
                 public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                 {
                 }

                 @Override
                 public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket)
                 {

                 }

                 @Override
                 public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) 
                 {

                 }

                 @Override
                 public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) 
                 {

                 }

                 @Override
                 public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) 
                 {

                 }

             }
     	};

     	SSLContext sc = SSLContext.getInstance("SSL");
     	sc.init(null, trustAllCerts, new java.security.SecureRandom());
     	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier allHostsValid = new  HostnameVerifier()
        {
        	@Override
        	public boolean verify(String hostname, SSLSession session)
        	{
        		return true;
        	}
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        URL url = new URL(apiUrl);
        conn = (HttpsURLConnection) url.openConnection();
        return conn;
	}
	catch(Exception e)
	{
		throw new ApplicationException(e);
	}
}
}
/*HttpsURLConnection conn = (HttpsURLConnection) HTTPHelper.getInstance().getConnection(apiUrl);
String usernameColonPassword = "LP_API_ORD:2WHdZ4p!#q#f";
String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
conn.setDoOutput(true);
conn.setRequestMethod("POST");
conn.addRequestProperty("Authorization", basicAuthPayload);
conn.setRequestProperty("Content-Type", "application/json");
conn.setRequestProperty("Accept", "application/json");
conn.setDoOutput(true);
conn.setRequestMethod("POST");

OutputStream os = conn.getOutputStream();

os.write(json1.toString().getBytes());
os.flush();
os.close();
}
*/