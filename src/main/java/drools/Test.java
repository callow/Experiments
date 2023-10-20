package drools;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import drools.entity.CustomerType;
import drools.entity.OrderDiscount;
import drools.entity.OrderRequest;

public class Test {

	public static void main(String[] args) {
		
	     System.out.println("Start the request 1++++++");
	     fire(new OrderRequest("123", 8, 50000, CustomerType.LOYAL));
	     System.out.println("End the request 1++++++");

         System.out.println("Start the request 2++++++");
         fire(new OrderRequest("124", 65, 500, CustomerType.NEW));
         System.out.println("End the request 2++++++");
		
	}
	
	private static void fire(OrderRequest orderRequest) {
		KieContainer kieContainer = DroolsConfig.kieContainer();
		
	    OrderDiscount orderDiscount = new OrderDiscount();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("orderDiscount", orderDiscount);
        kieSession.insert(orderRequest);
        kieSession.fireAllRules();
        kieSession.dispose();
	}
}
