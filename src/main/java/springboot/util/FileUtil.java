package springboot.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	/**
	 * 上传文件的总行数
	 */
	public static long getCount(final MultipartFile file) throws IOException {
		long total;
        ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(bis))) {
            total = br.lines().count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return total;
	}
}
