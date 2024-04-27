package springboot.web;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springboot.exception.StorageException;
import springboot.service.FileSystemStorageService;

@RestController
public class FileController {

	@Autowired
	private FileSystemStorageService storageService;

	@GetMapping("/list")
	public List<String> listUploadedFiles() throws IOException {
		return storageService.loadAll().map(i -> i.getFileName().toString()).collect(Collectors.toList());
	}

	@GetMapping("/download/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/upload")
	public ResponseEntity<Boolean> handleFileUpload(@RequestParam("file") MultipartFile file) {
		storageService.store(file);
		return ResponseEntity.status(200).body(true);
	}
	
	/**
	 * 特别处理某个处理异常
	 */
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageException exc) {
		return ResponseEntity.notFound().build();
	}
}
