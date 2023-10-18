package disruptor.multi;

import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.WorkHandler;

import disruptor.comparison.Order;

public class OrderConsumer implements WorkHandler<Order> {
    private String consumerId;

    private static AtomicInteger count = new AtomicInteger(0);

    public OrderConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Order order) throws Exception {
        System.err.println("Consumer：" + consumerId +  ",Order ID:" + order.getOrderId());
        count.incrementAndGet();
    }
    public int getCount(){
        return count.get();
    }
}
