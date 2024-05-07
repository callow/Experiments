package springboot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GameTotalSalesRequest {

	private Date start;
	private Date end;
	
	private Integer gameNo;
}
