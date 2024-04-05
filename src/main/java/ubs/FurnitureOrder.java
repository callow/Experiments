package ubs;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * TO DO
 */
public class FurnitureOrder implements FurnitureOrderInterface{
	/**
	 * TODO Create a map of furniture items to order quantities
	 */
	public HashMap<Furniture, Integer> furnitureQuantityMap;
	
	/**
	 * Initialize a new  mapping of furniture types to order quantities
	 */
	public FurnitureOrder(){
		// TODO 
		furnitureQuantityMap = new HashMap<>();
	}
	
	@Override
	public void addToOrder(final Furniture type, final int furnitureCount) {
		// TODO 
		furnitureQuantityMap.put(type, furnitureQuantityMap.getOrDefault(type, 0) + 1);
	}
	
	@Override
	public HashMap<Furniture, Integer> getOrderedFurniture() {
		// TODO 
		return furnitureQuantityMap;
	}
	
	@Override
	public float getTotalOrderCost() {
		// TODO 
		float total = 0f;
		if (furnitureQuantityMap.isEmpty()) {
			return 0f;
		}
		for (Map.Entry<Furniture, Integer> entry : furnitureQuantityMap.entrySet()) {
			Furniture type = entry.getKey();
			Integer quantity = entry.getValue();
	        total += (float) quantity * type.getCost();
	    }
		return total;
	}
	
	@Override
	public int getTypeCount(Furniture type) {
		// TODO 
		return furnitureQuantityMap.getOrDefault(type, 0);
	}
	
	@Override
	public float getTypeCost(Furniture type) {
		// TODO 
		return (float)furnitureQuantityMap.getOrDefault(type, 0) * type.getCost();
	}
	
	@Override
	public int getTotalOrderQuantity() {
		// TODO 
		int total = 0;
		if (furnitureQuantityMap.isEmpty()) {
			return 0;
		}
		
		for (Map.Entry<Furniture, Integer> entry : furnitureQuantityMap.entrySet()) {
			Integer quantity = entry.getValue();
	        total += quantity;
	    }
		return total;
	}
}
