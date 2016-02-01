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

	public MapComponent(int id) {
		super();
		this.id = id;
		setOpenSpace();
		if (id >= 0) {
			addActionListener(new MyActionListener());
			addMouseListener(new MyMouseListener());
			addKeyListener(new MyKeyListener());
		}
	}

	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isStartZone && !isGoalZone && !isRobot) {
				setObstacle();
			}
		}
	}

	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				if (isObstacle) {
					setOpenSpace();
				} else {
					MapManager.setRobot(id, RobotManager.getRobotOrientation(), true);
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
					MapManager.robotHeadRight();
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					MapManager.robotHeadLeft();
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					MapManager.robotHeadUp();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					MapManager.robotHeadDown();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
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

	public void unSetIsRobot() {
		isRobot = false;
		if (isStartZone) {
			setStartZone();
		} else if (isGoalZone) {
			setGoalZone();
		} else {
			setOpenSpace();
		}
	}

	public void setOpenSpace() {
		isObstacle = isStartZone = isRobot = isGoalZone = false;
		setBackground(Color.WHITE);
	}

	public void setRobotHead() {
		isRobot = true;
		setBackground(Color.MAGENTA);
	}

	@Override
	public String toString() {
		return id + "";
	}

}
