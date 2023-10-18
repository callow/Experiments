package disruptor.comparison;

import com.lmax.disruptor.RingBuffer;

public class OrderProducer {

    private RingBuffer<Order> ringBuffer;

    public OrderProducer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(long orderId){
        long sequence = ringBuffer.next();
        try{
            Order order = ringBuffer.get(sequence);
            order.setOrderId(orderId);
        }finally {
            ringBuffer.publish(sequence);
        }


    }
}
