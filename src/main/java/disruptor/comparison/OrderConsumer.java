package disruptor.comparison;

import com.lmax.disruptor.EventHandler;

public class OrderConsumer implements EventHandler<Order>{

	@Override
	public void onEvent(Order order, long sequence, boolean endOfBatch) throws Exception {
		 System.out.println("Consuming order ID:" + order.getOrderId());
	}

}


