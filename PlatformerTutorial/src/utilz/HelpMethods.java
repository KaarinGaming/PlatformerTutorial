package utilz;

import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Projectile;
import objects.Spike;

public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}

	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}

	public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);

	}

	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];

		if (value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}

	public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
	}

	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}

	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvlData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}
		return true;
	}

	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}

	public static int[][] GetLevelData(BufferedImage img) {
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;
	}

	public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
		ArrayList<Crabby> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == CRABBY)
					list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}

	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}

	public static ArrayList<Potion> GetPotions(BufferedImage img) {
		ArrayList<Potion> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == RED_POTION || value == BLUE_POTION)
					list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}

	public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
		ArrayList<GameContainer> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == BOX || value == BARREL)
					list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}

	public static ArrayList<Spike> GetSpikes(BufferedImage img) {
		ArrayList<Spike> list = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == SPIKE)
					list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
			}

		return list;
	}

	public static ArrayList<Cannon> GetCannons(BufferedImage img) {
		ArrayList<Cannon> list = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == CANNON_LEFT || value == CANNON_RIGHT)
					list.add(new Cannon(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}

}