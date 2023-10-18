package disruptor.chain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import disruptor.chain.handlers.TradeConsumer1;
import disruptor.chain.handlers.TradeConsumer2;
import disruptor.chain.handlers.TradeConsumer3;
import disruptor.chain.handlers.TradeConsumer4;
import disruptor.chain.handlers.TradeConsumer5;

public class Test {
	
	private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(5);
	private static final int RING_SIZE = 1024* 1024;
	
	public static void main(String[] args) {
        
        // 1、 构建Disruptor
        Disruptor<Trade> disruptor = new Disruptor<Trade>(new TradeFactory(), RING_SIZE, THREAD_POOL,ProducerType.SINGLE, new BusySpinWaitStrategy());
        
        // 2、 把消费者设置再Disruptor中的handleEventsWith
        chain(disruptor);
        
        parallel(disruptor);

        diamond(disruptor);

        ploygon(disruptor);
        
        
        // 3、启动disruptor
        disruptor.start();
        long startTime = System.currentTimeMillis();

        TradeProducer producer = new TradeProducer(disruptor.getRingBuffer());
        
        producer.sendData();
        
        disruptor.shutdown();
        THREAD_POOL.shutdown();
        long endTime = System.currentTimeMillis();
        System.err.println("总耗时： " +(endTime - startTime));
	}
	
	
	
	
	/**
     * 2.1 使用串行操作
     */
	private static void chain(Disruptor<Trade> disruptor) {
      disruptor.handleEventsWith(new TradeConsumer1())
      			.handleEventsWith(new TradeConsumer2())
      			.handleEventsWith(new TradeConsumer3());
	}
	
    /**
     * 2.2 并行操作 ：可以分两种方式添加
     * 1、handleEventWith 方法，添加多个handler实现即可
     * 2、handleEventWith方法，分开调用
     */
	private static void parallel(Disruptor<Trade> disruptor) {
	  disruptor.handleEventsWith(new TradeConsumer1(),new TradeConsumer2(),new TradeConsumer3());
	  // or
      disruptor.handleEventsWith(new TradeConsumer1());
      disruptor.handleEventsWith(new TradeConsumer2());
      disruptor.handleEventsWith(new TradeConsumer3());
	}
	
    /**
     * 2.3 菱形操作
     */
	private static void diamond(Disruptor<Trade> disruptor) {
      // 2.3.1 菱形操作一
      disruptor.handleEventsWith(new TradeConsumer1(),new TradeConsumer2())
              .handleEventsWith(new TradeConsumer3());
      // 2.3.2 菱形操作二
      EventHandlerGroup<Trade> eventHandlerGroup = disruptor.handleEventsWith(new TradeConsumer1(), new TradeConsumer2());
      eventHandlerGroup.then(new TradeConsumer3());
	}
	
	/**
	 * 
	 * 2.4 多边形
	 */
	private static void ploygon(Disruptor<Trade> disruptor) {
		TradeConsumer1 h1 = new TradeConsumer1();
		TradeConsumer2 h2 = new TradeConsumer2();
		TradeConsumer3 h3 = new TradeConsumer3();
		TradeConsumer4 h4 = new TradeConsumer4();
		TradeConsumer5 h5 = new TradeConsumer5();
		
        disruptor.handleEventsWith(h1,h4);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2,h5).handleEventsWith(h3);
	}
}
