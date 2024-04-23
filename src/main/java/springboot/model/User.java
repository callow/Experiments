package springboot.model;

/**
 * all username are unique
 */
public class User {
	
	private String name;
	private String password;
	private String userId;
	private String nickname;
	
	public User(String name, String password, String userId, String nickname) {
		this.name = name;
		this.password = password;
		this.userId = userId;
		this.nickname = nickname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
