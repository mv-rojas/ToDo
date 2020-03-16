/* Experiment with a basic to-do app */
import java.awt.*; 
import java.awt.event.*; 
import java.util.*;
import javax.swing.*;
import java.io.*;

public class ToDo {

	//List of ToDos
	protected ArrayList<JCheckBox> toDoList;
	static JTextField textField; 
	static JPanel topPan;
	static JPanel pan;
	static JFrame frame;

	//Constructor method 
	ToDo() {
		
		toDoList = new ArrayList<JCheckBox>();
		textField = new JTextField(20);
		topPan = new JPanel();
		pan = new JPanel();

		//creates new folder to store to-do list 
		new File("Saved To-Dos").mkdirs(); 
		
		//Loads any saved to-dos lists, otherwise creates new list
		if (new File("Saved To-Dos/todos.ser").isFile()) {
			try {

				FileInputStream listIn = new FileInputStream("Saved To-Dos/todos.ser");
				ObjectInputStream in = new ObjectInputStream(listIn);
				
				toDoList = (ArrayList<JCheckBox>) in.readObject();
				in.close();
				listIn.close();

			} catch (IOException i) {
				i.printStackTrace();
				System.out.println("Yikes");
			} catch (ClassNotFoundException c) {

			}
		}
	}

	private void createGUI() {

		frame = new JFrame("To-Do App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField.setMaximumSize(new Dimension(300,50));
		WriteToDoActionListener toDoText = new WriteToDoActionListener();
		textField.addActionListener(toDoText);

		JLabel title = new JLabel("To-Dos");


		topPan.setLayout(new BoxLayout(topPan, BoxLayout.Y_AXIS));
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

		pan.add(textField);
		pan.add(new JLabel("To-Dos"));
		topPan.add(pan);
		topPan.add(Box.createVerticalGlue());

		for (JCheckBox i : toDoList) {
			pan.add(i);					
		}

		frame.add(topPan);
		frame.pack();
		frame.setVisible(true);

	}

	private class WriteToDoActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			toDoList.add(new JCheckBox(textField.getText()));
			pan.add(new JCheckBox(textField.getText()));
			textField.setText("");

			//ensures new checkbox component is visible
			frame.pack();
			frame.setVisible(true);

			
			//Saves list of to-dos for later use	
			try {

				FileOutputStream listOut = new FileOutputStream(new File("Saved To-Dos/todos.ser"));
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

