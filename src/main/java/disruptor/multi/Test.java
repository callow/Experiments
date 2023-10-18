package disruptor.multi;

import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

import disruptor.comparison.Order;
import disruptor.comparison.OrderProducer;


public class Test {

    public static void main(String[] args) throws InterruptedException {

       // 1、RingBuffer 这里和创建Disruptor有点区别，创建Disruptor需要指定线程池，如果是多生产多消费者模式，就不需要线程池了
       RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,() -> new Order(), 1024, new YieldingWaitStrategy());
       
       //2、通过ringBuffer创建一个屏障
       SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
       
       // 3、创建多个消费者组
       OrderConsumer[] consumers = new OrderConsumer[10];
       for(int i = 0 ;i < consumers.length;i++){
           consumers[i] = new OrderConsumer("C" + i);
       }
       // 4、构建多消费者工作池
       WorkerPool<Order> workerPool = new WorkerPool<>(ringBuffer,sequenceBarrier,new EventExceptionHandler(),consumers);
       
       // 5、设置多消费者sequence序号，用于单独统计消费者进度
       ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
       
       // 6、 启动WorkPool
       workerPool.start(Executors.newFixedThreadPool(10));

       CyclicBarrier latch = new CyclicBarrier(100);
       for(int i = 0 ;i < 100;i++){
           OrderProducer producer = new OrderProducer(ringBuffer);
           new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       latch.await();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   } catch (BrokenBarrierException e) {
                       e.printStackTrace();
                   }
                   for(int j = 0 ;j < 100;j++){
                       producer.sendData(UUID.randomUUID().variant());
                   }
               }
           }).start();
       }
       System.err.println("======线程创建完毕，开始生产数据==========");
       Thread.sleep(2 *1000);
       System.err.println("任务总的数量：" + consumers[0].getCount());
   }
}
