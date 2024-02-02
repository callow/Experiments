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
		System.out.println("服务器启动");
	}

	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("客户端登陆成功");
	}

	@Override
	public void onLogout(SessionID sessionId) {
		System.out.println("客户端断开连接");
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		System.out.println("发送会话消息: " + message.toString());
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		System.out.println("接收会话类型消息");
        try {
            crack(message, sessionId);
        } catch (UnsupportedMessageType | FieldNotFound | IncorrectTagValue e) {
            e.printStackTrace();
        }

	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		 System.out.println("发送业务消息: " + message.toString());
	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		System.out.println("接收业务消息时调用此方法");
        crack(message, sessionId);
	}
	
	/**
	 * 收到客户端发来的不同的消息 干啥？
	 */
	@Override
	protected void onMessage(Message message, SessionID sessionID) {
        try {
            System.out.println("业务逻辑实现统一写在此方法中");
            String msgType = message.getHeader().getString(35);
            Session session = Session.lookupSession(sessionID);
            System.out.println("服务器接收到用户信息订阅==" + msgType);
            switch (msgType) {
            case MsgType.LOGON: // 登陆
                session.logon();
                session.sentLogon();
                break;
            case MsgType.HEARTBEAT: // 心跳
                session.generateHeartbeat();
                break;
            }

        } catch (FieldNotFound e) {
            e.printStackTrace();
        }
	}
}
