package springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springboot.service.FileSystemStorageService;

@SpringBootApplication
public class SongLeiAppliation {
 
    public static void main(String[] args) {
        SpringApplication.run(SongLeiAppliation.class, args);
    }
    
    /**
     * 启动过后删空并初始化文件夹
     */
    @Bean
	CommandLineRunner init(FileSystemStorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
