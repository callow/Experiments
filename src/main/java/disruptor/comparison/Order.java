package disruptor.comparison;

public class Order {
	
	private long orderId;
	private String name;
	private double price;
	
	public Order() {}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

}
