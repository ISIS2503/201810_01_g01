package programa;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Consumidor implements MqttCallback {

MqttClient client;

public Consumidor() {
}

public static void main(String[] args) 
{
    new Consumidor().correr();
}

public void correr() 
{	
	try 
	{
		//CONECION MQTT
    	client = new MqttClient("tcp://172.24.42.95:8083", MqttClient.generateClientId());
    	client.setCallback(this);
        client.connect();
        client.subscribe("home");
    } 
    catch (MqttException e) {
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
	String num = message.toString();
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
	else
	{
		tipo = "Batería crítica";
	}
	
	String timestamp = getCurrentTimeStamp();
	String msg = tipo+" - "+ timestamp;
	
	//PERSISTENCIA
	
	try{
		URL url = new URL("http://172.24.42.78:8080/administrador/1/alarmas");
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

		System.out.println(input);

		conn.disconnect();
		
		}catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	
	//CORREO
	
	try{
		URL url = new URL("http://172.24.42.87:8081/mail");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		String input = "{\"asunto\":\"Alarma\",\"remitente\":\"m.rodriguez21@uniandes.edu.co\","
				+ "\"cuerpo\":\""+msg+"\","
				+ "\"destinatarios\": [\"jr.restom10@uniandes.edu.co\"]}";

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

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

}