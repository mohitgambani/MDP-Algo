package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class MapComponent extends JButton {

	private int id;
	private boolean isObstacle;
	private boolean isStartZone;
	private boolean isGoalZone;
	private boolean isRobot;

	public MapComponent(int id) {
		super();
		this.id = id;
		isObstacle = isStartZone = isGoalZone = isRobot = false;
		addActionListener(new MyActionListener());
		addMouseListener(new MyMouseListener());
		setBackground(Color.WHITE);
	}

	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(isRobot){
				Thread t = new Thread() {
			         @Override
			         public void run() {
			            MapManager.move();
			         }
			      };
			      t.start();
			}else if (!isStartZone && !isGoalZone) {
				setObstacle();
			}
		}
	}

	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				if (isObstacle) {
					unSetObstacle();
				}else {
					MapManager.setRobot(id);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	public int getId() {
		return id;
	}

	public boolean isObstacle() {
		return isObstacle;
	}

	public void setObstacle() {
		isObstacle = true;
		setBackground(Color.BLACK);
	}

	public void unSetObstacle() {
		if (isObstacle) {
			isObstacle = false;
			setBackground(Color.WHITE);
		}
	}

	public boolean isStartZone() {
		return isStartZone;
	}

	public void setStartZone() {
		isStartZone = true;
		setBackground(Color.BLUE);
	}

	public boolean isGoalZone() {
		return isGoalZone;
	}

	public void setGoalZone() {
		isGoalZone = true;
		setBackground(Color.RED);
	}

	public boolean isRobot() {
		return isRobot;
	}
	
	public void setIsRobot() {
		isRobot = true;
		setBackground(Color.GREEN);
	}
	
	public void unSetIsRobot(){
		isRobot = false;
		if(isStartZone){
			setStartZone();
		}else if(isGoalZone){
			setGoalZone();
		}else{
			setBackground(Color.WHITE);
		}
	}

	@Override
	public String toString() {
		return id + "";
	}

}