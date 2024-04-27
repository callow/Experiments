package springboot.websocketA;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class OrderHandler extends TextWebSocketHandler{
	
	public static final HashMap<String, WebSocketSession> userSessionMap;
	 
    static {  
        userSessionMap = new HashMap<>();
    }  
  
    /** 
     * 连接成功时候，会触发UI上onopen方法 
     */  
    @Override  
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {  
        System.out.println("connect to the websocket success......");  
        Map<String, Object> attributes = session.getAttributes();
        String userCode = (String) attributes.get("userCode");
        if(userCode != null) {
        	userSessionMap.put(userCode, session);
        }
    }  
  
    /** 
     * 在UI在用js调用websocket.send()时候，会调用该方法 
     */  
    @Override  
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {  
    	System.out.println("Received Msg : " + message + " from :" + session.getId() + " userCode: " + session.getAttributes().get("userCode"));
    	System.out.println(session.toString());
        sendMessageToUsers(session, new TextMessage("收到！"));  
    }  
  
    /** 
     * 给某个用户发送消息 
     * 
     * @param userName 
     * @param message 
     */  
    public void sendMessageToUser(String userName, TextMessage message) {  
    }  
  
    /** 
     * 给所有在线用户发送消息 
     * 
     * @param message 
     * @throws IOException 
     */  
    public void sendMessageToUsers(WebSocketSession session, TextMessage message)  {
    	try {
			session.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }  
  
    @Override  
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {  
        if (session.isOpen()) {  
            session.close();  
        }
        Map<String, Object> attributes = session.getAttributes();
		String userCode = (String) attributes.get("userCode");
		if(userCode != null) {
			userSessionMap.remove(userCode);
		}
    }  
  
    @Override  
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		Map<String, Object> attributes = session.getAttributes();
		String userCode = (String) attributes.get("userCode");
		if(userCode != null) {
			userSessionMap.remove(userCode);
		}
    }  
  
    @Override  
    public boolean supportsPartialMessages() {  
        return false;  
    }  
}
