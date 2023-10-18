package disruptor.chain.handlers;

import com.lmax.disruptor.EventHandler;

import disruptor.chain.Trade;

public class TradeConsumer4 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handler 4 : SET PRICE");
		event.setPrice(17.0);
	}

}
