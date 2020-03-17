/* Experiment with a basic to-do app */
import java.awt.*; 
import java.awt.event.*; 
import java.util.*;
import javax.swing.*;
import java.io.*;

public class ToDoApp {

	//List of ToDos
	protected ArrayList<String> toDoList;
	static JTextField textField; 
	static JPanel pan;
	static JFrame frame;

	//Constructor method 
	ToDoApp() {
		
		toDoList = new ArrayList<String>();
		textField = new JTextField(20);
		pan = new JPanel();

		//creates new folder to store to-do list 
		new File("Saved To-Dos").mkdirs(); 
		
		//Loads any saved to-dos lists, otherwise creates new list
		if (new File("Saved To-Dos/todos.ser").isFile()) {
			try {

				FileInputStream listIn = new FileInputStream("Saved To-Dos/todos.ser");
				ObjectInputStream in = new ObjectInputStream(listIn);
				
				toDoList = (ArrayList<String>) in.readObject();
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

		//topPan allows a second panel with the to-do components to be added to while allowing a strechable buffer space beneath the components
		JPanel topPan = new JPanel();
		topPan.setLayout(new BoxLayout(topPan, BoxLayout.Y_AXIS));
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

		pan.add(textField);
		pan.add(new JLabel("To-Dos"));
		topPan.add(pan);
		//prevents elements from expanding by letting this element expand with the resizing of windows
		topPan.add(Box.createVerticalGlue());

		// add any to-dos that were saved previously
		for (String i : toDoList) {
			pan.add(new ToDo(i));					
		}

		frame.add(topPan);
		frame.pack();
		frame.setVisible(true);

	}

	//updates save file when items are added or deleted
	private void updateSaveFile() {
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

	//This component displays individual to-dos and allows their modification
	private class ToDo extends JPanel implements ActionListener {
		private String toDoText;

		ToDo(String text) {

			toDoText = text;

			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			JButton deleteButton = new JButton("X");
			deleteButton.addActionListener(this);

			this.add(new JCheckBox(text));
			this.add(deleteButton);
		}

		//removes to-do from general list and to-do GUI component
		public void actionPerformed(ActionEvent e) {
			
			toDoList.remove(toDoText);
			updateSaveFile();

			for(String i : toDoList) {
				System.out.println(i);
			}
			pan.remove(this);

			pan.revalidate();
			pan.repaint();
		}

		public String getText() {
			return toDoText;
		}

	}

	//creates checkable to-do from text that is entered in textField
	private class WriteToDoActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			toDoList.add(textField.getText());
			pan.add(new ToDo(textField.getText()));
			textField.setText("");

			//ensures new checkbox component is visible
			pan.revalidate();
			pan.repaint();
	
			//Saves list of to-dos for later use	
			updateSaveFile();
		}
			
	}


	public static void main(String[] args) {

		//Creates Event Dispatcher Thread so GUI can run without problems
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ToDoApp testToDo = new ToDoApp();
				testToDo.createGUI();
			}
		});

		
	}

}

