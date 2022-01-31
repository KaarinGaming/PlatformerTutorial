package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private float xDelta = 100, yDelta = 100;
	private BufferedImage img, subImg;

	public GamePanel() {

		mouseInputs = new MouseInputs(this);

		importImg();

		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);

	}

	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/player_sprites.png");

		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setPanelSize() {
		Dimension size = new Dimension(1280, 800);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);

	}

	public void changeXDelta(int value) {
		this.xDelta += value;

	}

	public void changeYDelta(int value) {
		this.yDelta += value;

	}

	public void setRectPos(int x, int y) {
		this.xDelta = x;
		this.yDelta = y;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		subImg = img.getSubimage(1 * 64, 8 * 40, 64, 40);
		g.drawImage(subImg, (int) xDelta, (int) yDelta, 128, 80, null);

	}

}
