package fr.tactik.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import sun.awt.WindowClosingListener;

public class EditorWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String picturesFolder = "images/icons/";
	private static LevelStruct currentLevelFile;


	private static LevelPanel levelPanel;
	private static TilesContentPanel tilesContentPanel;
	private static JSplitPane splitPane;

	//buttons
	private static JButton saveLevel;
	
	//level file extension
	private static final String extName = "blkt"; //à rendre plus sérieux
	public static EditorWindow  win;
	
	//Check if the editor is open
	static boolean isOpen = false;
	
	private EditorWindow() {};
	
	public static EditorWindow createWindow(int w, int h) {
		if (win == null && !getIsOpen()){
			win = new EditorWindow();
			win.setTitle("level Editor");
			win.setSize(800,800);
			win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			win.setIconImage(new ImageIcon("images/icon_editor.png").getImage());
	
			createMenu(win);

			JToolBar toolBar = new JToolBar("Still draggable");
			toolBar.setPreferredSize(new Dimension(w, 30));
			toolBar.setFloatable(false);
			addComponents(toolBar, win);
	
			JPanel mainPanel = new JPanel();
			addPanels(mainPanel, w, h);
			mainPanel.add(toolBar, BorderLayout.NORTH);
			win.add(mainPanel);
			setIsOpen(true);
			
			win.addWindowListener(new WindowListener() {
		            public void windowClosed(WindowEvent arg0) {}
		            public void windowActivated(WindowEvent arg0) {}
		            public void windowClosing(WindowEvent arg0) {
		                setIsOpen(false);
		                win = null;
		            }
		            public void windowDeactivated(WindowEvent arg0) {}
		            public void windowDeiconified(WindowEvent arg0) {}
		            public void windowIconified(WindowEvent arg0) {}
		            public void windowOpened(WindowEvent arg0) {}
		        });
			
			win.setVisible (true);
		}
			
		return win;
	}
	
	public Object clone()
	throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException(); 
	}
	
	public static void setIsOpen(boolean status){
		isOpen = status;
	}
	
	public static boolean getIsOpen(){
		return isOpen;
	}
	
	private static void createMenu(final EditorWindow win) {
		JMenuBar menuBar = new JMenuBar();
		JMenu project = new JMenu("Project");
		JMenu about = new JMenu("About the Editor");
		
		final JMenuItem newItem = new JMenuItem ("New");
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		project.add(newItem);
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newLevel(win);
			}
		});

		final JMenuItem openItem = new JMenuItem ("Open");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		project.add(openItem);
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openLevel(win);
			}
		});


		
		final JMenuItem save = new JMenuItem ("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		project.add(save);
		
		final JMenuItem quit = new JMenuItem ("Quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		project.add(quit);
		menuBar.add(project);
		menuBar.add(about);
		
		win.setJMenuBar(menuBar);
	}
	
	private static void addComponents(JToolBar toolBar, final EditorWindow win) {
		
		JButton newLevelConfFile = new JButton(new ImageIcon(picturesFolder + "newLevel.png"));
		newLevelConfFile.setBorderPainted(false); 
		newLevelConfFile.setContentAreaFilled(false);
		newLevelConfFile.setToolTipText("New Level"); 
		toolBar.add(newLevelConfFile);
		
		toolBar.addSeparator();
		JButton loadLevelConfigFile = new JButton(new ImageIcon(picturesFolder + "import.png"));
		loadLevelConfigFile.setToolTipText("open an existant level configuration file");
		loadLevelConfigFile.setBorderPainted(false); 
		loadLevelConfigFile.setContentAreaFilled(false);
		toolBar.add(loadLevelConfigFile);
		
		saveLevel = new JButton(new ImageIcon(picturesFolder + "save.png"));
		saveLevel.setToolTipText("Save level");
		saveLevel.setEnabled(false);
		saveLevel.setBorderPainted(false); 
		saveLevel.setContentAreaFilled(false);
		toolBar.add(saveLevel);
		
		//events
		
		loadLevelConfigFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openLevel(win);
			}
		});
		
		newLevelConfFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newLevel(win);
			}
		});
	}
	
	private static void addPanels(JPanel mainPanel, int width, int height) {
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		
		levelPanel = new LevelPanel(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tilesContentPanel = new TilesContentPanel(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tilesContentPanel, levelPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(width/5);
		splitPane.setContinuousLayout(true);
		
		mainPanel.add(splitPane);
	}
	
	// TODO new level
	private static void newLevel(EditorWindow win){
		//TODO : delete old level view before display the new one.
		
				if(currentLevelFile == null){
					if(DialogWindow.createDialog(win)){
						createNewLevel();
					}
				}else{
					//TODO : seulement si des modifs ont été faites et pas enregistrées
					int option = JOptionPane.showConfirmDialog(
						    win,
						    "Do you want to save the project before creating a new one ?",
						    "Save project",
						    JOptionPane.YES_NO_CANCEL_OPTION); // 0 1 2

					switch(option){
					case 0 :
						saveLevel(win);
						currentLevelFile = null;
						newLevel(win);
						break;
					case 1:
						currentLevelFile = null; 
						tilesContentPanel.repaint();
						// TODO : destruction, garbage collector toussa ? refresh etc etc
						break;
					case 2 :
						break;
					}
				}
	}
	
	// load level
	private static void openLevel(EditorWindow win){
		if(currentLevelFile == null){
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open a level configuration file");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Level (."+extName+")", extName);
			chooser.setFileFilter(filter);
			chooser.setMultiSelectionEnabled(false);
			if(chooser.showOpenDialog(win) == JFileChooser.APPROVE_OPTION) {
				System.out.println(chooser.getSelectedFile());
				//TODO
			}
		}else{
			int option = JOptionPane.showConfirmDialog(
				    win,
				    "Do you want to save the project before opening a new one ?",
				    "Save project",
				    JOptionPane.YES_NO_CANCEL_OPTION); // 0 1 2

			switch(option){
				case 0 :
					//save()
					currentLevelFile = null;
					// openLevel();
					break;
				case 1:
					currentLevelFile = null;
					levelPanel.repaint();
					// TODO : destruction, garbage collector toussa ? refresh etc etc
					break;
				case 2 :
					break;
			}
		}
	}
	
	// TODO save level
	private static void saveLevel(EditorWindow win){}
	
	private static void createNewLevel(){
		if(-1 == levelPanel.initView(DialogWindow.getLevelWidth(), DialogWindow.getLevelHeight(),DialogWindow.getLevelBackground().toString()))
			return;

		currentLevelFile = new LevelStruct(DialogWindow.getLevelWidth(), DialogWindow.getLevelHeight(), DialogWindow.getLevelBackground());
		// TODO init the view with the background, size etc
		saveLevel.setEnabled(true);
	}

}
