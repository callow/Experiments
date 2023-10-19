package disruptor.fun;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrrayBlockingQueueTest {

    public static void main(String[] args) {
        final ArrayBlockingQueue<Data> queue = new ArrayBlockingQueue<>(Constants.EVENT_NUM);
        final long startTime = System.currentTimeMillis();
        // produce
        new Thread(() -> {
            long i = 0;
            while (i < Constants.EVENT_NUM){
                Data data = new Data(i, "name:" + i);
                try {
                    queue.put(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();
        // consume
        new Thread(() -> {
            long k = 0;
            while (k < Constants.EVENT_NUM){
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                k++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("ArrayBlockingQueue costTime:" + (endTime - startTime) + "ms");
        }).start();

    }
}
