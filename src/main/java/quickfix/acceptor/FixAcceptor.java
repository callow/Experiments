package quickfix.acceptor;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgType;
/**
 * 
 * MessageCracker是一个工具类，通过继承MessageCracker可以覆盖onMessage方法
 * 通过调用crack回调onMessage中的业务逻辑。所以所有的业务逻辑可以直接写在onMessage 方法中
 *
 */
public class FixAcceptor extends MessageCracker implements Application{

	@Override
	public void onCreate(SessionID sessionId) {
	}

	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("登录: " + sessionId.toString());
	}

	@Override
	public void onLogout(SessionID sessionId) {
		System.out.println("退出: " + sessionId.toString());
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		System.out.println("发给客户端Admin: " + message.toString());
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		System.out.println("收到客户端Admin: " + message);
        try {
            crack(message, sessionId);
        } catch (UnsupportedMessageType | FieldNotFound | IncorrectTagValue e) {
            e.printStackTrace();
        }

	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		 System.out.println("发给客户端App: " + message.toString());
	}

	/**
	 * 接收到market data request
	 */
	@Override
	public void fromApp(Message message, SessionID sessionId)throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		System.out.println("收到客户端App： " + message);
        crack(message, sessionId);
	}
	
	/**
	 * 收到客户端发来的不同的消息 干啥？ 业务逻辑实现统一写在此方法中
	 */
	@Override
	protected void onMessage(Message message, SessionID sessionID) {
        try {
            String msgType = message.getHeader().getString(35);
            Session session = Session.lookupSession(sessionID);
            
            switch (msgType) {
	            case MsgType.LOGON: // 登陆
	            	System.out.println("收到客户端的 LOGON消息： " + message);
	                session.logon();
	                session.sentLogon();
	                session.generateHeartbeat();
	                break;
	            case MsgType.HEARTBEAT: // 心跳
	            	System.out.println("收到客户端 HEARTBEAT的消息： " + message);
	                session.generateHeartbeat();
	                break;
	            case MsgType.MARKET_DATA_REQUEST:
	            	System.out.println("收到客户端 MARKET_DATA_REQUEST的消息： " + message);
	            	session.generateHeartbeat();
	            	break;
	            default:
	            	break;
            }

        } catch (FieldNotFound e) {
            e.printStackTrace();
        }
	}
}
