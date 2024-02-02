package quickfix.initiator;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Logon;

public final class FixInitiator implements Application {
	
	public static final String PRODUCT = "AUDUSD";

	@Override
	public void onCreate(SessionID sessionId) {
		
	}

	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("-------------------Fix 登录成功---------------------" + sessionId.getSessionQualifier());
		System.out.println(sessionId.toString());
		RequestService.subscribeBridgeProducts(PRODUCT,sessionId);
	}

	@Override
	public void onLogout(SessionID sessionId) {
		System.out.println("-------------------Fix 退出成功---------------------" + sessionId.getSessionQualifier());
		System.out.println(sessionId.toString());
	}

	/**
	 * 登录
	 */
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		  if (message instanceof Logon) {
            try {
                Logon logon = (Logon) message;
                System.out.println("Start to login fix >>>>set logon username / password" + sessionId.getSenderCompID());
                logon.set(new quickfix.field.Username("primexm_APP test_q"));
                logon.set(new quickfix.field.Password("MF8N9hnFBSCG"));
                logon.set(new quickfix.field.ResetSeqNumFlag(true));
            } catch (Exception exception) {
                throw new RuntimeException("Username and Password must be specified in configuration");
            }
	      }
		  System.out.println("发给Acceptor: " + message.toString());
//		  System.out.println("toAdmin " + message.toString().replace("\u0001", " "));
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		System.out.println("收到Acceptor Admin: " + message.toString());
//		System.out.println("fromAdmin " + message.toString().replace("\u0001", " "));
	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
//		System.out.println("toApp " + message.toString().replace("\u0001", " "));
		System.out.println("发给Acceptor App: " + message.toString());
	}

	/**
	 * 接收行情
	 */
	
	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		System.out.println("收到Acceptor App: from OZ/PrimXM " + message.toString());
//		System.out.println("from OZ/PrimXM" + message.toString().replace("\u0001", " "));
		
		// process OZ
//		RequestService.processOneZero(message, sessionId);
		
		// process PrimeXM
		RequestService.processPrimXM(message, sessionId);
	}

}
