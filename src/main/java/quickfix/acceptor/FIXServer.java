package quickfix.acceptor;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider.TemplateMapping;

public class FIXServer {

	public static ThreadedSocketAcceptor acceptor = null;
 
    public final Map<InetSocketAddress, List<TemplateMapping>> dynamicSessionMappings = new HashMap<>();
    
    public FIXServer(String propFile) throws ConfigError, FieldConvertError {
    	
        // 设置配置文件
        SessionSettings settings = new SessionSettings(propFile);

        // 设置一个APPlication
        Application application = new FixAcceptor();

        /**
         * <pre>
         * quickfix.MessageStore 有2种实现。
         *          quickfix.JdbcStore,quickfix.FileStore .
         * JdbcStoreFactory 负责创建JdbcStore,
         * FileStoreFactory 负责创建FileStorequickfix 默认用文件存储，因为文件存储效率高。
         */
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        acceptor = new ThreadedSocketAcceptor(application, storeFactory,settings, logFactory, messageFactory);
        configureDynamicSessions(settings, application, storeFactory,logFactory, messageFactory);
        
        
    }
    
    /**
     * <pre>
     * 以下几个方法是配置动态SessionProvider
     * FIX 支持同时支持不同的SessionTemple，使用不同的数据处理Provider
     * 体现在配置文件中的[session]标签
     * 
     */
    private void configureDynamicSessions(SessionSettings settings,Application application, MessageStoreFactory messageStoreFactory,LogFactory logFactory, MessageFactory messageFactory)
            throws ConfigError, FieldConvertError {
        
    	//获取配置文件中的[session]标签集合
        Iterator<SessionID> sectionIterator = settings.sectionIterator();
        while (sectionIterator.hasNext()) {
            SessionID sessionID = sectionIterator.next();
            if (isSessionTemplate(settings, sessionID)) { 
                //判断是否使用了AcceptorTemplate
                InetSocketAddress address = getAcceptorSocketAddress(settings,sessionID);
                getMappings(address).add(new TemplateMapping(sessionID, sessionID));
            }
        }

        // 手动关联一个 acceptor socket address  和一个 session provider instance
        for (Map.Entry<InetSocketAddress, List<TemplateMapping>> entry : dynamicSessionMappings.entrySet()) {
            acceptor.setSessionProvider(entry.getKey(),new DynamicAcceptorSessionProvider(settings, entry.getValue(), application, messageStoreFactory,logFactory, messageFactory));
        }
    }

    /**
     * Acceptor.SETTING_ACCEPTOR_TEMPLATE : AcceptorTemplate 是否打开
     */
    private boolean isSessionTemplate(SessionSettings settings,SessionID sessionID) throws ConfigError, FieldConvertError {
        return settings.isSetting(sessionID, Acceptor.SETTING_ACCEPTOR_TEMPLATE)&& settings.getBool(sessionID, Acceptor.SETTING_ACCEPTOR_TEMPLATE);
    }

    private List<TemplateMapping> getMappings(InetSocketAddress address) {
        List<TemplateMapping> mappings = dynamicSessionMappings.get(address);
        if (mappings == null) {
            mappings = new ArrayList<TemplateMapping>();
            dynamicSessionMappings.put(address, mappings);
        }
        return mappings;
    }

   /**
    * Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS : SocketAcceptAddress, 接收客户端连接所使用的本地地址localhost
    * Acceptor.SETTING_SOCKET_ACCEPT_PORT:  SocketAcceptPort, 接收客户端连接所使用的端口
    */
    private InetSocketAddress getAcceptorSocketAddress(SessionSettings settings, SessionID sessionID) throws ConfigError,FieldConvertError {
        String acceptorHost = "0.0.0.0";
        if (settings.isSetting(sessionID, Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS)) {
            acceptorHost = settings.getString(sessionID,Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS);
        }
        int acceptorPort = (int) settings.getLong(sessionID,Acceptor.SETTING_SOCKET_ACCEPT_PORT);
        InetSocketAddress address = new InetSocketAddress(acceptorHost,acceptorPort);
        return address;
    }


    void startServer() throws RuntimeError, ConfigError {
        acceptor.start();
    }

    public void stop() {
        acceptor.stop();
    }
}
