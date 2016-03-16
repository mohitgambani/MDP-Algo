package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private boolean isExplored;

	public MapComponent(int id) {
		super();
		this.id = id;
		setOpenSpace();
		addActionListener(new MyActionListener());
		addMouseListener(new MyMouseListener());
		addKeyListener(new MyKeyListener());

	}

	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isStartZone && !isGoalZone && !isRobot) {
				// MapManager.setObstacle(id);
				setObstacle();
			}
		}
	}

	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				if (isObstacle) {
					// MapManager.unsetObstacle(id);
					unsetObstacle();
				} else {
					MapManager.initialiseRobot(id);
					requestFocusInWindow();
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

	private class MyKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (isRobot) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					MapManager.headEast(true);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					MapManager.headWest(true);
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					MapManager.headNorth(true);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					MapManager.headSouth(true);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

	public boolean isObstacle() {
		return isObstacle;
	}

	public void setObstacle() {
		isObstacle = true;
		setBackground(Color.BLACK);
	}

	public void unsetObstacle() {
		isObstacle = false;
		if (isExplored) {
			setExplored();
		}else {
			setBackground(Color.WHITE);
		}
	}

	public boolean isStartZone() {
		return isStartZone;
	}

	public void setStartZone() {
		setOpenSpace();
		isStartZone = true;
		setBackground(Color.BLUE);
	}

	public boolean isGoalZone() {
		return isGoalZone;
	}

	public void setGoalZone() {
		setOpenSpace();
		isGoalZone = true;
		setBackground(Color.RED);
	}

	public boolean isRobot() {
		return isRobot;
	}

	public void setRobot() {
		isRobot = true;
		setBackground(Color.GREEN);
		
//		if(isObstacle){
//			System.out.println("Wrong");
//		}
	}

	public void setRobotHead() {
		isRobot = true;
		setBackground(Color.MAGENTA);
		
//		if(isObstacle){
//			System.out.println("Wrong");
//		}
	}

	public void setExplored() {
		if (!isStartZone && !isGoalZone && !isRobot) {
			setOpenSpace();
			setBackground(Color.YELLOW);
		}
		isExplored = true;
	}

	public void unsetRobot() {
		isRobot = false;
		if (isStartZone) {
			setStartZone();
		} else if (isGoalZone) {
			setGoalZone();
		} else if (isExplored) {
//			System.out.println(id);
			setExplored();
		} else {
			setOpenSpace();
		}
	}

	public void setOpenSpace() {
		isObstacle = isRobot = isStartZone = isGoalZone = isExplored = false;
		setBackground(Color.WHITE);
	}
}
