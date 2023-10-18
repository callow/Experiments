package disruptor.chain.handlers;

import java.util.UUID;

import com.lmax.disruptor.EventHandler;

import disruptor.chain.Trade;

public class TradeConsumer2 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler 2 :set ID");
        event.setId(UUID.randomUUID().toString());
	}

}
