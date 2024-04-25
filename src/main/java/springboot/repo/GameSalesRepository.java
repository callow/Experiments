package springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.GameSales;

@Repository
public interface GameSalesRepository extends JpaRepository<GameSales, Long>{
	
	
}
