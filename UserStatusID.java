
public interface UserStatusID {
	public static final int FORM_RECIEVED = 1; // The form was received by the server
	public static final int FORM_NOTRECEIVED = 2; // error processing the form
	public static final int EMAIL_SENT = 3; // Email was sent to User and admin
	public static final int EMAIL_NOTSENT = 4; // Email error on server-side
	public static final int FATAL_ERROR = 5; 
	
	public abstract String getStatus(); // Method to print out the statuses
}
