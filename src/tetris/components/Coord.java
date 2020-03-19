package tetris.components;

public class Coord implements Cloneable {
	
	protected int x, y;
	
	public Coord(int x, int y) {
		set(x, y);
	}
	
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		else {
			Coord t = (Coord) obj;
			if ((this.x == t.x) && (this.y == t.y))
				return true;
			else
				return false;
		}
	}
	
	public Coord clone() {
		return new Coord(this.x, this.y);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
	