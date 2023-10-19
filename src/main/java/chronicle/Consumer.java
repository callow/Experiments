package chronicle;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

public class Consumer extends Thread{

	@Override
	public void run() {
		try (ChronicleQueue queue = SingleChronicleQueueBuilder.single("queue-dir").rollCycle(RollCycles.FIVE_MINUTELY).build()) {
		    ExcerptTailer reader = queue.createTailer("a");
		    while(!reader.isClosed()) {
		    	reader.readDocument(w -> System.out.println("consuming: " + w.read(()->"data").text()));
		    }    
		    queue.close();
		    System.out.println("finish");
		}		
	}

}
