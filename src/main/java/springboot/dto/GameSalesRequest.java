package springboot.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class GameSalesRequest {

	private Date start;
	private Date end;
	
	private BigDecimal salesPrice;
	private boolean lessThan;
}
