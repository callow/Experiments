package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import springboot.dto.GameSalesRequest;
import springboot.model.GameSales;
import springboot.repo.GameSalesRepository;

@Service
@Transactional
public class GameSalesService {
	
	@Autowired
	private GameSalesRepository gameSalesRepository;
	

	@Async
	public String imports(MultipartFile file) {
		return null;
	}
	
	public Page<GameSales> getGameSales(GameSalesRequest request, Pageable pageable) {
		return null;
	}
}
