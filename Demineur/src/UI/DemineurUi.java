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
	private Scene scene;
	
	
	public DemineurUi(Stage s) {
		this.instance = this;
		this.stage = s;		
		VBox vbox = new VBox();
		this.scene = new Scene(vbox);		
		this.stage.setScene(scene);
		
		MenuItem facile = new MenuItem("Facile");
		
		HBox hbox = new HBox();
		MenuBar mb = new MenuBar(initMenuJeu(vbox),initMenuDifficulte(facile,vbox));
		mb.setMinSize(140, 25);
		textBombes = new TextField(String.valueOf(bombes-nbFlags));
		textBombes.setPrefSize(45, 25);
		textBombes.setFocusTraversable(false);
		textBombes.setEditable(false);
		
		textBombes.setOnAction((e) -> 
		{
			textBombes.setText(String.valueOf(bombes-nbFlags));
		});		
		
		hbox.getChildren().addAll(mb,textBombes);
		vbox.getChildren().add(hbox);
				
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
				initGrille();
				initPartie(vbox);
			}
		});
		
		restart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				premierCoup = false;
				map.reset();
				initPartie(vbox);
			}
		});
		jeu.getItems().addAll(nouveau,restart);
		return jeu;
	}
	
	private void initPartie(VBox vbox)
	{
		stage.setTitle("Démineur");
		nbFlags = 0;
		termine = false;
		setGrille(vbox);
		vbox.autosize();
		textBombes.fireEvent(new ActionEvent());
		stage.setWidth(vbox.getWidth());
		stage.setHeight(vbox.getHeight());
	}

	private Menu initMenuDifficulte(MenuItem facile,VBox vbox)
	{
		Menu diff = new Menu("Difficulté");		
		
		
		MenuItem moyen = new MenuItem("Moyen");
		MenuItem difficile = new MenuItem("Difficile");
		MenuItem perso = new MenuItem("Personnalisée");
		
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
		
		perso.setOnAction((e) -> 
		{
			selectionJeuPerso();
			this.termine = true;
			this.setGrille(vbox);			
		});
		diff.getItems().addAll(facile,moyen,difficile,perso);
		return diff;
	}
	
	private void selectionJeuPerso() {
		//Stage stageChoix = new Stage();	
		VBox vboxChoix = new VBox();
		Scene sceneChoix = new Scene(vboxChoix);		
		//stageChoix.setScene(scene);
		//stageChoix.setTitle("Choix des paramètres");
		
		GridPane grid = new GridPane();
		grid.setHgap(3);
		grid.setVgap(3);
	    grid.setPadding(new Insets(5, 5, 5, 5));

	    TextField tW = new TextField("9");
	    TextField tH = new TextField("9");
	    TextField tB = new TextField("10");
	    tW.setMaxSize(45,25);tW.setMinSize(45,25);	    
	    tH.setMaxSize(45,25);tH.setMinSize(45,25);
	    tB.setMinSize(45,25);tB.setMaxSize(45,25);
	    grid.add(tW, 3, 0, 1, 1);
	    grid.add(tH, 3, 1, 1, 1);
	    grid.add(tB, 3, 2, 1, 1);
	    
	    Slider sW = new Slider();
	    Slider sH = new Slider();
	    Slider sB = new Slider();
	    sW.setMinSize(230, 25);
	    sH.setMinSize(230, 25);
	    sB.setMinSize(230, 25);
	    grid.add(sW, 0, 0, 3, 1);
	    grid.add(sH, 0, 1, 3, 1);
	    grid.add(sB, 0, 2, 3, 1);
	    
	    sW.setMin(9);
	    sW.setMax(32);
	    sW.setMajorTickUnit(1);
	    sW.setOnMouseDragged((e) ->
	    {
	    	tW.setText(String.valueOf((int) (sW.getValue())));
		    sB.setMax((int) (sH.getValue()* sW.getValue() * 0.85));
	    });
	    sH.setMin(9);
	    sH.setMax(32);
	    sH.setMajorTickUnit(1);
	    sH.setOnMouseDragged((e) ->
	    {
	    	tH.setText(String.valueOf((int) (sH.getValue())));
		    sB.setMax((int) (sW.getValue()* sH.getValue() * 0.85));
	    });
	    
	    sB.setMin(10);
	    sB.setMax(68);
	    sB.setMajorTickUnit(1);
	    sB.setOnMouseDragged((e) ->
	    {
	    	tB.setText(String.valueOf((int) (sB.getValue())));
	    });
	    
	    tB.setOnKeyTyped((e) -> 
	    {
	    	try
	    	{
	    		sB.setValue((int) Math.max(Integer.valueOf(tB.getText()),(int) sB.getMin()));
	    	} catch (NumberFormatException ex) { tB.setText(String.valueOf((int) sB.getMin()));}
	    });
	    tW.setOnKeyTyped((e) -> 
	    {
	    	try
	    	{
	    		sW.setValue((int) Math.max(Integer.valueOf(tW.getText()),(int) sW.getMin()));
	    	} catch (NumberFormatException ex) { tW.setText(String.valueOf((int) sW.getMin()));}
	    });
	    tH.setOnKeyTyped((e) -> 
	    {
	    	try
	    	{
	    		sH.setValue((int) Math.max(Integer.valueOf(tH.getText()),(int) sH.getMin()));
	    	} catch (NumberFormatException ex) { tH.setText(String.valueOf((int) sH.getMin()));}
	    });
		HBox hboxChoix = new HBox();
		Button btn = new Button("Valider");
		Button btn3 = new Button("Annuler");
		Button btn2 = new Button("Quitter");

		btn.setOnAction((e) -> {
			taillex = Integer.valueOf(tW.getText());
			tailley = Integer.valueOf(tH.getText());
			bombes = Integer.valueOf(tB.getText());
			stage.setScene(this.scene);
			nouveau.fire();
		});
		
		btn2.setOnAction((e) -> {
			this.stage.close();
		});
		btn3.setOnAction((e) -> {
			stage.setScene(this.scene);
			nouveau.fire();
		});
		btn.setMinSize(100, 40);
		btn2.setMinSize(100, 40);
		btn3.setMinSize(100, 40);
		hboxChoix.getChildren().addAll(btn,btn3,btn2);
		vboxChoix.getChildren().addAll(grid,hboxChoix);
		this.stage.setScene(sceneChoix);
		vboxChoix.setMinSize(3 * btn.getMinWidth() + 5, 3* (3+sW.getMinHeight())+ 10 + btn.getMinHeight() +25);
		
		stage.setWidth(vboxChoix.getMinWidth());
		stage.setHeight(vboxChoix.getMinHeight());
		/*
		stageChoix.setResizable(false);
		stageChoix.show();*/
		
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
		Stage stageF = new Stage();
		HBox hboxFin = new HBox();
		Scene sceneFin = new Scene(hboxFin);	
		if(fin == 1) stageF.setTitle("Victoire !");
		else stageF.setTitle("Défaite");
		Button btn = new Button("Rejouer");
		Button btn3 = new Button("Recommencer");
		Button btn2 = new Button("Quitter");

		btn.setOnAction((e) -> {
			stage.setScene(this.scene);
			nouveau.fire();
			stageF.close();
		});
		
		btn2.setOnAction((e) -> {
			this.stage.close();
			stageF.close();
		});
		btn3.setOnAction((e) -> {
			stage.setScene(this.scene);
			restart.fire();
			stageF.close();
		});
		btn.setMinSize(95, 40);
		btn2.setMinSize(95, 40);
		btn3.setMinSize(95, 40);
		hboxFin.getChildren().addAll(btn,btn3,btn2);	
		stageF.setScene(sceneFin);
		stageF.show();
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
