/* This class creates the base frame of the to-do app and creates the options to add a to-do*/

package todoapp;

import java.awt.*; 
import java.awt.event.*; 
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;
import java.io.*;

public class MainToDoApp implements Observer {

	//List of ToDos
	private ArrayList<ToDo> toDoList;
	private JTextField textField; 
	private JPanel pan;
	private JFrame frame;

	//Constructor method initializes main components of frame and creates a new file for saving the toDos
	MainToDoApp() {
		
		toDoList = new ArrayList<ToDo>();
		textField = new JTextField(20);
		pan = new JPanel();

		//creates new folder to store to-do list 
		new File("Saved To-Dos").mkdirs(); 

		loadSaveFile();
		
	}

	private void loadSaveFile() {

		//Loads any saved to-dos lists, otherwise creates new list
		if (new File("Saved To-Dos/todos.ser").isFile()) {
			try {

				FileInputStream listIn = new FileInputStream("Saved To-Dos/todos.ser");
				ObjectInputStream in = new ObjectInputStream(listIn);
				
				toDoList = (ArrayList<ToDo>) in.readObject();

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
		
		//When someone presses enter in the textfield, create new to-do 
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toDoList.add(new ToDo(textField.getText()));
				pan.add(new ToDoComponent(toDoList.get(toDoList.size()-1),pan));
				textField.setText("");

				//ensures new checkbox component is visible
				pan.revalidate();
				pan.repaint();
		
				//Saves list of to-dos for later use	
				update(new ToDo(textField.getText()), "nothing");
			}
		});

		JLabel title = new JLabel("To-Dos");

		//topPan allows a second panel with the to-do components to be added to while allowing a strechable buffer space beneath the components
		JPanel topPan = new JPanel();
		topPan.setLayout(new BoxLayout(topPan, BoxLayout.Y_AXIS));
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

		pan.add(textField);
		pan.add(new JLabel("To-Dos"));
		topPan.add(pan);
		
		// add any to-dos that were saved previously
		for (ToDo i : toDoList) {
			ToDoComponent todocomp = new ToDoComponent(i, pan);
			pan.add(todocomp, Component.LEFT_ALIGNMENT);	
			i.addObserver(this);				
		}
		pan.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		pan.setMaximumSize(pan.getPreferredSize());

		topPan.setBorder(BorderFactory.createLineBorder(Color.GREEN));


		frame.add(topPan);
		frame.pack();
		frame.setVisible(true);

	}

	//updates save file when items are added or deleted
	@Override
	public void update(Observable todo, Object arg) {

		if(toDoList.contains(todo)) {
			toDoList.remove(todo);
		}

		try {

			FileOutputStream listOut = new FileOutputStream(new File("Saved To-Dos/todos.ser"));
			ObjectOutputStream out = new ObjectOutputStream(listOut);
			out.writeObject(toDoList);

			out.close();
			listOut.close();
				
		} catch (IOException i) {
			i.printStackTrace();
		}


		pan.setMaximumSize(pan.getPreferredSize());

	}

	public static void main(String[] args) {

		//Creates Event Dispatcher Thread so GUI can run without problems
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
/*
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

				JPanel nestP1 = new JPanel();
				nestP1.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.FIRST_LINE_START;
				c.weightx = 0;
				c.weighty = 0;

				nestP1.add(new JButton("dklfj"), c);
				nestP1.setMaximumSize(nestP1.getPreferredSize());

				mainPanel.add(new JButton("DFD"));
				mainPanel.add(nestP1);
				mainPanel.add(new JButton("D"));
				mainPanel.add(Box.createVerticalGlue());

				frame.add(mainPanel);
				frame.pack();
				frame.setVisible(true);


*/




				MainToDoApp testToDo = new MainToDoApp();
				testToDo.createGUI();
			}					
			
		});
	}

}

