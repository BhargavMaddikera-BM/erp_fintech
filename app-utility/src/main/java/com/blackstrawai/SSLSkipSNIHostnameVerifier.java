package com.blackstrawai;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class SSLSkipSNIHostnameVerifier implements HostnameVerifier {

	
	 public SSLSkipSNIHostnameVerifier() {
	  }
	 
	 @Override
	  public boolean verify (String hostname, SSLSession session) {

	    // Return true so that we implicitly trust hostname mismatch
	    return true;
	  }

}
