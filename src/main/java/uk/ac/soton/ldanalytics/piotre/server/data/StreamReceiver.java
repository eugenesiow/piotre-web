package uk.ac.soton.ldanalytics.piotre.server.data;

import static uk.ac.soton.ldanalytics.piotre.server.Application.context;
import static uk.ac.soton.ldanalytics.piotre.server.Application.epService;

import java.lang.reflect.Type;
import java.util.Map;

import org.zeromq.ZMQ;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StreamReceiver implements Runnable  {
	
	private ZMQ.Socket receiver = null;
	private Gson gson = new Gson();
	
	public StreamReceiver(String clientAddress) {
		receiver = context.socket(ZMQ.PULL);
        receiver.bind(clientAddress);
	}

	@Override
	public void run() {
		while (!Thread.currentThread ().isInterrupted ()) {
        	String name = receiver.recvStr();
            String data = receiver.recvStr();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String,Object> dataMap = gson.fromJson(data,type);
            System.out.println(name + " " + dataMap);
            epService.getEPRuntime().sendEvent(dataMap, name);
        }
        receiver.close();
	}

}
