package springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.GameSalesUploadFailure;

@Repository
public interface GameSalesFailureRepository extends JpaRepository<GameSalesUploadFailure, Long>{
	
	
}
