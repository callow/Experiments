package springboot.websocketB;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session)
    {
        onlineNumber++;
        System.out.println("现在来连接的客户id："+session.getId()+"用户名："+username);
        this.username = username;
        this.session = session;
        System.out.println("有新连接加入！ 当前在线人数" + onlineNumber);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            //先给所有人发送通知，说我上线了
            Map<String,Object> map1 = new HashMap<>();
            map1.put("messageType",1);
            map1.put("username",username);
            sendMessageAll(map1.toString(),username);
 
            //把自己的信息加入到map当中去
            clients.put(username, this);
            //给自己发一条消息：告诉自己现在都有谁在线
            Map<String,Object> map2 = new HashMap<>();
            map2.put("messageType",3);
            //移除掉自己
            Set<String> set = clients.keySet();
            map2.put("onlineUsers",set);
            sendMessageTo(map2.toString(),username);
        }
        catch (IOException e){
        	System.out.println(username+"上线的时候通知所有人发生了错误");
        }
 
 
 
    }
 
    @OnError
    public void onError(Session session, Throwable error) {
    	System.out.println("服务端发生了错误"+error.getMessage());
        //error.printStackTrace();
    }
    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;
        //webSockets.remove(this);
        clients.remove(username);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> map1 = new HashMap<>();
            map1.put("messageType",2);
            map1.put("onlineUsers",clients.keySet());
            map1.put("username",username);
            sendMessageAll(map1.toString(),username);
        }
        catch (IOException e){
        	System.out.println(username+"下线的时候通知所有人发生了错误");
        }
        System.out.println("有连接关闭！ 当前在线人数" + onlineNumber);
    }
 
    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        try {
        	System.out.println("来自客户端消息：" + message+"客户端的id是："+session.getId());
            String textMessage = "haha";
            String fromusername = "songlei";
            String tousername = "allen";
            //如果不是发给所有，那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> map1 = new HashMap<>();
            map1.put("messageType",4);
            map1.put("textMessage",textMessage);
            map1.put("fromusername",fromusername);
            if(tousername.equals("All")){
                map1.put("tousername","所有人");
                sendMessageAll(map1.toString(),fromusername);
            }
            else{
                map1.put("tousername",tousername);
                sendMessageTo(map1.toString(),tousername);
            }
        }
        catch (Exception e){
        System.out.println("发生了错误了");
        }
 
    }
 
 
    public void sendMessageTo(String message, String ToUserName) throws IOException {
        for (WebSocket item : clients.values()) {
            if (item.username.equals(ToUserName) ) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }
 
    public void sendMessageAll(String message,String FromUserName) throws IOException {
        for (WebSocket item : clients.values()) {
                item.session.getAsyncRemote().sendText(message);
        }
    }
 
    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }
 
}
