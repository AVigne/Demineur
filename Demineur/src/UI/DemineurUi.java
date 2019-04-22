package UI;



import Code.*;
import Code.Cell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class DemineurUi {
	
	private Stage stage;
	private int taillex;
	private int tailley;
	private int bombes;
	private Grid map;
	private DemineurUi instance;
	private GridPane grille = null;
	private int nbFlags = 0;
	private TextField textBombes;
	private boolean termine = false;
	private MenuItem nouveau;
	private MenuItem restart;
	private boolean premierCoup = true;
	
	
	public DemineurUi(Stage s) {
		this.instance = this;
		this.stage = s;		
		VBox vbox = new VBox();
		Scene scene = new Scene(vbox);		
		this.stage.setScene(scene);

		
		MenuItem facile = new MenuItem("Facile");
		
		HBox hbox = new HBox();
		MenuBar mb = new MenuBar(initMenuJeu(vbox),initMenuDifficulte(facile));
		mb.setMinSize(140, 25);
		textBombes = new TextField(String.valueOf(bombes-nbFlags));
		textBombes.setPrefSize(30, 25);
		textBombes.setFocusTraversable(false);
		textBombes.setEditable(false);
		
		textBombes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textBombes.setText(String.valueOf(bombes-nbFlags));
			}			
		});
		
		hbox.getChildren().addAll(mb,textBombes);
		vbox.getChildren().add(hbox);
		
		
		stage.setTitle("Démineur");
		stage.setResizable(false);
		stage.show();

		facile.fire();
	}

	private Menu initMenuJeu(VBox vbox)
	{
		Menu jeu = new Menu("Jeu");
		nouveau = new MenuItem("Nouvelle Partie");
		restart = new MenuItem("Recommencer");	

		nouveau.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				premierCoup = true;
				nbFlags = 0;
				termine = false;
				initGrille();
				setGrille(vbox);
				vbox.autosize();
				textBombes.fireEvent(new ActionEvent());
				stage.setWidth(vbox.getWidth());
				stage.setHeight(vbox.getHeight());
			}
		});
		
		restart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				premierCoup = false;
				nbFlags = 0;
				termine = false;
				map.reset();
				textBombes.fireEvent(new ActionEvent());
				setGrille(vbox);
			}
		});
		jeu.getItems().addAll(nouveau,restart);
		return jeu;
	}

	private Menu initMenuDifficulte(MenuItem facile)
	{
		Menu diff = new Menu("Difficulté");		
		
		
		MenuItem moyen = new MenuItem("Moyen");
		MenuItem difficile = new MenuItem("Difficile");
		
		facile.setOnAction((e) -> 
		{
			taillex = 9;
			tailley = 9;
			bombes = 10;
			nouveau.fire();		
		});
		
		moyen.setOnAction((e) -> 
		{
			taillex = 16;
			tailley = 16;
			bombes = 40;
			nouveau.fire();
		});
		
		difficile.setOnAction((e) -> 
		{
			taillex = 30;
			tailley = 16;
			bombes = 99;
			nouveau.fire();
		});
		diff.getItems().addAll(facile,moyen,difficile);
		return diff;
	}
	
	private void initGrille()
	{
		this.map =  new Grid(taillex,tailley,bombes);
	}
	
	private void setGrille(VBox vbox) {
		if(this.grille != null) vbox.getChildren().remove(this.grille);
		this.grille = new GridPane();
		grille.setHgap(3);
		grille.setVgap(3);
	    grille.setPadding(new Insets(5, 5, 5, 5));

		this.grille.setMinSize(taillex*28+15, (1+tailley)*28+10);
		for(int i = 0; i < taillex; i++)
			for(int j = 0; j < tailley; j++)
			{
				final int i2 = i;
				final int j2 = j;
				Button btnTmp;
				if(map.getCell(i2, j2).isShown())
				{
					btnTmp = new Button();
					String strTmp = map.getCell(i2, j2).toString();
					if(strTmp.equals("0"))
					{
						strTmp = " ";
					}
					btnTmp.setText(strTmp);
					if(!map.getCell(i2, j2).isTrap()) 
						btnTmp.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));	
					else
						btnTmp.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));	
				}
				else
				{
					btnTmp = new Button(" ");	
					if(map.getCell(i2, j2).isFlag())
					{	
						btnTmp.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));	
						btnTmp.setText("F");
					}
					else
					{
						btnTmp.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
					}
				}
				btnTmp.setMouseTransparent(termine);
				btnTmp.setOnMousePressed(new EventHandler<MouseEvent>() {
					
					private int posx = i2;
					private int posy = j2;
					
					@Override
					public void handle(MouseEvent event) {
						while(premierCoup && instance.map.getCell(posx, posy).isTrap())
						{
							//System.out.println("wolo");
							initGrille();
						}
						if(event.isPrimaryButtonDown() && ! map.getCell(posx, posy).isFlag())
						{
							map.show(posx,posy);
						}
						if(event.isSecondaryButtonDown() && ! map.getCell(posx, posy).isShown())
						{
							map.getCell(posx, posy).flag();
							if(map.getCell(posx, posy).isFlag())
								nbFlags++;
							else
								nbFlags--;
							textBombes.fireEvent(new ActionEvent());
						}
						int fin = testFinPartie();
						termine = fin != 0;
						premierCoup = false;
						instance.setGrille(vbox);
						if(termine)
						{
							finPartie(fin);	
							map.showAll();
							instance.setGrille(vbox);							
						}
					}
				});
				
				btnTmp.setMinSize(25, 25);
				btnTmp.setMaxSize(25, 25);
				
				grille.add(btnTmp, i,j);
			}

	    this.grille.autosize();
		vbox.getChildren().add(grille);
		
	}
	
	private void finPartie(int fin) {
		Stage stageFin = new Stage();		
		HBox hboxFin = new HBox();
		Scene scene = new Scene(hboxFin);		
		stageFin.setScene(scene);
		if(fin == 1) stageFin.setTitle("Victoire !");
		else stageFin.setTitle("Défaite");
		Button btn = new Button("Rejouer");
		Button btn3 = new Button("Recommencer");
		Button btn2 = new Button("Quitter");

		btn.setOnAction((e) -> {
			nouveau.fire();
			stageFin.close();
		});
		
		btn2.setOnAction((e) -> {
			this.stage.close();
			stageFin.close();
		});
		btn3.setOnAction((e) -> {
			restart.fire();
			stageFin.close();
		});
		btn.setPrefSize(100, 40);
		btn2.setPrefSize(100, 40);
		btn3.setPrefSize(100, 40);
		hboxFin.getChildren().addAll(btn,btn3,btn2);
		stageFin.show();
	}

	private int testFinPartie()
	/**
	 * 1 si victoire
	 * 0 si partie non terminée
	 * -1 si défaite
	 */
	{
		int cpt = 0;
		int cpt2 = taillex*tailley - bombes;
		for(int i = 0; i < taillex; i++) for(int j = 0; j < tailley; j++)
		{
			Cell tmp1 = map.getCell(i, j);
			if(tmp1.isTrap())
			{
				if(tmp1.isShown()) return -1;
				if(tmp1.isFlag()) cpt++;
			}
			if(!tmp1.isTrap() && tmp1.isShown()) cpt2--;
		}
		if((cpt == bombes && cpt2 == 0) || cpt2 == 0) return 1;
		return 0;
	}

}
