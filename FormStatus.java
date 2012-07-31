import java.io.Serializable;

/*
 * Status object, gets a new status from an object and attaches an explaination for exception handling
 */

@SuppressWarnings("serial")
public class FormStatus implements Serializable {
	private Integer statusid;
	private String filename;
	
	public FormStatus(Integer status, String filename) {
		setStatusID(status);
		setFilename(filename);
	}
	
	public String getStatus() {
		int id = getStatusID();
		
		switch(id){
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
