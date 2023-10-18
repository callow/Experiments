package disruptor.chain.handlers;

import com.lmax.disruptor.EventHandler;

import disruptor.chain.Trade;

public class TradeConsumer5 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handler 5 : GET PRICE: " +  event.getPrice());
		event.setPrice(event.getPrice() + 3.0);		
	}

}
