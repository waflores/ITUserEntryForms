import java.io.Serializable;

/*
 * Status object, gets a new status from an object and attaches an explaination for exception handling
 */

@SuppressWarnings("serial")
public class FormStatus implements Serializable, UserStatusID {
	private Integer statusid;
	private String filename;
	
	public FormStatus(Integer status) {
		setStatusID(status);
//		setFilename(filename);
	}
	
	@Override
	public String getStatus() {
		int id = getStatusID();
		
		switch(id){
		case UserStatusID.EMAIL_SENT:
			break;
		case UserStatusID.EMAIL_NOTSENT:
			break;
		case UserStatusID.FORM_RECIEVED:
			break;
		case UserStatusID.FORM_NOTRECEIVED:
			break;
		case UserStatusID.FATAL_ERROR:
			break;
		default:
			break;
		}
		return "Not Implemented yet";
	}
	
	public void setStatusID(Integer id) {
		this.statusid = id;
	}
	
	public int getStatusID() {
		return this.statusid;
	}
	
	public void setFilename(String file) {
		this.filename = file;
	}
	
	public String getFilename() {
		return this.filename;
	}

}
