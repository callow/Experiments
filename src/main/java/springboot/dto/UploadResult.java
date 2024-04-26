package springboot.dto;

import lombok.Data;

@Data
public class UploadResult {
	private int totalCount;
	private int successCount;
	private String failedMsg;
}
