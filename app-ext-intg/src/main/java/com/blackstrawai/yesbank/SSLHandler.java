package com.blackstrawai.yesbank;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyStore;
import java.util.Properties;

public class SSLHandler {

  public static String certPassword = null;
  private static SSLHandler sslHandler;
  private final Logger logger = Logger.getLogger(SSLHandler.class);

  private SSLHandler() {}

  public static SSLHandler getInstance() {
    if (sslHandler == null) {
      sslHandler = new SSLHandler();

      FileReader reader;
      try {

        reader = new FileReader(YesBankConstants.DECIFER_CONFIG_PROPERTTY_URL);
        Properties p = new Properties();
        p.load(reader);

        certPassword = p.getProperty(YesBankConstants.CERT_PASSWORD_PROPERTY_KEY);

      } catch (Exception e) {
        throw new ApplicationRuntimeException(e);
      }
    }
    return sslHandler;
  }

  public Client sslConnect() throws ApplicationException {

    logger.info(" Entering into SSL Connect");
    Client client;

    try {

      KeyStore identityKeyStore = KeyStore.getInstance(YesBankConstants.KEYSTORE_TYPE);

      FileInputStream identityKeyStoreFile =
          new FileInputStream(new File(YesBankConstants.KEYSTORE_TYPE_PATH));

      identityKeyStore.load(identityKeyStoreFile, certPassword.toCharArray());

      // load identity keystore
      SSLContext sslContext =
          SSLContexts.custom()
              .loadKeyMaterial(identityKeyStore, certPassword.toCharArray(), null)
              .build();

      client = ClientBuilder.newBuilder().sslContext(sslContext).build();

      logger.info("SSL handshake is successful");
      return client;
    } catch (Exception ex) {
      logger.error("Exception in SSL Connection" + ex);
      throw new ApplicationException(ex);
    }
  }
}
