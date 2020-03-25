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

	//constructor takes in the to-do text and the panel that the to-do is part of (so that it can be deleted from said panel)
	ToDoComponent(ToDo t, JPanel n) {

		toDo = t;
		nestedPanel = n;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.RED));


		JCheckBox checkbox = new JCheckBox(toDo.getText());

		GridBagConstraints cCheckbox = new GridBagConstraints();
		//cCheckbox.fill = GridBagConstraints.HORIZONTAL;
		cCheckbox.gridx = 0;
		cCheckbox.gridy = 0;
		cCheckbox.weightx = 0;
		cCheckbox.weighty = 0;
		this.add(checkbox, cCheckbox);
		
		createDeleteButton();
		createSubToDoButton();

		//for each subtask in todo, add to do component to self
		for(ToDo s : toDo.getSubTasks()) {
			addSubTaskComponent(s);
		}
		
	}

	private void createSubToDoButton() {
		
		JButton subToDoButton = new JButton("Add Sub-Task");

		GridBagConstraints cSub = new GridBagConstraints();
		cSub.gridx = 1;
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
		cSubToDo.gridx = 1;
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
		cDelete.gridx = 3;
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