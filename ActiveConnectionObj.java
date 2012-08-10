import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class ActiveConnectionObj implements Serializable {

	private String userName;
	private String compName;
	private Date loginTime;
	private String connectionStatus; // User or Admin
	public ArrayList<String> logFile;
	public ArrayList<NewUser> inputedUsers;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String username) {
		this.userName = username;
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
	public void setCompName(String string) {
		this.compName = string;
	}
	
	public ActiveConnectionObj() {
		setUserName(System.getProperty("user.name"));
		try {
			setCompName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			setCompName(null);
		}
		setLoginTime(new Date());
		setConnectionStatus(""); // this is a bogus constructor
	}
	
	public ActiveConnectionObj (String userName, String compName, Date loginTime, String connectionStatus) {
		setUserName(userName);
		setCompName(compName);
		setLoginTime(loginTime);
		setConnectionStatus(connectionStatus);
	}
	
	@Override
	public String toString() {
		return getUserName() + " at " + getCompName() + " on " + getLoginTime().toString();
		
	}
	public String getConnectionStatus() {
		return connectionStatus;
	}
	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}
}
