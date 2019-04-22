package Code;

public class ICell {

	private boolean shown;
	private boolean drapeau = false;
	
	
	public boolean isTrap()
	{
		return false;
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
