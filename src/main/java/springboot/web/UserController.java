package springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.model.User;
import springboot.service.UserService;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
   	@PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
   		userService.save(user);
//      return ResponseEntity.ok(userService.findUser(user.getName()));
   		return ResponseEntity.status(HttpStatus.CREATED).body(userService.findUser(user.getName()));
    }
}
