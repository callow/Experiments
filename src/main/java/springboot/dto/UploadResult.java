package springboot.dto;

import lombok.Data;

@Data
public class UploadResult {
	private boolean successCount;
	private String failedMsg;
}
