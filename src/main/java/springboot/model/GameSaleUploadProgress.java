package springboot.model;

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
@Table(name = "game_sales_upload_progress")
public class GameSaleUploadProgress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "total")
	private int total;
	
	@Column(name = "finished")
	private int finished;
	
	@Column(name = "time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Column(name = "time_spend")
	private int timeSpend;
	
}
