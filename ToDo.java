/* Experiment with a basic to-do app */
import java.awt.*; 
import java.awt.event.*; 
import java.util.*;
import javax.swing.*;
import java.io.*;

public class ToDo {

	//List of ToDos
	protected ArrayList<String> toDoList;
	static JTextField textField; 
	static JTextArea toDoDisplay; 

	//Constructor method 
	ToDo() {
		textField = new JTextField(20);
		toDoDisplay = new JTextArea("To-Do List\n", 1,20); 

		textField.setMaximumSize(new Dimension(300,1));

		//creates new folder to store to-do list 
		new File("Saved To-Dos").mkdirs(); 
		
		//Loads any saved to-dos lists, otherwise creates new list
		if (new File("Saved To-Dos/todos.ser").isFile()) {
			try {

				FileInputStream listIn = new FileInputStream("/Saved To-Dos/todos.ser");
				ObjectInputStream in = new ObjectInputStream(listIn);
				
				toDoList = (ArrayList<String>) in.readObject();
				in.close();
				listIn.close();

				for (String i : toDoList) {
					System.out.println(i);
					toDoDisplay.append(i + "\n");					
				}

			} catch (IOException i) {
				i.printStackTrace();
				System.out.println("Yikes");
			} catch (ClassNotFoundException c) {

			}
		} else { 
			toDoList = new ArrayList<String>();
		}
	}

	private void createGUI() {

		//Create app window
		JFrame frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Connects actionlistener to textfield so textfield executes code when text is entered
		WriteToDoActionListener toDoText = new WriteToDoActionListener();
		textField.addActionListener(toDoText);

		//create new panel to add textfield, textarea, and other components. Sets layout to follow 
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS)); 

		pan.add(textField); 
		pan.add(toDoDisplay);
		frame.add(pan);
		
		frame.pack(); 
		frame.setVisible(true);
	}

	private class WriteToDoActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			toDoList.add(textField.getText());
			toDoDisplay.append(textField.getText() + "\n");
			textField.setText("");
			
			//Saves list of to-dos for later use	
			try {

				FileOutputStream listOut = new FileOutputStream(new File("/Saved To-Dos/todos.ser"));
				ObjectOutputStream out = new ObjectOutputStream(listOut);
				out.writeObject(toDoList);
				out.close();
				listOut.close();
				
			} catch (IOException i) {
				i.printStackTrace();
			}
			
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

