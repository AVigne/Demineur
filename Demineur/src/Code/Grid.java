package Code;

public class Grid {

	Cell[][] tableau;
	
	public Grid(int sizex,int sizey,int bombs) {
		tableau = new Cell[sizex][sizey];
		for(int i = 0; i < sizex; i++) for(int j = 0; j < sizey; j++) tableau[i][j] = new Cell();
		if(bombs > sizex*sizey - 1) bombs = sizex*sizey - 1;
		while(bombs > 0)
		{
			int x = (int) (Math.random()*sizex);
			int y = (int) (Math.random()*sizey);
			if(!tableau[x][y].isTrap()) {
				tableau[x][y].setTrap(true);
				bombs--;
				actuTableau(x,y,tableau);
			}
		}
	}

	public String toString()
	{
		String res = "";
		for(Cell[] line : tableau)
		{
			for(Cell cell : line)
			{
				if (!cell.isTrap()) res+= cell.getVoisins();
				else res+="_";
			}
			res+="\n";
		}
		return res;
	}
	public String toString2()
	{
		String res = "";
		for(Cell[] line : tableau)
		{
			for(Cell cell : line)
			{
				if(cell.isShown())
				{
					if (!cell.isTrap()) res+= cell.getVoisins();
					else res+="_";
				}
				else
				{
					res+=" ";
				}
			}
			res+="\n";
		}
		return res;
	}
	
	public Cell getCell(int i, int j)
	{
		return tableau[i][j];
	}
	
	
	private void actuTableau(int x, int y, Cell[][] tab)
	{
		for(int i = Math.max(-1, -x); i<= Math.min(1, tab.length - x-1); i++)
		{
			for(int j = Math.max(-1, -y); j<= Math.min(1, tab[0].length - y-1); j++)
			{
				if((i != 0 || j != 0) && (!tableau[x+i][y+j].isTrap()))
				{
					tableau[x+i][y+j] = new Cell(tableau[x+i][y+j].getVoisins()+1);
				}
			}
		}
	}

	public void show(int x, int y)
	{
		tableau[x][y].show();
		if(tableau[x][y].isTrap() || tableau[x][y].getVoisins() < getFlaggedVoisins(x,y))
			return;
		for(int i = Math.max(-1, -x); i<= Math.min(1, tableau.length - x-1); i++)
		{
			for(int j = Math.max(-1, -y); j<= Math.min(1, tableau[0].length - y-1); j++)
			{
				if((i != 0 || j != 0) && !tableau[x+i][y+j].isFlag() && (!tableau[x+i][y+j].isTrap()) && !tableau[x+i][y+j].isShown())
				{
					//tableau[x+i][y+j].show();
					if( tableau[x+i][y+j].getVoisins() == 0) this.show(x+i, y+j);
				}
				if( tableau[x][y].getVoisins() == getFlaggedVoisins(x,y) && !tableau[x+i][y+j].isFlag()) tableau[x+i][y+j].show();
			}
		}
	}

	private int getFlaggedVoisins(int x, int y)
	{
		int res = 0;
		for(int i = Math.max(-1, -x); i<= Math.min(1, tableau.length - x-1); i++)
			for(int j = Math.max(-1, -y); j<= Math.min(1, tableau[0].length - y-1); j++)
				if((i != 0 || j != 0) && (tableau[x+i][y+j].isFlag()))
					res++;
		return res;
	}

	public void reset() {
		for(Cell[] line : this.tableau) for(Cell cell : line) 
		{
			cell.setFlag(false);
			cell.setShown(false);
		}
			
	}
	
	
	public void showAll() {
		for(Cell[] line : this.tableau) for(Cell cell : line) 
		{
			if(cell.isTrap())
				cell.setShown(true);
		}
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
