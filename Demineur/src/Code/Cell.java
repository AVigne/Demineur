package Code;

public class Cell extends ICell {
	

	private boolean shown;
	private boolean drapeau = false;	
	private int voisins;
	private boolean trap;

	public Cell(int i)
	{
		this.shown = false;
		this.voisins = i;
		this.trap = false;
	}
	
	public Cell() {
		this(0);
	}

	
	public boolean isTrap() {
		return this.trap;
	}
	
	public void setTrap(boolean b) {
		this.trap = b;
	}
	public int getVoisins() {
		return voisins;
	}

	public void setVoisins(int voisins) {
		this.voisins = voisins;
	}
	
	public String toString() {
		if(isTrap()) return "B";
		return String.valueOf(voisins);
	}


	public boolean isShown() {
		return shown;
	}
	
	public void setShown(boolean shown) {
		this.shown = shown;
	}

	public void show()
	{
		this.shown = true;
	}


	public boolean isFlag() {
		return drapeau;
	}


	public void setFlag(boolean drapeau) {
		this.drapeau = drapeau;
	}
	
	public void flag() {
		this.drapeau = !this.drapeau;
	}
	
	
}
