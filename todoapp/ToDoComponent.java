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

	//constructor takes in the to-do text and the panel that the to-do is part of (so that it can be deleted from said panel)
	ToDoComponent(ToDo t, JPanel n) {

		toDo = t;
		nestedPanel = n;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		JCheckBox checkbox = new JCheckBox(toDo.getText());		
		//checkbox.setMinimumSize(new Dimension(100,10));

		textAndButtonPanel = new JPanel();
		textAndButtonPanel.setLayout(new BoxLayout(textAndButtonPanel, BoxLayout.X_AXIS));
		textAndButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textAndButtonPanel.add(checkbox);
		textAndButtonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textAndButtonPanel.setPreferredSize(new Dimension(500-checkbox.getWidth(),30));
		textAndButtonPanel.setMinimumSize(textAndButtonPanel.getPreferredSize());
		
		Component glue = Box.createHorizontalGlue();
		//glue.setPreferredSize(new Dimension(300-checkbox.getWidth(),checkbox.getHeight()));
		
		textAndButtonPanel.add(Box.createHorizontalGlue());
		/*
		Dimension minSize = new Dimension(0,0);
		Dimension prefSize = new Dimension(500-checkbox.getWidth(),checkbox.getHeight());
		Dimension maxSize = new Dimension(Short.MAX_VALUE, checkbox.getHeight());
		textAndButtonPanel.add(new Box.Filler(prefSize, prefSize, maxSize));
		*/
		
		createSubToDoButton();
		createDeleteButton();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(textAndButtonPanel, c);
	
		revalidate();
		repaint();

		//for each subtask in todo, add to do component to self
		for(ToDo s : toDo.getSubTasks()) {
			addSubTaskComponent(s);
		}
	}

	private void createSubToDoButton() {
		
		JButton subToDoButton = new JButton("Add Sub-Task");
		textAndButtonPanel.add(subToDoButton);
		
		subToDoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Must first create a textfield in which to input the new sub-task
				JTextField tempTextField = new JTextField(50);

				tempTextField.setMinimumSize(tempTextField.getPreferredSize());
				tempTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
				ToDoComponent.this.add(tempTextField,1);

				ToDoComponent.this.setMinimumSize(ToDoComponent.this.getPreferredSize());
				ToDoComponent.this.revalidate();
				ToDoComponent.this.repaint();
				
				tempTextField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)	{
						
						ToDo subTask = new ToDo(tempTextField.getText(), toDo);
						toDo.addSubTask(subTask);
						ToDoComponent.this.remove(tempTextField);
						addSubTaskComponent(subTask);

					}

				});
			}

		});
	}

	private void createDeleteButton() {

		JButton deleteButton = new JButton("X");
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
		c.weightx = 1	;

		this.add(subComp,c);
		this.setMinimumSize(this.getPreferredSize());
		
		this.revalidate();
		this.repaint();
	}


	//Since ToDoComponents are nested within other ToDoComponents or panels, there needs to be a way to refresh outer panels even if the to-do
	//component is nested within multiple ToDoComponents
	
	@Override
	public void revalidate() {
		super.revalidate();
		if(nestedPanel !=null ) {
			nestedPanel.revalidate();	
		}		 
	}

	@Override
	public void repaint() {
		super.repaint();
		if(nestedPanel !=null ) {
			nestedPanel.repaint();	
		}
	}
}








































/*

//This class creates a component that displays individual to-dos, allows their modification and addition sub-to-do components
public class ToDoComponent extends JPanel {
	private ToDo toDo;
	private JPanel nestedPanel; 

	//constructor takes in the to-do text and the panel that the to-do is part of (so that it can be deleted from said panel)
	ToDoComponent(ToDo t, JPanel n) {

		toDo = t;
		nestedPanel = n;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.RED));


		JCheckBox checkbox = new JCheckBox(toDo.getText());
		checkbox.setMinimumSize(new Dimension(100,10));

		GridBagConstraints cCheckbox = new GridBagConstraints();
		//cCheckbox.fill = GridBagConstraints.HORIZONTAL;
		cCheckbox.gridx = 0;
		cCheckbox.gridy = 0;
		cCheckbox.weightx = 0;
		cCheckbox.weighty = 0;
		cCheckbox.gridwidth = 100;
		this.add(checkbox, cCheckbox);
		
		createDeleteButton();
		createSubToDoButton();
		revalidate();
		repaint();

		addSubToDos();

		//for each subtask in todo, add to do component to self
		for(ToDo s : toDo.getSubTasks()) {
			addSubTaskComponent(s);
		}
		
	}

	private void addSubToDos() {

		for(ToDo sub : toDo.getSubTasks()) {
			this.add(new ToDoComponent(sub, this));
		}

	}


	private void createSubToDoButton() {
		
		JButton subToDoButton = new JButton("Add Sub-Task");

		GridBagConstraints cSub = new GridBagConstraints();
		cSub.gridx = 101;
		cSub.gridy = 0; 
		cSub.weightx = 0;
		cSub.weighty = 0;
		cSub.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(subToDoButton, cSub);
		
		subToDoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Must first create a textfield in which to input the new sub-task
				JTextField tempTextField = new JTextField(50);
				GridBagConstraints cTemp = new GridBagConstraints();
				
				cTemp.gridx = 1;
				cTemp.gridy = GridBagConstraints.RELATIVE;
				cTemp.weightx = 1;
				cTemp.weighty = 0;
				cTemp.gridwidth = 3;
				cTemp.anchor = GridBagConstraints.FIRST_LINE_START;
											
				tempTextField.setMinimumSize(tempTextField.getPreferredSize());
				ToDoComponent.this.add(tempTextField,cTemp);

				ToDoComponent.this.revalidate();
				ToDoComponent.this.repaint();
				
				tempTextField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)	{
						
						ToDo subTask = new ToDo(tempTextField.getText(), toDo);
						toDo.addSubTask(subTask);
						ToDoComponent.this.remove(tempTextField);
						addSubTaskComponent(subTask);

					}

				});
			}

		});
	}

	private void addSubTaskComponent(ToDo sub) {

		GridBagConstraints cSubToDo = new GridBagConstraints();
		cSubToDo.gridx = 101;
		cSubToDo.gridy = GridBagConstraints.RELATIVE;
		cSubToDo.weightx = 0;
		cSubToDo.weighty = 0;
		cSubToDo.gridwidth = 3;
		cSubToDo.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(new ToDoComponent(sub, this), cSubToDo);
		this.setMaximumSize(this.getPreferredSize());

		this.revalidate();
		this.repaint();
	}


	private void createDeleteButton() {

		JButton deleteButton = new JButton("X");

		GridBagConstraints cDelete = new GridBagConstraints();
		cDelete.gridx = 103;
		cDelete.gridy = 0;
		cDelete.weightx = 1;
		cDelete.weighty = 0;
		cDelete.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(deleteButton, cDelete);


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
	
	//Since ToDoComponents are nested within other ToDoComponents or panels, there needs to be a way to refresh outer panels even if the to-do
	//component is nested within multiple ToDoComponents
	@Override
	public void revalidate() {
		super.revalidate();
		if(nestedPanel !=null ) {
			nestedPanel.revalidate();	
		}
		 
	}
	
	@Override
	public void repaint() {
		super.repaint();
		if(nestedPanel !=null ) {
			nestedPanel.repaint();	
		}
	}
}

*/