package programa;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ConsumidorB implements MqttCallback {

MqttClient client;
String caFilePath = "certs/m2mqtt_ca.crt";
String clientCrtFilePath = "certs/m2mqtt_srv.crt";
String clientKeyFilePath = "certs/m2mqtt_srv.key";

public ConsumidorB() {
}

public static void main(String[] args) 
{
    new ConsumidorB().correr();
}

public void correr() 
{	
	try 
	{
		//CONECION MQTT
		client = new MqttClient("tcp://172.24.42.95:8083", MqttClient.generateClientId());
    	client.setCallback(this);
    	MqttConnectOptions options = new MqttConnectOptions();
    	/*options.setUserName("microcontrolador");
    	options.setPassword("Isis2503.".toCharArray());
    	
    	options.setConnectionTimeout(60);
		options.setKeepAliveInterval(60);
		options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

		
		SSLSocketFactory socketFactory = getSocketFactory(caFilePath,
				clientCrtFilePath, clientKeyFilePath, "Isis2503.");
		options.setSocketFactory(socketFactory);
		*/
        client.connect(options);
        client.subscribe("1/1/alarma");
    } 
    catch (MqttException e) {
        e.printStackTrace();
    }
	catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
public void connectionLost(Throwable cause) 
{
	cause.printStackTrace();
	System.out.println("Connection lost"); 
}

@Override
public void messageArrived(String topic, MqttMessage message)
		throws Exception 
{
	String num = message.toString().trim();
	String tipo = "";
	
	if(num.equals("0"))
	{
		tipo = "Apertura sospechosa";
	}
	else if(num.equals("1"))
	{
		tipo = "Apertura no permitida";
	}
	else if(num.equals("2"))
	{
		tipo = "Puerta abierta";
	}
	else if(num.equals("3"))
	{
		tipo = "Batería crítica";
	}
	else if(num.equals("R"))
	{
		System.out.println("Rechazado");
		return;
	}
	else 
	{
		String[] msg = num.split(";");
		
		//Llamado del metodo
		try{
			URL url = new URL("http://172.24.42.78:8080/clave/validar/"+msg[1]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
		
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
	        }

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}


			conn.disconnect();
			
			
			}catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
		return;
	}
	
	String timestamp = getCurrentTimeStamp();
	
	//PERSISTENCIA
	
	try{
		URL url = new URL("http://172.24.42.78:8080/administrador/2/alarmas");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		String input = "{\"nombre\":\""+tipo+"\","
				+ "\"tipo\":"+num+","
				+ "\"fecha\":\""+timestamp+"\"}";

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
        }

		System.out.println(input);

		conn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	
}

@Override
public void deliveryComplete(IMqttDeliveryToken token) 
{
    // TODO Auto-generated method stub

}

public String getCurrentTimeStamp() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");//dd/MM/yyyy
    Date now = new Date();
    String strDate = sdfDate.format(now);
    return strDate;
}

private static SSLSocketFactory getSocketFactory(final String caCrtFile,
		final String crtFile, final String keyFile, final String password)
		throws Exception {
	Security.addProvider(new BouncyCastleProvider());

	// load CA certificate
	X509Certificate caCert = null;

	FileInputStream fis = new FileInputStream(caCrtFile);
	BufferedInputStream bis = new BufferedInputStream(fis);
	CertificateFactory cf = CertificateFactory.getInstance("X.509");

	while (bis.available() > 0) {
		caCert = (X509Certificate) cf.generateCertificate(bis);
		// System.out.println(caCert.toString());
	}

	// load client certificate
	bis = new BufferedInputStream(new FileInputStream(crtFile));
	X509Certificate cert = null;
	while (bis.available() > 0) {
		cert = (X509Certificate) cf.generateCertificate(bis);
		// System.out.println(caCert.toString());
	}

	// load client private key
	PEMParser pemParser = new PEMParser(new FileReader(keyFile));
	Object object = pemParser.readObject();
	PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
			.build(password.toCharArray());
	JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
			.setProvider("BC");
	KeyPair key;
	if (object instanceof PEMEncryptedKeyPair) {
		System.out.println("Encrypted key - we will use provided password");
		key = converter.getKeyPair(((PEMEncryptedKeyPair) object)
				.decryptKeyPair(decProv));
	} else {
		System.out.println("Unencrypted key - no password needed");
		key = converter.getKeyPair((PEMKeyPair) object);
	}
	pemParser.close();

	// CA certificate is used to authenticate server
	KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
	caKs.load(null, null);
	caKs.setCertificateEntry("ca-certificate", caCert);
	TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
	tmf.init(caKs);

	// client key and certificates are sent to server so it can authenticate
	// us
	KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	ks.load(null, null);
	ks.setCertificateEntry("certificate", cert);
	ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
			new java.security.cert.Certificate[] { cert });
	KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
			.getDefaultAlgorithm());
	kmf.init(ks, password.toCharArray());

	// finally, create SSL socket factory
	SSLContext context = SSLContext.getInstance("TLSv1.2");
	context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

	return context.getSocketFactory();
}

}