package chronicle;

import java.util.Random;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

public class Producer extends Thread {
	
	static Random RANDOM = new Random();


	@Override
	public void run() {
		try (ChronicleQueue queue = SingleChronicleQueueBuilder.single("queue-dir").build()) {
			while(true) {
			    ExcerptAppender writer = queue.createAppender();
			    for(int i = 1; i <= 10000; i++) {
			    	String c = "Customer-" + RANDOM.nextInt();
				    writer.writeDocument(w -> w.write("data").text(c)); // Writes: {msg: Customer-1}
			    }
			    System.out.println("  ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
