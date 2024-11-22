package crawaler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Runner {
	private static final String EMPTY = "";
	private static final List<Risk> RISKS = new ArrayList<>();
	static final ExecutorService executor = Executors.newFixedThreadPool(1);
	
	public static void main(String[] args) {
		String[] cves = getSRA();
		System.out.println(cves.length);
		CountDownLatch countDownLatch = new CountDownLatch(cves.length);
		try (WebClient client = new WebClient()) {
			client.getOptions().setCssEnabled(false);  
			client.getOptions().setJavaScriptEnabled(false); 
			for(int i = 0; i < cves.length; i++) {
				String cve = cves[i];
				int j = i;
				executor.submit(() -> {
					HtmlPage page;
					try {
						page = client.getPage("https://nvd.nist.gov/vuln/detail/" + cve);
						collect(cve, page);
					} catch (Exception e) {
						System.err.println(e);
					} finally {
						countDownLatch.countDown();
					}
				});
			}
		} catch (Exception e) {
			
		}
		try {
			countDownLatch.await();
			for(Risk risk : RISKS) {
				System.out.println(risk.toString());
			}
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void collect(String cve, HtmlPage page) {
		HtmlElement cvss3 =  page.getHtmlElementById("Vuln3CvssPanel");
		if(cvss3 != null) {
			Iterable<HtmlElement> children = cvss3.getHtmlElementDescendants();
			for(HtmlElement item : children) {
				List<HtmlElement> scoreList = (List<HtmlElement>) item.getByXPath(".//span[@class='severityDetail']"); // score span
				if(!(scoreList == null || scoreList.isEmpty())) {
					String score = scoreList.get(0).asText();
					if (!score.contains("N/A")) {
						String trimScore = score.replace("HIGH", EMPTY).replace("MEDIUM", EMPTY).replace("CRITICAL", EMPTY).replace("LOW", EMPTY).replace("NEGLIGIBLE", EMPTY).trim();
						RISKS.add(new Risk(cve, trimScore));
						break;
					}
					
				}
			}
		}
	}
	
	private static final String[] getSRA() {
		String s6 = "CVE-2021-27803,CVE-2021-0326,CVE-2020-12695,CVE-2019-9499,CVE-2019-9498,CVE-2019-9497,CVE-2019-9496,CVE-2019-10064,CVE-2017-13082";
		return s6.split(",");
	}
}
