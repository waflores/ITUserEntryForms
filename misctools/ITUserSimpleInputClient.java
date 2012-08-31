package misctools;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class ITUserSimpleInputClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

				String stopStr = "EXIT"; // our stopping string
				
				String serverAddress = "localhost";		
				if (args.length == 1) serverAddress = args[0];
				if (args.length > 1) {
					System.out.println("Restart. Provide the server computer address "
						+ "in dotted-numeric form as a single cmd line parameter.");
					System.out.println("Or provide no parameter to designate 'localhost'.");
					return;
				}
				
				// Test connection to server
				try {
					Socket socket = new Socket(serverAddress, 1234);
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF("test");
//					DataInputStream dis = new DataInputStream(socket.getInputStream());
//					String testResponse = dis.readUTF();
				}
				catch(IOException ioe) {
					System.out.println("ERROR: Attempt to connect to the TherapistServer"
										+ "at" + serverAddress + " has failed.");
					System.out.println("This network address may be incorrect for the server,");
					System.out.println("or the server may not be up. The specific failure is:");
					System.out.println(ioe.toString());
					return;
				}
						
				// Q & A session
				try{
					Socket socket = new Socket(serverAddress, 1234);
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					while(true) {
//						DataInputStream dis = new DataInputStream(socket.getInputStream());
						String question = JOptionPane.showInputDialog(null, "Hi, What can I do for you"
		                                        + " today?\r\n Enter "+ stopStr +" to end our session...");
		                                
		                                if(question == null || question.equalsIgnoreCase(stopStr)) {
		                                    JOptionPane.showMessageDialog(null, "Bye now, come back soon!", "Therapist says...", 1);
		                                    break; // exit on stopString
						}
		                                else if(question.length() == 0) continue; // skip whitespace questions
						else {
							// Print out an answer
							dos.writeUTF(question); // send message to TherapistServer
//							String answer = dis.readUTF();
//							JOptionPane.showMessageDialog(null, answer, "Therapist says...", 1);
						}			
					} // End While loop
				}
				catch(IOException ioe) { // Connection with server lost
		                    JOptionPane.showMessageDialog(null, "Communication with server lost. Please restart program to" 
						+ " reconnect.", "Server says...", 0);
				}
				
	}

}
