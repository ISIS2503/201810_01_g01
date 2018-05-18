package programa;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
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

public class Policia implements MqttCallback {
	
MqttClient client;
String caFilePath = "certs/m2mqtt_ca.crt";
String clientCrtFilePath = "certs/m2mqtt_srv.crt";
String clientKeyFilePath = "certs/m2mqtt_srv.key";

private Interfaz interfaz;

public Policia() 
{
	
}

public static void main(String[] args) 
{
    new Policia().correr();    
}

public void correr() 
{	
	interfaz = new Interfaz( );
    interfaz.setVisible( true );
    
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
    } catch (Exception e) {
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
	String alerta = "";
	System.out.println(num);
	
	if(num.equals("4"))
	{
		alerta= "Posible secuestro en: Cra 3 # 21-46 ";
		interfaz.enviarAlerta(alerta);
	}
	
	else
	{
		return;
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

private void publish(String content)
{
	 try {
		 String topic = "1/1/monitoreo";
		 String broker = "tcp://172.24.42.95:8083";
		 MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        
        System.out.println("Connecting to broker: " + broker);
        
        client.connect(connOpts);
        
        System.out.println("Connected");
        System.out.println("Publishing message: "+content);
        
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(2);
        client.publish(topic, message);
        
        System.out.println("Message published");
        
        client.disconnect();
        
        //System.out.println("Disconnected");
        //System.exit(0);
    } catch(MqttException me) {
        System.out.println("reason "+me.getReasonCode());
        System.out.println("msg "+me.getMessage());
        System.out.println("loc "+me.getLocalizedMessage());
        System.out.println("cause "+me.getCause());
        System.out.println("excep "+me);
        me.printStackTrace();
    }
}

private void sendMail(String cuerpo)
{
	try{
			URL url = new URL("http://172.24.42.87:8081/mail");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"asunto\":\"Alarma\",\"remitente\":\"m.rodriguez21@uniandes.edu.co\","
					+ "\"cuerpo\":\""+cuerpo+"\","
					+ "\"destinatarios\": [\"jr.restom10@uniandes.edu.co\"]}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != 204) {
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
}