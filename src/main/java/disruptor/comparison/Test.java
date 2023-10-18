package disruptor.comparison;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class Test {

	  public static void main(String[] args) {
	        OrderFactory orderEventFactory = new OrderFactory();
	        // 2^
	        int ringBufferSize = 1024;
	        
	        // thread pool
//	        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	        ThreadFactory threads = DaemonThreadFactory.INSTANCE;
	        WaitStrategy strategy = new BlockingWaitStrategy();

	        /**
	         * 1、orderEventFactory  :消息（Event） 工厂对象
	         * 2、ringBufferSize :容器长度
	         * 3、executor:线程池（建议使用自定义线程池）,
	         * 4、ProducerType： 单生产，还是多生产
	         * 5、waitStrategy:等待策略  ：当队列数据满了或者没有数据的时候，生产者和消费者的等待策略
	         */
	        // 1、initiate
	        Disruptor<Order> disruptor = new Disruptor<Order>(orderEventFactory,ringBufferSize,threads,ProducerType.SINGLE, strategy);
	        // 2. set consumer
	        disruptor.handleEventsWith(new OrderConsumer());
	        // 3、start
	        disruptor.start();
	        
	        // 4、produce
	        RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
	        OrderProducer producer = new OrderProducer(ringBuffer);
	        for(long i = 0; i < 100;i ++){
	            producer.sendData(i);
	        }
	        // 5. shutdown
	        disruptor.shutdown();

	    }
}
