package springboot.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GameTotalSalesResponse {

	private int gamesSold;
	private BigDecimal totalSalesPrice;
	
}
