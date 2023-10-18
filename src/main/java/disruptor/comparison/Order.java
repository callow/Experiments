package disruptor.comparison;

public class Order {
	
	private long orderId;
	
	public Order() {}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public Order(long orderId) {
		this.orderId = orderId;
	}
}
