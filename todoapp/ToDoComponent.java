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
		
	}

	private void createSubToDoButton() {
		
		JButton subToDoButton = new JButton("Add Sub-Task");

		GridBagConstraints cSub = new GridBagConstraints();
		cSub.gridx = 1;
		cSub.gridy = 0; 
		cSub.weightx = 0;
		cSub.weighty = 0;
		this.add(subToDoButton, cSub);
		
		subToDoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Must first create a textfield in which to input the new sub-task
				JTextField tempTextField = new JTextField(20);
				GridBagConstraints cTemp = new GridBagConstraints();
				cTemp.gridx = 1;
				cTemp.gridy = GridBagConstraints.RELATIVE;
				ToDoComponent.this.add(tempTextField,cTemp);

				ToDoComponent.this.revalidate();
				ToDoComponent.this.repaint();
				
				tempTextField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)	{
						
						ToDo subTask = new ToDo(tempTextField.getText(), toDo);
						toDo.addSubTask(tempTextField.getText());

						GridBagConstraints cSubToDo = new GridBagConstraints();
						cSubToDo.gridx = 1;
						cSubToDo.gridy = GridBagConstraints.RELATIVE;
						cSubToDo.weightx = 0;
						cSubToDo.weighty = 0;
						cSubToDo.gridwidth = 3;

						ToDoComponent.this.add(new ToDoComponent(subTask, ToDoComponent.this), cSubToDo);
						ToDoComponent.this.remove(tempTextField);

						ToDoComponent.this.revalidate();
						ToDoComponent.this.repaint();
					}

				});
			}

		});
	}


	private void createDeleteButton() {

		JButton deleteButton = new JButton("X");

		GridBagConstraints cDelete = new GridBagConstraints();
		cDelete.gridx = 3;
		cDelete.gridy = 0;
		cDelete.weightx = 0;
		cDelete.weighty = 0;
		this.add(deleteButton, cDelete);


		//removes to-do from general list and to-do GUI component
		deleteButton.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				toDo.remove();


				nestedPanel.remove(ToDoComponent.this);
				nestedPanel.revalidate();
				nestedPanel.repaint();

			}				
		});
	}
}