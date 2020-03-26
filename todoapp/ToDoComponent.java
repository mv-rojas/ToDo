package todoapp;

import java.awt.*; 
import java.awt.event.*; 
import java.util.*;
import javax.swing.*;
import java.io.*;


//This class creates a component that displays individual to-dos, allows their modification and addition sub-to-do components
public class ToDoComponent extends JPanel {
	private ToDo toDo;
	private JPanel nestedPanel; 
	private JPanel textAndButtonPanel;
	private JPanel hiddenPanel;

	//constructor takes in the to-do text and the panel that the to-do is part of (so that it can be deleted from said panel)
	ToDoComponent(ToDo t, JPanel n) {

		toDo = t;
		nestedPanel = n;
		this.setLayout(new GridBagLayout());
		
		createMainPanel();
		createSubToDoButton();
		createDeleteButton();
		createHiddenTextField();
	
		revalidate();
		repaint();

		//for each subtask in todo, add to do component to self
		for(ToDo s : toDo.getSubTasks()) {
			addSubTaskComponent(s);
		}
	}
	
	private void createMainPanel() {
		
		JCheckBox checkbox = new JCheckBox(toDo.getText());		

		textAndButtonPanel = new JPanel();
		textAndButtonPanel.setLayout(new BoxLayout(textAndButtonPanel, BoxLayout.X_AXIS));
		textAndButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textAndButtonPanel.setPreferredSize(new Dimension(1000-checkbox.getWidth(),30));
		textAndButtonPanel.setMinimumSize(textAndButtonPanel.getPreferredSize());
		textAndButtonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		
		textAndButtonPanel.add(checkbox);
		textAndButtonPanel.add(Box.createHorizontalGlue());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 5;
		c.ipady = 5;
		c.insets = new Insets(5,5,0,0);
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(textAndButtonPanel, c);
		
	}
	
	private void createHiddenTextField() {
		
		hiddenPanel = new JPanel();
		hiddenPanel.setLayout(new BoxLayout(hiddenPanel,BoxLayout.X_AXIS));
		
		JTextField textField = new JTextField(25-toDo.getText().length());
	
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)	{
				
				ToDo subTask = new ToDo(textField.getText(), toDo);
				toDo.addSubTask(subTask);
				
				hideTextField();
				textField.setText("");
				
				addSubTaskComponent(subTask);

			}});
		
		hiddenPanel.add(textField);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.insets = new Insets(0,25,0,0);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		hideTextField();

		this.add(hiddenPanel,c);
	}
	
	private void hideTextField() {
		hiddenPanel.setMaximumSize(new Dimension(0,0));
		hiddenPanel.setPreferredSize(new Dimension(0,0));
		hiddenPanel.setMinimumSize(new Dimension(0,0));
	}

	private void createSubToDoButton() {
		
		JButton subToDoButton = new JButton("+");
		textAndButtonPanel.add(subToDoButton);
		
		subToDoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				//show the hidden panel by increasing its size
				hiddenPanel.setPreferredSize(new Dimension(textAndButtonPanel.getWidth(), 25));
				hiddenPanel.setMinimumSize(hiddenPanel.getPreferredSize());
				
				revalidate();
				repaint();
			}
	

		});
	}

	private void createDeleteButton() {

		JButton deleteButton = new JButton("x");
		textAndButtonPanel.add(deleteButton);

		//removes to-do from general list and to-do GUI component
		deleteButton.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				toDo.remove();

				nestedPanel.remove(ToDoComponent.this);
				nestedPanel.setMaximumSize(nestedPanel.getPreferredSize());

				revalidate();
				repaint();

			}				
		});
	}
	
	private void addSubTaskComponent(ToDo sub) {
	
		ToDoComponent subComp = new ToDoComponent(sub,this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0,25,0,0);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1;

		this.add(subComp,c);
		this.setMinimumSize(this.getPreferredSize());
		
		revalidate();
		repaint();
	}


	//Since ToDoComponents are nested within other ToDoComponents or panels, there needs to be a way to refresh outer panels even if the to-do
	//component is nested within multiple ToDoComponents
	
	@Override
	public void revalidate() {
		super.revalidate();
		if(nestedPanel !=null ) {
			nestedPanel.revalidate();	
		}		 
		this.setMinimumSize(this.getPreferredSize());
	}

	@Override
	public void repaint() {
		super.repaint();
		if(nestedPanel !=null ) {
			nestedPanel.repaint();	
		}
	}
}
