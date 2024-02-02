package quickfix.acceptor;

import quickfix.ConfigError;
import quickfix.FieldConvertError;

public class AcceptorStarter {

	public static void main(String[] args) {
		FIXServer server;
		try {
			server = new FIXServer("D:\\Self-Experiments\\experiments\\src\\main\\java\\quickfix\\acceptor\\quickfix-server.cfg");
			server.startServer();
		} catch (ConfigError | FieldConvertError e) {
			e.printStackTrace();
		} 
        
	}
}
