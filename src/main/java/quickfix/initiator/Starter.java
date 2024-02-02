package quickfix.initiator;

import java.io.IOException;

public class Starter {

	public static void main(String[] args) {
		try {
			FixSessionInitializer.start();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
