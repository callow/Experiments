package springboot.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import springboot.model.Book;
import springboot.model.User;
import springboot.utility.JWTUtils;
import springboot.utility.MemoryNoSQLDatabase;

@Validated
@RestController
public class BookController {

	@PostMapping("/book/update")
    public ResponseEntity<Boolean> addOrUpdate(@RequestBody Book book) {
		MemoryNoSQLDatabase.addOrUpdate(book);
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/book/delete")
    public ResponseEntity<Boolean> delete(HttpServletRequest request, @RequestParam("isbn") String isbn) {
		boolean isAdmin = (boolean) request.getAttribute("admin");
		
		if (isAdmin) {
			MemoryNoSQLDatabase.delete(isbn);
			return ResponseEntity.ok(true);
		} else {
			System.err.println("unauthorized access!");
			return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(false);
		}
	}
	
	@GetMapping("/book/find-title")
    public ResponseEntity<List<Book>> findByTitle(@RequestParam("title") String title) {
		return ResponseEntity.ok(MemoryNoSQLDatabase.findByTitle(title));
	}
	
   	@PostMapping("/login")
    public Map<String,Object> login(String username, String password) {
   		Map<String,Object> map = new HashMap<>();
   		User user = MemoryNoSQLDatabase.getUserByName(username);
   		
   		if (user != null && user.getPassword().equals(password)) {
   			
   			Map<String,String> payload = new HashMap<>();
   			payload.put("userId", user.getUserId());
   			payload.put("nickname", user.getNickname());
   			
   			map.put("state", true);
   			map.put("token", JWTUtils.getToken(payload));
   			map.put("msg", "success");
   			return map;
   		}
   		
   		map.put("state", false);
		map.put("msg", "fail");
		return map;
    }
}
