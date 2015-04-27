package fr.tactik.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class LevelPanelContainer extends JScrollPane{
	private static final long serialVersionUID = 1L;
	private static LevelPanel levelView;
	int width, height;
	
	//TODO : afficher tiles / plateform / objects delimitations ?
	
	public LevelPanelContainer(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	
	public int initView(int width, int height, String background) {

		levelView = new LevelPanel(width, height, background);
		if (-1 == levelView.init()){
			return -1;
		}
		
		JPanel outer = new JPanel();
		outer.add(levelView);
	
		outer.setBackground(Color.gray);
		getViewport().add(outer, BorderLayout.NORTH);
		revalidate();
		return 1;
	}

}
