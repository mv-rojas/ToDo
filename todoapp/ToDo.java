package todoapp;

import java.util.Observable;
import java.util.ArrayList;
import java.io.Serializable;

public class ToDo extends Observable implements Serializable {

		private String toDoText;
		private ArrayList<ToDo> subtasks;
		private ToDo parentToDo;

		ToDo(String text) {
			toDoText = text;
			parentToDo = null;
			subtasks = new ArrayList<ToDo>();
		}

		//Constructor for creating subtask to-dos part of the parent to-do
		ToDo(String text, ToDo parent) {
			parentToDo = parent;
			toDoText = text;
			subtasks = new ArrayList<ToDo>();

		}

		public void addSubTask(String text) {
			subtasks.add(new ToDo(text, this));
			setChanged();
			notifyObservers();
		}

		public void removeSubTask(ToDo toDo) {
			if(subtasks.contains(toDo)) {
				subtasks.remove(toDo);
			} 
			setChanged();
			notifyObservers();
		}

		public void remove() {
			if (parentToDo != null) {
				parentToDo.removeSubTask(this);
			}
			setChanged();
			notifyObservers("deleted");
		}

		public ArrayList<ToDo> getSubTasks() {
			return subtasks;
		}

		public String getText() {
			return toDoText;
		}

		@Override
		public void setChanged() {
			super.setChanged();
			if (parentToDo != null) {
				parentToDo.setChanged();
			} 
		}
		
		@Override
		public void notifyObservers(Object arg) {
			super.notifyObservers(arg);
			if(parentToDo != null) {
				parentToDo.notifyObservers(arg);
			}
		}
 	}
