package tetris.components;

import java.util.*;

import javafx.scene.paint.Color;

public class ResourceCenter {

	protected static ResourceCenter instance;
	
	protected ResourceCenter() {
		this.foreground = new Color(1, 1, 1, 1);
		this.background = new Color(0, 0, 0, 1);
		this.shadow = new Color(74.0/256, 68.0/256, 70.0/256, 0.7);
		this.empty = new Color(201.0/256, 192.0/256, 187.0/256, 1);
		
		this.colors = new TreeMap<Integer, Color>();
		this.colors.put(-1, shadow);
		this.colors.put(0, empty);
		this.colors.put(1, Color.YELLOW);
		this.colors.put(2, new Color(139.0/256, 0, 1, 1));
		this.colors.put(3, Color.RED);
		this.colors.put(4, new Color(51.0/256, 204.0/256, 1, 1));
		this.colors.put(5, Color.GREEN);
		this.colors.put(6, Color.ORANGE);
	}
	
	protected Color background;
	protected Color foreground;
	protected Color empty;
	protected Color shadow;
	protected TreeMap<Integer, Color> colors;
	
	public static ResourceCenter getInstance() {
		if (instance == null) {
			instance = new ResourceCenter();
		}
		return instance;
	}
	
	public void loadXMLSetting(String path) {
		throw new UnsupportedOperationException();
	}
	
	public Color getBackground() {
		return this.background;
	}
	
	public Color getForeground() {
		return this.foreground;
	}
	
	public Color getEmpty() {
		return this.empty;
	}
	
	public Color getShadowColor() {
		return this.shadow;
	}
	
	public Color getColor(int i) {
		Color col = colors.get(i);
		if (col != null) {
			return col;
		}
		else {
			return this.empty;
		}
	}
	
	public int getColorRange() {
		return this.colors.size();
	}
	
	public int getColorIndex(Color col) {
		for (Map.Entry<Integer, Color> p : colors.entrySet()) {
			if (col.equals(p.getValue())) {
				return p.getKey();
			}
		}
		return 0;
	}
	
}
