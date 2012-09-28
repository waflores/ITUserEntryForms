package helpertools;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/*
 * This will the be Object that is transmitted for a newUser Request.
 */
@SuppressWarnings("serial")
public class NewUser implements Serializable {
	private String f_name;
	private String l_name;
	private String pref_name;
	private String m_initials;
	private String title;
	private String manager;
	private String dept;
	private String diosynth_empID;
	private String emp_location;
	private String emp_startDate;
	private boolean emp_rehire;
	private boolean emp_status; // is he a regular or temp?
	private String atSign = "@"; // newLine only serves as a separator... not an actual newLine char
	private String fileName;
	
	private boolean sentToServer = false;
	
	/* The constructor for our object */
//	@SuppressWarnings("deprecation")
	public NewUser(String f_name, String l_name, String pref_name, String m_initials, String title, 
			String manager, String dept, String diosynth_empID, String emp_location, String emp_startDate, 
			boolean emp_rehire, boolean emp_status) throws ParseException {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		String date = sdf.format(new Date());
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
		
		this.setF_name(f_name);
		this.setL_name(l_name); 
		this.setPref_name(pref_name); 
		this.setM_initials(m_initials); 
		this.setTitle(title); 
		this.setManager(manager); 
		this.setDept(dept); 
		this.setDiosynth_empID(diosynth_empID); 
		this.setEmp_location(emp_location); 
		this.setEmp_startDate(emp_startDate); 
		this.setEmp_rehire(emp_rehire); 
		this.setEmp_status(emp_status);
		
		if (emp_startDate.length() != 0) {
			date = sdf.format(df.parse(emp_startDate));
		}
		else {
			date = sdf.format(new Date());
		}
		
		this.setFileName(f_name + l_name + date  + ".pdf");
	}

	public String toString() {
		/* De-serialize the object */
		String firstName = this.getF_name();
		String lastName = this.getL_name(); 
		String prefName = this.getPref_name(); 
		String midName = this.getM_initials(); 
		String title = this.getTitle(); 
		String manager = this.getManager(); 
		String dept = this.getDept(); 
		String employeeID = this.getDiosynth_empID(); 
		String location = this.getEmp_location(); 
		String startDate = this.getEmp_startDate(); 
		String empRehire = this.isEmp_rehire(); 
		String empStatus = this.isEmp_status();
		String retVal = null; 
		
		if (firstName.length() != 0) retVal = firstName;
			retVal = retVal + atSign;
		
		if (midName.length() != 0) retVal += midName;
			retVal = retVal + atSign;
			
		if (lastName.length() != 0) retVal += lastName;
			retVal = retVal + atSign; 
			
		if (prefName.length() != 0) retVal += prefName;
			retVal = retVal + atSign;
			
		if (title.length() != 0) retVal += title;
			retVal = retVal + atSign;
		
		if (manager.length() != 0) retVal += manager;
			retVal = retVal + atSign; 
		
		if (dept.length() != 0) retVal += dept;
			retVal = retVal + atSign;
		
		if (employeeID.length() != 0) retVal += employeeID;
			retVal = retVal + atSign; 
		
		if (location.length() != 0) retVal += location;
			retVal = retVal + atSign;
		
		if (startDate.length() != 0) retVal += startDate;
			retVal = retVal + atSign;
		
		if (empRehire.length() != 0) retVal += empRehire;
			retVal = retVal + atSign;
		
		if (empStatus.length() != 0) retVal += empStatus;
		
		return retVal;
	}

	public String getF_name() {
		return f_name;
	}

	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	public String getPref_name() {
		return pref_name;
	}

	public void setPref_name(String pref_name) {
		this.pref_name = pref_name;
	}

	public String getM_initials() {
		return m_initials;
	}

	public void setM_initials(String m_initials) {
		this.m_initials = m_initials;
	}

	public String getL_name() {
		return l_name;
	}

	public void setL_name(String l_name) {
		this.l_name = l_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getDiosynth_empID() {
		return diosynth_empID;
	}

	public void setDiosynth_empID(String diosynth_empID) {
		this.diosynth_empID = diosynth_empID;
	}

	public String getEmp_location() {
		return emp_location;
	}

	public void setEmp_location(String emp_location) {
		this.emp_location = emp_location;
	}

	public String getEmp_startDate() {
		return emp_startDate;
	}

	public void setEmp_startDate(String emp_startDate) {
		this.emp_startDate = emp_startDate;
	}

	public String isEmp_rehire() {
		if (emp_rehire) {
			return "Yes";
		}
		else return "No";
	}

	public void setEmp_rehire(boolean emp_rehire) {
		this.emp_rehire = emp_rehire;
	}

	public String isEmp_status() {
		if (emp_status) {
			return "REGULAR";
		}
		else return "TEMP/ CONTRACT";
	}

	public void setEmp_status(boolean emp_status) {
		this.emp_status = emp_status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isSentToServer() {
		return sentToServer;
	}

	public void setSentToServer(boolean sentToServer) {
		this.sentToServer = sentToServer;
	}
	
}
