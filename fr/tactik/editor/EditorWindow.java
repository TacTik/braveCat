package fr.tactik.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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


/**
 * This is the EditorWindow Class. This class create the window for the editor.
 * 
 * @author Juliette Belin, Alice Neichols, Denis Tribouillois 
 * @version 1.0
 */
public class EditorWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String picturesFolder = "images/icons/";
	private static LevelStruct currentLevelFile;

	private static LevelPanelContainer levelPanelContainer;
	private static TilesContentPanel tilesContentPanel;
	private static JSplitPane splitPane;

	//buttons
	private static JButton saveLevel;
	private static JButton loadLevelConfigFile;
	
	//menuItems
	private static JMenuItem changeBg;
	private static JMenuItem save;
	private static JMenuItem loadLevel;
	
	public static EditorWindow  win;
	
	//Check if the editor is open
	static boolean isOpen = false;
	
	private EditorWindow() {};
	
	/**
	 * This function create an editor window.
	 * @param w
	 * 			the width of the window.
	 * @param h
	 * 			the height of the window.
	 */
	public static EditorWindow createWindow(int w, int h) {
		if (win == null && !getIsOpen()){
			win = new EditorWindow();
			win.setTitle("level Editor");
			win.setExtendedState(win.MAXIMIZED_BOTH);
			win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			win.setIconImage(new ImageIcon("images/icons/icon_editor.png").getImage());
	
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
	
	/**
	 * This function prevent the window to be cloned.
	 */
	public Object clone()
	throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException(); 
	}
	
	/**
	 * This function set if the window is open or not.
	 * @param status
	 * 			If the window is open, status is true.
	 */
	public static void setIsOpen(boolean status){
		isOpen = status;
	}
	
	/**
	 * This function get if the window is open or not.
	 * @return isOpen
	 * 				If the window is open, isOpen is true.
	 */
	public static boolean getIsOpen(){
		return isOpen;
	}
	
	/**
	 * This function create the menu.
	 */
	private static void createMenu(final EditorWindow win) {
		JMenuBar menuBar = new JMenuBar();
		JMenu project = new JMenu("Project");
		JMenu about = new JMenu("About the Editor");
		
		changeBg = new JMenuItem("Change background");
		changeBg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DialogWindow.createBgDialog(win)){
					levelPanelContainer.changeBg(DialogWindow.getLevelBackground());
				}
			}
		});
		changeBg.setEnabled(false);
		
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

		
		save = new JMenuItem ("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		project.add(save);
		save.setEnabled(false);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogWindow.createSaveLevelDialog(win);
				saveLevel(win);
			}
		});
		
		final JMenuItem quit = new JMenuItem ("Quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		project.add(quit);
		menuBar.add(project);
		menuBar.add(about);
		project.addSeparator();
		project.add(changeBg);
		win.setJMenuBar(menuBar);
		


	}
	
	/**
	 * This function add the components to the tool bar of the editor.
	 */
	private static void addComponents(JToolBar toolBar, final EditorWindow win) {
		
		JButton newLevelConfFile = new JButton(new ImageIcon(picturesFolder + "newLevel.png"));
		newLevelConfFile.setBorderPainted(false); 
		newLevelConfFile.setContentAreaFilled(false);
		newLevelConfFile.setToolTipText("New Level"); 
		toolBar.add(newLevelConfFile);
		
		toolBar.addSeparator();
		loadLevelConfigFile = new JButton(new ImageIcon(picturesFolder + "import.png"));
		loadLevelConfigFile.setToolTipText("Open an existant level configuration file");
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
		
		saveLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogWindow.createSaveLevelDialog(win);
				LevelPanel.writeFileFromEditor(DialogWindow.getLevelPath());
			}
		});
		
	}
	
	/**
	 * This function add the panel of the editor.
	 */
	private static void addPanels(JPanel mainPanel, int width, int height) {
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		
		levelPanelContainer = new LevelPanelContainer(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tilesContentPanel = new TilesContentPanel(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tilesContentPanel, levelPanelContainer);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(width/5);
		splitPane.setContinuousLayout(true);
		
		mainPanel.add(splitPane);
	}
	
	/**
	 * This function add the panel of the editor.
	 */
	private static void newLevel(EditorWindow win){
		
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
						newLevel(win);
						break;
					case 2 :
						break;
					}
				}
	}
	
	// load level
	/**
	 * This function load a level.
	 */
	private static void openLevel(EditorWindow win){
		if(currentLevelFile == null){
			if(DialogWindow.createOpenExistingLevelDialog(win));
				openLevelFromFile(DialogWindow.getLevelPath());
		}else{
			int option = JOptionPane.showConfirmDialog(
				    win,
				    "Do you want to save the project before opening a new one ?",
				    "Save project",
				    JOptionPane.YES_NO_CANCEL_OPTION); // 0 1 2

			switch(option){
				case 0 :
					//save()
					DialogWindow.createSaveLevelDialog(win);
					LevelPanel.writeFileFromEditor(DialogWindow.getLevelPath());
					currentLevelFile = null;
					levelPanelContainer.repaint();
					// openLevel();
					break;
				case 1:
					currentLevelFile = null;
					levelPanelContainer.repaint();
					break;
				case 2 :
					break;
			}
		}
	}
	
	/**
	 * This function save a level.
	 */
	private static void saveLevel(EditorWindow win){
		LevelPanel.writeFileFromEditor(DialogWindow.getLevelPath());
	}
	
	/**
	 * This function create a new level.
	 */
	private static void createNewLevel(){
		if(-1 == levelPanelContainer.initView(DialogWindow.getLevelWidth(), DialogWindow.getLevelHeight(),DialogWindow.getLevelBackground()))
			return;

		currentLevelFile = new LevelStruct(DialogWindow.getLevelWidth(), DialogWindow.getLevelHeight(), DialogWindow.getLevelBackground());
		saveLevel.setEnabled(true);
		changeBg.setEnabled(true);
		save.setEnabled(true);
	}
	
	private static void openLevelFromFile(String file){
		if(-1 == levelPanelContainer.initView(DialogWindow.getLevelWidth(), DialogWindow.getLevelHeight(),DialogWindow.getLevelBackground()))
			return;
		
		levelPanelContainer.setViewFromFile(file);
		currentLevelFile = new LevelStruct(DialogWindow.getLevelWidth(), DialogWindow.getLevelHeight(), DialogWindow.getLevelBackground());
		saveLevel.setEnabled(true);
		changeBg.setEnabled(true);
		save.setEnabled(true);
	}

}
