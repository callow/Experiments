package springboot.util;

import static java.time.LocalDateTime.now;

import java.util.stream.Stream;

import org.springframework.scheduling.support.CronExpression;

public class SpringUtil {

	
    /**
     * Enumerate 10 samples for a cron expression
     *
     * @param cron
     */
    public static void enumerate(String cron) {
    	System.out.println();
        Stream.iterate(now(), CronExpression.parse(cron)::next).limit(10).forEach(i -> System.out.println(i));
    }
}
