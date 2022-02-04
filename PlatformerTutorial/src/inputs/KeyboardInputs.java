package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import static utilz.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_A:
		case KeyEvent.VK_S:
		case KeyEvent.VK_D:
			gamePanel.setMoving(false);
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			gamePanel.setDirection(UP);
			break;
		case KeyEvent.VK_A:
			gamePanel.setDirection(LEFT);
			break;
		case KeyEvent.VK_S:
			gamePanel.setDirection(DOWN);
			break;
		case KeyEvent.VK_D:
			gamePanel.setDirection(RIGHT);
			break;
		}
	}
}
