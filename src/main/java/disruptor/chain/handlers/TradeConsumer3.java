package disruptor.chain.handlers;

import com.lmax.disruptor.EventHandler;

import disruptor.chain.Trade;

public class TradeConsumer3 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handler 3 : NAME: " 
				+ event.getName() 
				+ ", ID: " 
				+ event.getId()
				+ ", PRICE: " 
				+ event.getPrice()
				+ " INSTANCE : " + event.toString());
	}

}
