/* Experiment with a basic to-do app */
import java.awt.*; 
import java.awt.event.*; 
import java.util.*;
import javax.swing.*;


public class ToDo {

	//List of ToDos
	protected ArrayList<String> toDoList;
	static JTextField textField; 

	//Constructor method creates GUI
	ToDo() {
		toDoList = new ArrayList<String>();
		textField = new JTextField(20);
	}
	
	private void createGUI() {

		//Create app window
		JFrame frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		WriteToDoActionListener toDoText = new WriteToDoActionListener();

		textField.addActionListener(toDoText);

		JPanel pan = new JPanel(); 		
		pan.add(textField); 
		frame.add(pan);

		
		frame.pack(); 
		frame.setVisible(true);
	}

	private class WriteToDoActionListener implements ActionListener {

		WriteToDoActionListener() {

		}

		public void actionPerformed(ActionEvent e) {
			toDoList.add(textField.getText());
			System.out.println(toDoList.get(toDoList.size()-1));
		
		}
	}


	public static void main(String[] args) {

		//Creates Event Dispatcher Thread so GUI can run without problems
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ToDo testToDo = new ToDo();
				testToDo.createGUI();
			}
		});

		
	}

}

