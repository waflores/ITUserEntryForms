import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class Picker implements ActionListener {
	/* GUI Stuff */
	private JButton b = new JButton("popup");
	private JFrame f = new JFrame();
	private JTextField text = new JTextField(20);
	
	 public static void main(String[] args) {
         new Picker();
	 }
	 
	 /* Picker Constructor */
	 public Picker(){
		 JLabel label = new JLabel("Selected Date:");
         JPanel p = new JPanel();
         p.add(label);
         p.add(text);
         p.add(b);
         f.getContentPane().add(p);
         f.pack();
         f.setVisible(true);
         b.addActionListener(this);
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 }
	 
	  public void actionPerformed(ActionEvent ae) {
		  if (ae.getSource() == b) text.setText(new DatePicker(f).setPickedDate());
  }
}
