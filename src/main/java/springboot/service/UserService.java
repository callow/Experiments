package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.model.User;
import springboot.repo.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User findUser(String name) {
		return userRepository.findUserByName(name);
	}
	
	public boolean save(User user){
		userRepository.save(user);
		return true;
	}

}
