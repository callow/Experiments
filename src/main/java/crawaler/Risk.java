package crawaler;

public class Risk {
  private String name;
  private String score;
  
  
  
	public Risk(String name, String score) {
	this.name = name;
	this.score = score;
}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return name +"," + score;
	}
	
	
}
