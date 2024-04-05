package ubs;

public enum Furniture {
	CHAIR("Chair", 100.0f),
	TABLE("Table", 250.0f),
	COUCH("Couch", 500.0f);

	private final String label;
	private final float cost;
	
	private Furniture(String label, float cost) {
		this.label = label;
		this.cost = cost;
	}

	public String getLabel() {
		return label;
	}

	public float getCost() {
		return cost;
	}
	
	
	
	
}
