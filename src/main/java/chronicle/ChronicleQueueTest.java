package chronicle;

/**
 * 
 * Excerpt – is a data container
   Appender – appender is used for writing data
   Trailer – is used for sequentially reading data
   
   FileChannel -> used to do mmap
 *
 */
public class ChronicleQueueTest {
	
	public static void main(String[] args) {
		
		/**
		 * 写入/读取数据
		 */
		// ${java.io.tmpdir}/queue-dir/{today}.cq4
//		try (ChronicleQueue queue = SingleChronicleQueueBuilder.single("queue-dir").rollCycle(RollCycles.FIVE_MINUTELY).build()) {
//		    ExcerptAppender writer = queue.createAppender();
//		    
//		    for(int i = 0; i <= 10; i++) {
//		    	String c = "Customer-" + i;
//			    writer.writeDocument(w -> w.write("msg").text(c)); // Writes: {msg: Customer-1}
//		    }
//		    writer.close();
//		    
//		    ExcerptTailer reader = queue.createTailer("a");
//		    while(!reader.isClosed()) {
//		    	reader.readDocument(w -> System.out.println("msg: " + w.read(()->"msg").text()));
//		    }    
//		    
//		    queue.close();
//		    System.out.println("finish");
//		}
		
		System.out.println("producer start!");
		new Producer().start();
		System.out.println("consumer start!");
		new Consumer().start();
	}
}
