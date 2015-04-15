package fr.tactik.game;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * The main window. Contains the whole universe.
 *
 */
public class GameWindow extends JFrame{

	Menu gameMenu = new Menu();
	int[] gameGrid;
	static String rootDir = System.getProperty("user.dir");
	
	GameWindow(){
		setTitle("Un super Titre de Jeu OH YEAH");
		setSize(800,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("images/icon.png").getImage());
		
		Menu gameMenu = new Menu();
		setJMenuBar(gameMenu);
		
		gameGrid = GameLoader.load("monFichier.bct");

		getContentPane().add(createPanel());
		
		setVisible (true);		
	}
	
	
	public static JPanel createPanel(){

		GameDisplay gamePanel = new GameDisplay();
		System.out.println(rootDir);
		
		
		gamePanel.setBackground(rootDir + "/images/game/background.jpg"); 		
		
		return gamePanel;
	}
}