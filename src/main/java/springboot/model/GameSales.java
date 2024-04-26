package springboot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@Entity
@Table(name = "game_sales")
public class GameSales {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "sales_id")
	private Long salesId;
	
	@Column(name = "game_no", length = 100)
	private int gameNo;
	
	@Column(name = "game_name", length = 20)
	private String gameName;
	
	@Column(name = "game_code", length = 5)
	private String gameCode;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "cost_price")
	private BigDecimal costPrice;
	
	@Column(name = "tax")
	private BigDecimal tax = new BigDecimal("0.09");
	
	@Column(name = "sale_price")
	private BigDecimal salesPrice;
	
	@Column(name = "date_of_sale")
	private LocalDateTime salesDate;

}
