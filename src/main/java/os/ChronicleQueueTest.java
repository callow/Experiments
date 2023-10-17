package os;

import java.io.File;
import java.nio.file.Files;

import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

/**
 * 
 * Excerpt – is a data container
   Appender – appender is used for writing data
   Trailer – is used for sequentially reading data
 *
 */
public class ChronicleQueueTest {
	
	public static void main(String[] args) {
		
		/**
		 * 写入/读取数据
		 */
		// ${java.io.tmpdir}/queue-dir/{today}.cq4
		try (ChronicleQueue queue = SingleChronicleQueueBuilder.single("queue-dir").build()) {
		    // Obtain an ExcerptAppender
		    ExcerptAppender writer = queue.createAppender();

		    // Writes: {msg: TestMessage}
		    writer.writeDocument(w -> w.write("msg").text("TestMessage"));

		    // Writes: TestMessage
		    writer.writeText("TestMessage");

		    ExcerptTailer reader = queue.createTailer();

		    reader.readDocument(w -> System.out.println("msg: " + w.read(()->"msg").text()));
		}
		
		
		
		
		
		
	}
}
