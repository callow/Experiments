package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import springboot.dto.UploadResult;
import springboot.repo.GameSalesRepository;

@Service
@Transactional
public class GameSalesService {
	
	@Autowired
	private GameSalesRepository gsRepo;
	
	
	public UploadResult imports(MultipartFile file) {
		return null;
	}


}
