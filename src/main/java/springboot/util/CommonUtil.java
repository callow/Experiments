package springboot.util;

import org.springframework.web.multipart.MultipartFile;

import springboot.dto.UploadResult;

public class CommonUtil {
	
	public static final String[] HEADERS = {"id", "game_no", "game_name", "game_code","type","cost_price","tax", "sale_price", "date_of_sale"};
	
	public static UploadResult verify(MultipartFile file) {
		
		return new UploadResult();
	}
}
