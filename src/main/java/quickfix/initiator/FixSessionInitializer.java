package quickfix.initiator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.drools.core.io.impl.ClassPathResource;
import org.kie.api.io.Resource;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

public class FixSessionInitializer {
	
	public static final FixInitiator INITIATOR = new FixInitiator();

	public static void start() throws IOException {
		
		 // 准备启动参数
		 InputStream is = null;
         String filePath = "D:\\Self-Experiments\\experiments\\src\\main\\java\\quickfix\\initiator\\bridge-common.cfg";
         try {
             Resource resource = new ClassPathResource(filePath);
             is = resource.getInputStream();
         } catch (Exception e) {
             is = new FileInputStream(filePath);
         }
         if (is == null) {
             throw new RuntimeException("Failed to load bridge-common.cfg");
         }
         String template = IOUtils.toString(is, "utf-8");
         
         BridgeDetail bridgeDetail = new BridgeDetail();
         Map<String, String> replaceMap = new HashMap<>();
         replaceMap.put("{SenderCompID}", "SenderCompID=" + bridgeDetail.senderCompID);
         replaceMap.put("{TargetCompID}", "TargetCompID=" + bridgeDetail.targetCompID);
         replaceMap.put("{SocketConnectHost}", "SocketConnectHost=" + bridgeDetail.socketConnectHost);
         replaceMap.put("{SocketConnectPort}", "SocketConnectPort=" + bridgeDetail.socketConnectPort);
         replaceMap.put("{SessionQualifier}", "SessionQualifier=" + bridgeDetail.sessionQualifier);
         
         String bridgeCfg = template;
         for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
             bridgeCfg = bridgeCfg.replace(entry.getKey(), entry.getValue());
         }
         
         System.out.println(bridgeCfg);
        
        // 启动Initiator
        int index = 0;
     	SocketInitiator socketInitiator = initSocket(bridgeCfg, index);
        if (socketInitiator == null) {
        	System.out.println("****** SocketInitiator of is null****** " + index);
        	return;
        }
        
        try {
            socketInitiator.start();
            while (true) {
                if (socketInitiator.isLoggedOn()) {
                	System.out.println(index + "logged in successfully, added to SocketInitiatorMap");
                    break;
                }
            }
        } catch (ConfigError configError) {
            configError.printStackTrace();
            System.out.println(index + "failed to connect.");
        }
        is.close();
	}
	
	
	
	
    private static SocketInitiator initSocket(String bridgeCfg, int index) throws IOException {
        try {
        	InputStream is = IOUtils.toInputStream(bridgeCfg, StandardCharsets.UTF_8.name());            
            SessionSettings settings = new SessionSettings(is);
            MessageStoreFactory storeFactory = new FileStoreFactory(settings);
            MessageFactory messageFactory = new DefaultMessageFactory();
            SocketInitiator initiator = new SocketInitiator(INITIATOR, storeFactory, settings, messageFactory);
            System.out.println("initialized Fix SocketInitiator for" + index);
            return initiator;
        } catch (ConfigError configError) {
        	System.err.println(configError.getMessage());
        }
        return null;
    }
	
	
	static class BridgeDetail {
		public String mapping = "PrimeXM";
		public String userName = "primexm_APP test_q";
		public String password = "MF8N9hnFBSCG";
		public String senderCompID = "Q004";
		public String targetCompID = "XCD369";
		public String socketConnectHost = "pxmdemouk.primexm.com";
		public String socketConnectPort = "33166";
		public String sessionQualifier = "OZ-001";
	}
}
