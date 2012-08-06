import java.util.Date;


public class ActiveConnectionObj {

	private String userName;
	private String compName;
	private Date loginTime;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	
	public ActiveConnectionObj (String userName, String compName, Date loginTime) {
		setUserName(userName);
		setCompName(compName);
		setLoginTime(loginTime);
	}
	
	@Override
	public String toString() {
		return getUserName() + " at" + getCompName() + "on " + getLoginTime().toString();
		
	}
}
