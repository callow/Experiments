package quickfix.initiator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.core.util.StringUtils;

import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.BeginString;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateType;
import quickfix.field.MarketDepth;
import quickfix.field.NoQuoteEntries;
import quickfix.field.NoQuoteSets;
import quickfix.field.QuoteID;
import quickfix.field.SenderCompID;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.field.TargetCompID;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.MassQuote;
import quickfix.fix44.MassQuoteAcknowledgement;
import quickfix.fix44.component.Instrument;

public class RequestService {

	/**
	 * 产品订阅
	 */
	public static boolean subscribeBridgeProducts(String prod, SessionID sessionID) {
		
        MarketDataRequest message = new MarketDataRequest();
        Message.Header header = message.getHeader();
        // set message Header
        header.setField(new BeginString(sessionID.getBeginString())); // FIX.4.4
        header.setField(new SenderCompID(sessionID.getSenderCompID())); // VantageFX_UAT_Q
        header.setField(new TargetCompID(sessionID.getTargetCompID())); // Onezero_UAT_Q
        // set message body
        message.setField(new MDReqID(prod)); // 1
        message.setField(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_UPDATES)); // 1 = SNAPSHOT_PLUS_UPDATES
        message.setField(new MarketDepth(5)); // 5
        message.setField(new StringField(7533, prod.endsWith("+") ? "startrader_app_+" : "startrader_app_plain"));
        message.setField(new MDUpdateType(MDUpdateType.FULL_REFRESH)); // full / incremental

        MarketDataRequest.NoMDEntryTypes entryTypesGroup = new MarketDataRequest.NoMDEntryTypes();
        entryTypesGroup.set(new MDEntryType(MDEntryType.BID));
        message.addGroup(entryTypesGroup);
        entryTypesGroup.set(new MDEntryType(MDEntryType.OFFER));
        message.addGroup(entryTypesGroup);

        MarketDataRequest.NoRelatedSym noRelatedSymGroup = new MarketDataRequest.NoRelatedSym();
        noRelatedSymGroup.set(new Instrument(new Symbol(prod))); // EUR/USD
        message.addGroup(noRelatedSymGroup);

        try {
            return Session.sendToTarget(message, sessionID);
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
            return false;
        }
		
	}
	
	
	public static void processPrimXM(Message message, SessionID sessionID) {
        if (message instanceof MassQuote) {
            // 处理行情数据
            processMsg(message, sessionID);
            // 当收到行情消息后需要发送 Acknowledgement 消息
            sendAck(message, sessionID);

        } else if (message instanceof ExecutionReport) { // 非行情数据
        	System.out.println("抛单返回:" + message);
            try {
//                executionReportHandler.handleExecutionReport((ExecutionReport) message);
            } catch (Exception e) {
            	System.out.println("抛单返回异常:" + e);
            }
        }
	}
	
    private static final Map<String, Double> SYMBOL_BID_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Double> SYMBOL_ASK_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Double> SYMBOL_BSIZE_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Double> SYMBOL_ASIZE_MAP = new ConcurrentHashMap<>();
	private static void processMsg(Message message, SessionID sessionID) {
        /**
         * MassQuote 的数据结构如下 117=4 QuoteID 296=1 NoQuoteSets (indicates 1 QuoteSet group
         * will follow) 302=AP2 QuoteSetID (first tag of first QuoteSet) 295=1
         * NoQuoteEntries (indicates 1 QuoteEntry group will follow) 299=0 QuoteEntryID
         * (first tag of first QuoteEntry) 188=1.97471 BidSpotRate 190=1.97506
         * SellSpotRate
         */
        // 首先要获取到NoQuoteSets Group 从Group中取到其中的filed
        MassQuote.NoQuoteSets noQuoteSetsGroup = new MassQuote.NoQuoteSets();
        MassQuote.NoQuoteSets.NoQuoteEntries noQuoteEntriesGroup = new MassQuote.NoQuoteSets.NoQuoteEntries();

        double bidSpotRate;
        double askSpotRate;
        double bidSize = 0;
        double askSize = 0;

        String productName;
        int noQuoteSetsSize;
        int noQuoteEntriesSize;

        System.out.println("[收到行情：{}]" + message.toString().replace("\u0001", " "));
        try {
            noQuoteSetsSize = message.getField(new NoQuoteSets()).getValue();
            for (int noQuoteSetsIndex = 1; noQuoteSetsIndex <= noQuoteSetsSize; noQuoteSetsIndex++) {
                message.getGroup(noQuoteSetsIndex, noQuoteSetsGroup);
                productName = noQuoteSetsGroup.getQuoteSetID().getValue();

                if (StringUtils.isEmpty(productName)) {
                	System.out.println("PrimeXM processMsg invalid product name: " + productName);
                    continue;
                }


                // Check if the product is in quote period range setting in MTS
//                if (!isInQuotePeriod(productName)) {
//                    continue;
//                }


                noQuoteEntriesSize = noQuoteSetsGroup.getField(new NoQuoteEntries()).getValue();
                for (int quoteEntriesIndex = 1; quoteEntriesIndex <= noQuoteEntriesSize; quoteEntriesIndex++) {

                    // 获取Bid 和ask 价格,如果获取到最新的价格则保持到缓存，如果本次价格没有发生变动则取上传缓存的数据。
                    noQuoteSetsGroup.getGroup(quoteEntriesIndex, noQuoteEntriesGroup);
                    if (noQuoteEntriesGroup.isSetBidSpotRate()) {
                        bidSpotRate = noQuoteEntriesGroup.getBidSpotRate().getValue();
                        SYMBOL_BID_MAP.put(productName, bidSpotRate);
                    } else {
                        bidSpotRate = SYMBOL_BID_MAP.get(productName);
                    }

                    if (noQuoteEntriesGroup.isSetOfferSpotRate()) {
                        askSpotRate = noQuoteEntriesGroup.getOfferSpotRate().getValue();
                        SYMBOL_ASK_MAP.put(productName, askSpotRate);
                    } else {
                        askSpotRate = SYMBOL_ASK_MAP.get(productName);
                    }

                    // 获取Bid 和ask 交易量,如果获取到最新的价格则保持到缓存，如果本次价格没有发生变动则取上传缓存的数据。
                    if (noQuoteEntriesGroup.isSetBidSize()) {
                        bidSize = noQuoteEntriesGroup.getBidSize().getValue();
                        SYMBOL_BSIZE_MAP.put(productName, bidSize);
                    } /*
                     * else { bidSize = MapUtil.getProductBidSize(productName); }
                     */

                    if (noQuoteEntriesGroup.isSetOfferSize()) {
                        askSize = noQuoteEntriesGroup.getOfferSize().getValue();
                        SYMBOL_ASIZE_MAP.put(productName, askSize);
                    } /*
                     * else { askSize = MapUtil.getProductAskSize(productName); }
                     */
//                    LocalDateTime sendingTime = message.getHeader().getField(new SendingTime()).getValue();
//                    Tick tick = new Tick(sendingTime.toInstant(ZoneOffset.UTC).toEpochMilli(), askSpotRate, bidSpotRate,
//                            askSize, bidSize, productName);
//                    this.prepareSending(tick, sessionID);
                }
            }

        } catch (Exception e) {
        	System.err.println("primexm processMsg 收到行情：{}" + message.toString().replace("\u0001", " "));
            e.printStackTrace();
        }
    }
	
	   /**
     * @return
     * @description 发送行情确认消息。向fix server 发送 Acknowledgement
     * @author
     * @date
     * @Param
     */
    private static void sendAck(Message message, SessionID sessionID) {
        MassQuoteAcknowledgement mqack = new MassQuoteAcknowledgement();
        Message.Header header = mqack.getHeader();
        // set message Header
        header.setField(new BeginString(sessionID.getBeginString()));
        header.setField(new SenderCompID(sessionID.getSenderCompID()));
        header.setField(new TargetCompID(sessionID.getTargetCompID()));

        try {
            if (!message.isSetField(new QuoteID())) {
            	System.out.println("no quote id, don't send ack !");
            } else {
                QuoteID quoteID = new QuoteID(message.getField(new QuoteID()).getValue());
                System.out.println("with quote id ack ! {}, will send ack." + message.getField(new QuoteID()).getValue());
                mqack.set(quoteID);
                Session.sendToTarget(mqack, sessionID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static class Tick {
    	
    }
	
	
}
