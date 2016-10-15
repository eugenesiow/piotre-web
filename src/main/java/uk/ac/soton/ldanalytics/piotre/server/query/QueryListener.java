package uk.ac.soton.ldanalytics.piotre.server.query;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.zeromq.ZMQ.Socket;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uk.ac.soton.ldanalytics.piotre.server.EventsWebSocket;

public class QueryListener implements UpdateListener {
	private String queryName;
	private Socket sender;
	
	public QueryListener(String queryName, Socket sender) {
		this.sender = sender;
		this.queryName = queryName;
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if(newEvents.length>0) {
			JsonObject message = new JsonObject();
			message.addProperty("queryName", queryName);
			for(int i=0;i<newEvents.length;i++) {
				for(Object map:((Map<?, ?>)newEvents[i].getUnderlying()).entrySet()) {
					Entry<?, ?> entry = ((Entry<?, ?>)map);
//					System.out.println(entry.getKey()+":"+entry.getValue());
					if(entry.getKey()!=null && entry.getValue()!=null) {
						String key = entry.getKey().toString();
						String val = entry.getValue().toString();
						if(message.has(key)) {
							JsonElement jVal = message.get(key);
							JsonArray newVal = new JsonArray();
							if(jVal.isJsonArray()) {
								newVal = jVal.getAsJsonArray();
							} else if(jVal.isJsonPrimitive()) {
								newVal.add(jVal.getAsString());
							}
							newVal.add(val);
							message.add(key, newVal);
						} else {
							message.addProperty(key,val);
						}
					}
				}
			}
//			System.out.println(message.toString());
			sender.send(message.toString());
//			try {
//				EventsWebSocket.sendMessage(message.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}

}
