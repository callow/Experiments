package springboot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_sales")
public class GameSales {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "import_id")
	private Long importId;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImportId() {
		return importId;
	}

	public void setImportId(Long importId) {
		this.importId = importId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public LocalDateTime getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(LocalDateTime salesDate) {
		this.salesDate = salesDate;
	}
	
}
