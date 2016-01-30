package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MapComponent extends JButton{
	
	private int id;
	private boolean isObstacle;
	private boolean isStartZone;
	private boolean isGoalZone;
	
	public MapComponent(int id){
		super();
		this.id = id;
		isObstacle = isStartZone = isGoalZone = false;
		addActionListener(new MyActionListener());
		setBackground(Color.WHITE);
	}
	
	private class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!isStartZone && !isGoalZone){
				setObstacle();
			}
		}
		
	}
	
	public int getId(){
		return id;
	}
	
	public boolean isObstacle(){
		return isObstacle;
	}
	
	public void setObstacle(){
		isObstacle = true;
		setBackground(Color.BLACK);
	}
	
	public void unsetObstacle(){
		if(isObstacle){
			isObstacle = false;
			setBackground(Color.WHITE);
		}
	}
	
	public boolean isStartZone(){
		return isStartZone;
	}
	
	public void setStartZone(){
		isStartZone = true;
		setBackground(Color.BLUE);
	}
	
	public boolean isGoalZone(){
		return isGoalZone;
	}
	
	public void setGoalZone(){
		isGoalZone = true;
		setBackground(Color.RED);
	}
	
	@Override
	public String toString(){
		return id + "";
	}
	
	
}
