package uk.ac.soton.ldanalytics.piotre.server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.IOException;

@WebSocket
public class EventsWebSocket {
	private static Session session;

    @OnWebSocketConnect
    public void connected(Session session) {
        EventsWebSocket.session = session;
    }

    @OnWebSocketClose
    public void closed(int statusCode, String reason) {
        EventsWebSocket.session = null;
    }

    @OnWebSocketMessage
    public void message(String message) throws IOException {
        System.out.println("Got: " + message);   // Print message
        session.getRemote().sendString(message); // and send it back
    }
    
    public static void sendMessage(String message) throws IOException {
        System.out.println("Got: " + message);   // Print message
        session.getRemote().sendString(message); // and send it back
    }
}
