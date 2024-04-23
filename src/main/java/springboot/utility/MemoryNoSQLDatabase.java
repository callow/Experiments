package springboot.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import springboot.model.Author;
import springboot.model.Book;
import springboot.model.User;

public class MemoryNoSQLDatabase {

	public static final Map<String, Book> BOOK_MAP = new HashMap<>();
	public static final Map<String,User> USER_MAP = new HashMap<>();
	
	/**
	 * prepopulate some data in the memory
	 */
	static {
		USER_MAP.put("wendel", new User("wendel","123", "admin", "wendel123"));
		USER_MAP.put("john", new User("john","dsds", "john123", "john123"));
		USER_MAP.put("lucy", new User("lucy","12323", "lucy323", "lucy123"));
		
		BOOK_MAP.put("12345", new Book("12345", "ring of thor", 
				List.of(new Author("wendell",new Date()),new Author("susany",new Date())), 2032, 2.3, "fiction"));
		
	}
	
	public static void addOrUpdate(Book book) {
		BOOK_MAP.put(book.getIsbn(), book);
	}
	
	public static void delete(String isbn) {
		BOOK_MAP.remove(isbn);
	}
	
	public static List<Book> findByTitle(String title) {
		return BOOK_MAP.values().stream().filter(i-> i.getTitle().equals(title)).collect(Collectors.toList());
	}
	
	public static User getUserByName(String name) {
		return USER_MAP.get(name);
	}
}
