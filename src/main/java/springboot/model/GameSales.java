package springboot.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


@Data
@Entity
@Table(name = "game_sales")
public class GameSales {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "sales_id") // a running number starts with 1
	private Long salesId;
	
	@Column(name = "game_no")  // an integer value between 1 to 100
	private int gameNo;
	
	@Column(name = "game_name", length = 20) // a string value not more than 20 characters
	private String gameName;
	
	@Column(name = "game_code", length = 5) // a string value not more than 5 characters
	private String gameCode;
	
	@Column(name = "type") // an integer, 1 = Online | 2 = Offline
	private int type;
	
	@Column(name = "cost_price")
	private BigDecimal costPrice; // decimal value not more than 100
	
	@Column(name = "tax")
	private BigDecimal tax = new BigDecimal("0.09"); // 9%
	
	@Column(name = "sale_price")
	private BigDecimal salesPrice; // decimal value, cost_price inclusive of tax
	
	@Column(name = "date_of_sale")
	@Temporal(TemporalType.TIMESTAMP) 
	private Date salesDate; // a timestamp of the sale

}
