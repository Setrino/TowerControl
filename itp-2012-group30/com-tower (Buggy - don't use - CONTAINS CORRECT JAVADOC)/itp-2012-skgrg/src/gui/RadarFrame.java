/*
 * Sergei Kostevitch
 * Mar 25, 2012
 */

package gui;

import gui.given.PlaneDrawnForGivenGui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import plane.Plane;

// TODO: Auto-generated Javadoc
/**
 * The Class RadarFrame is the JFrame of the map.png we use throughout for drawing the planes
 * This class takes care of first intantiating shortcuts, (set up to be used for Windows and MacOS,
 * not tested for UNIX though
 * A plane thread is added here as Threads when instantiated
 */
public class RadarFrame extends JFrame implements Runnable, MenuKeyListener,
		ActionListener, WindowListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9180111039407571515L;

	/** The about. */
	private final JMenuItem about;

	/** The close frame. */
	private final JMenuItem closeFrame;

	/** The layered pane. */
	private final JLayeredPane layeredPane;

	/** The twitter log frame. */

	private int layerNumber = 0;

	/** The line component. */
	private final TurningLineComponent lineComponent;

	/** The main menu. */
	private final JMenu mainMenu;

	/** The menu bar. */
	private final JMenuBar menuBar;
	// Three Intermediates used for window closing
	/** The message log intermediate. */
	private int messageLogIntermediate;

	/** The messages log. */
	private final JMenuItem messagesLog;

	/** The messages log frame. */
	private FrameMessageLog messagesLogFrame;

	/** The plane drawn. */
	private PlaneDrawnForGivenGui planeDrawn;

	/** The twitter log. */
	private final JMenuItem twitterLog;

	/** The twitter log intermediate. */
	private int twitterLogIntermediate;

	/**
	 * Instantiates a new radar frame.
	 */

	public RadarFrame() {

		String vers = System.getProperty("os.name").toLowerCase();
		menuBar = new JMenuBar();
		mainMenu = new JMenu("Main");
		setJMenuBar(menuBar);
		menuBar.add(mainMenu);
		about = new JMenuItem("About");
		closeFrame = new JMenuItem("Close");
		twitterLog = new JMenuItem("Twitter Log",
				new ImageIcon("twit_bird.png"));
		messagesLog = new JMenuItem("Messages Log", new ImageIcon(
				"letter_box.png"));
		mainMenu.setMnemonic(KeyEvent.VK_M);

		if (vers.indexOf("windows") != -1) {

			twitterLog.setAccelerator(KeyStroke.getKeyStroke('T',
					InputEvent.CTRL_MASK));
			about.setAccelerator(KeyStroke.getKeyStroke('A',
					InputEvent.CTRL_MASK));
			messagesLog.setAccelerator(KeyStroke.getKeyStroke('M',
					InputEvent.CTRL_MASK));
			closeFrame.setAccelerator(KeyStroke.getKeyStroke('W',
					InputEvent.CTRL_MASK));
		} else if (vers.indexOf("mac") != -1) {
			twitterLog.setAccelerator(KeyStroke.getKeyStroke('T',
					InputEvent.META_MASK));
			about.setAccelerator(KeyStroke.getKeyStroke('A',
					InputEvent.META_MASK));
			messagesLog.setAccelerator(KeyStroke.getKeyStroke('M',
					InputEvent.META_MASK));
			closeFrame.setAccelerator(KeyStroke.getKeyStroke('W',
					InputEvent.META_MASK));
		}

		this.addWindowListener(this);
		// about.addMenuKeyListener(this);
		about.addActionListener(this);
		// twitterLog.addMenuKeyListener(this);
		twitterLog.addActionListener(this);
		// messagesLog.addMenuKeyListener(this);
		messagesLog.addActionListener(this);

		closeFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (messageLogIntermediate == 1) {

					messagesLogFrame.setVisible(false);
				} else if (twitterLogIntermediate == 1) {

				}

			}
		});

		mainMenu.add(about);
		mainMenu.addSeparator();
		mainMenu.add(messagesLog);
		mainMenu.addSeparator();
		mainMenu.add(twitterLog);
		mainMenu.addSeparator();
		mainMenu.add(closeFrame);

		getContentPane().add(new RadarBottomPanel(this), BorderLayout.SOUTH);

		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 1200, 1200);
		lineComponent = new TurningLineComponent();
		lineComponent.setBounds(0, 0, 1200, 1200);
		layeredPane.add(lineComponent, new Integer(layerNumber));
		layerNumber++;
		// addPlaneToTheFrame("LALA");
		getContentPane().add(layeredPane);
		lineComponent.start();
		// addPlaneToTheFrame("LOLO");

		this.pack();
	}

	// Need to fix this method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Item clicked: " + e.getActionCommand());

		if (e.getActionCommand() == "Twitter Log") {

			twitterLogIntermediate = 1;
			messageLogIntermediate = 0;

			if (messagesLogFrame != null) {

				messagesLogFrame.setVisible(false);

			}

		} else if (e.getActionCommand() == "Messages Log") {

			twitterLogIntermediate = 0;
			messageLogIntermediate = 1;

			if (messagesLogFrame == null) {

				messagesLogFrame = new FrameMessageLog(menuBar);
				messagesLogFrame.setVisible(true);
			} else {

				messagesLogFrame.setVisible(true);
			}

		} else if (e.getActionCommand() == "About") {

		}
	}

	/**
	 * Add a plane as a new Thread, as a layer to LayeredPane
	 * 
	 */

	public void addPlaneToTheFrame(String planeName, Plane plane) {

		System.out.println("Added Plane with name " + planeName);
		planeDrawn = new PlaneDrawnForGivenGui(planeName, plane);
		lineComponent.addPlaneDrawingToRadar(planeDrawn);
		planeDrawn.setBounds(0, 0, 1200, 1200);
		layeredPane.add(planeDrawn, new Integer(layerNumber));
		layerNumber++;
		planeDrawn.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.MenuKeyListener#menuKeyPressed(javax.swing.event.
	 * MenuKeyEvent)
	 */
	@Override
	public void menuKeyPressed(MenuKeyEvent e) {
		MenuElement[] path = e.getPath();
		JMenuItem item = (JMenuItem) path[path.length - 1];
		System.out.println("Key released: " + e.getKeyChar() + ", "
				+ KeyEvent.getKeyText(e.getKeyCode()) + " on " + item.getText());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.MenuKeyListener#menuKeyReleased(javax.swing.event.
	 * MenuKeyEvent)
	 */
	@Override
	public void menuKeyReleased(MenuKeyEvent e) {
		MenuElement[] path = e.getPath();
		JMenuItem item = (JMenuItem) path[path.length - 1];
		System.out.println("Key pressed: " + e.getKeyChar() + ", "
				+ KeyEvent.getKeyText(e.getKeyCode()) + " on " + item.getText());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuKeyListener#menuKeyTyped(javax.swing.event.MenuKeyEvent
	 * )
	 */
	@Override
	public void menuKeyTyped(MenuKeyEvent e) {

		MenuElement[] path = e.getPath();
		JMenuItem item = (JMenuItem) path[path.length - 1];
		System.out.println("Key typed: " + e.getKeyChar() + ", "
				+ KeyEvent.getKeyText(e.getKeyCode()) + " on " + item.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {

		this.setJMenuBar(menuBar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
	 * )
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
	 * )
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
