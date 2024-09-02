package springboot.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.annotation.PostConstruct;
import springboot.exception.StorageException;

@Service
public class FileSystemStorageService {
	
	@Value("${file.location}")
	private String location;
	
	@Autowired
    ResourceLoader resourceLoader;

	private Path rootLocation;
	
	/**
	 *  resourceLoader.getResource: 获取编译后的resource目录 D:\Self-Experiments\experiments\target\classes
	 *  ApplicationHome: 获取编译前的路径
	 */
	@PostConstruct
    private void postConstruct() {
//		Resource resource = resourceLoader.getResource("classpath:uploadDir");  
//		String path = resource.getFile().getPath();
		ApplicationHome applicationHome = new ApplicationHome(this.getClass());
		String path = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath()+ "\\src\\main\\resources\\" + location;
		rootLocation = Paths.get(path);
	}
	

	/**
	 * 
	 * 存文件到指定位置：
	 * 	
	 */
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException("Cannot store file outside current directory.");
			}
			// 如果文件已存在就替换
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}
	
	/**
	 * 加载此文件夹下面的所有文件
	 */
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	
	/**
	 * 用于下载某个文件
	 */
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageException("Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageException("Could not read file: " + filename, e);
		}
	}
	
	/**
	 * 清空文件夹 or 文件
	 */
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
		System.out.println("successfully purge directory");
	}
	
	/**
	 * 创建文件夹
	 */
	
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			System.out.println("successfully create directory");
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
	
	
	@Async
	public void bulkImport(MultipartFile file, long total) {
        int counter = 0;
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream())).build()) {
        	System.out.println("start to import");
            String[] r;
            Map<String, Integer> headerMap = null;
            while ((r = reader.readNext()) != null) {
                counter++;
                if (counter == 1) {
//                    headerMap = getHeaderPosition(r); 填充header
                } else {
                    if (!CollectionUtils.isEmpty(headerMap) && headerMap.size() == 7) {
                        long vol = Long.parseLong(r[headerMap.get("volume")]);
                        if (vol <= Integer.MAX_VALUE) {
//                            runSqlByInterval(interval, r, headerMap);
                        }
                        total--;
                        if (total % 1000 == 0) {
                        	System.out.println("current leftover: " + total);
                        }
                    } else {
                    	System.err.println("kline import header is missing: " + headerMap);
                    }
                }
            }
        } catch (Exception e) {
        	System.err.println("import kline error");
        }
	}
	
}
