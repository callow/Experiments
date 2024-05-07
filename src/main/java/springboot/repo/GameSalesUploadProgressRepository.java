package springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.GameSaleUploadProgress;

@Repository
public interface GameSalesUploadProgressRepository extends JpaRepository<GameSaleUploadProgress, Long>{
	
	
}
