package springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springboot.dto.GameSalesRequest;
import springboot.dto.UploadResult;
import springboot.model.GameSales;
import springboot.service.GameSalesService;

@Validated
@RestController
public class GameSalesController {
	
	@Autowired
	private GameSalesService gsService;
	
	/**
	 *  1.Design and create a database table called ‘game_sales’ that will store the content of the CSV.
	 *  2.Design and create the necessary tables to track the progress of the CSV import.
	 */
   	@PostMapping("/import")
    public ResponseEntity<UploadResult> importSales(@RequestPart(value = "file") MultipartFile upoadFile) {
		return ResponseEntity.ok(gsService.imports(upoadFile));
    }
   	
   	/**
   	 * 1.A list of game sales
   	 * 2.A list of game sales during a given period.
   	 * 3.A list of game sales where sale_price is less than or more than a given parameter.
   	 */
   	@PostMapping("/getGameSales")
   	public ResponseEntity<Page<GameSales>> getGameSales(@RequestBody GameSalesRequest req, @PageableDefault(size = 100) Pageable pageable) {
		return ResponseEntity.ok(gsService.getGameSales(req, pageable));
   	}
   	
   	
   	
   	
}
