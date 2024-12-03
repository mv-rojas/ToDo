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
	private ScrollablePane pan;
	private JFrame frame;

	//Constructor method initializes main components of frame and creates a new file for saving the toDos
	MainToDoApp() {
		
		toDoList = new ArrayList<ToDo>();
		textField = new JTextField(20);
		pan = new ScrollablePane();

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
		
		JScrollPane scroll = new JScrollPane(pan, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setRowHeaderView(Box.createRigidArea(new Dimension(35,10)));
		
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		
		setTextField();
		
		pan.add(textField);
		pan.add(new JLabel("To-Dos"));
		pan.setAlignmentX(Component.LEFT_ALIGNMENT);	
		

		JLabel title = new JLabel("To-Dos");
	
		// add any to-dos that were saved previously
		for (ToDo i : toDoList) {
			ToDoComponent todocomp = new ToDoComponent(i, pan);
			pan.add(todocomp,2);
			todocomp.setAlignmentX(Component.LEFT_ALIGNMENT);
			todocomp.setMaximumSize(todocomp.getPreferredSize());
			i.addObserver(this);				
		}

		pan.add(Box.createVerticalGlue());
		//pan.setPreferredSize(new Dimension(1000,1000));

		frame.add(scroll);
		frame.pack();
		frame.setVisible(true);

	}
	
	private void setTextField() {
		
		textField.setMaximumSize(textField.getPreferredSize());
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//When someone presses enter in the textfield, create new to-do 
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ToDo newToDo = new ToDo(textField.getText());
				toDoList.add(newToDo);
				newToDo.addObserver(MainToDoApp.this);
				
				ToDoComponent tdc = new ToDoComponent(toDoList.get(toDoList.size()-1),pan);
				tdc.setAlignmentX(Component.LEFT_ALIGNMENT);
				tdc.setMaximumSize(tdc.getPreferredSize());

				pan.add(tdc,2);
				textField.setText("");

				//ensures new checkbox component is visible
				pan.revalidate();
				pan.repaint();
		
				//Saves list of to-dos for later use	
				update(new ToDo(textField.getText()), null);
			}
		});
	}

	//updates save file when items are added or deleted
	@Override
	public void update(Observable todo, Object arg) {
	
		if(toDoList.contains(todo) && arg == "deleted") {
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
				MainToDoApp testToDo = new MainToDoApp();
				testToDo.createGUI();
			}					
			
		});
	}

	//This scrollablePane class allows the to-do components placed on this pane to stretch/shrink horizontally but remain scrollable vertically, depending on the window size 
	private class ScrollablePane extends JPanel implements Scrollable {
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}
		public int getScrollableUnitIncrement(Rectangle rect, int orientation, int direction) {
			return rect.height - 1;
		}
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}
		public int getScrollableBlockIncrement(Rectangle rect, int orientation, int direction) {
			return 1;
		}

	}
	
}

