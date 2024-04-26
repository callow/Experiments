package springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springboot.dto.UploadResult;
import springboot.model.GameSales;
import springboot.service.GameSalesService;

@Validated
@RestController
public class GameSalesController {
	
	@Autowired
	private GameSalesService gsService;
	
   	@PostMapping("/import")
    public ResponseEntity<UploadResult> createUser(@RequestPart(value = "file") MultipartFile upoadFile) {
		return null;
   		
    }
}