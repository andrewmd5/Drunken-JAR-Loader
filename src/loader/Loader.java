package loader;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Loader extends JWindow implements ActionListener {
	private static URLClassLoader classLoader;
	private static int downloaded; // number of bytes downloaded

	private final static String FRAME_IMAGE = "http://i.imgur.com/UW5ZfQ5.jpg";

	private final static String JAR_URL = "http://codeusa.net/play/client.jar";

	final static Loader loader = new Loader();

	private final static Logger logger = Logger.getLogger(Loader.class
			.getName());

	private static String MAIN_CLASS = "RunClient";

	private static JProgressBar progressBar;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1162207878136245145L;

	private static int size; // size of download in bytes

	private static JDialog splash;

	private final static String SPLASH_IMAGE = "http://i.imgur.com/wPH1YJb.png"; // link

	private static float getProgress() {
		return ((float) downloaded / size) * 100;
	}

	private static void grabJAR() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException,
			UnsupportedLookAndFeelException {
		final URL url = new URL(Loader.JAR_URL);
		final InputStream is = url.openStream();
		final byte[] b = new byte[2048];
		int length;
		final HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();

		// Specify what portion of file to download.
		connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

		// Connect to server.
		connection.connect();

		// Make sure response code is in the 200 range.
		if ((connection.getResponseCode() / 100) != 2) {
			logger.info("Unable to find file");
			return;
		}

		// set content length.
		size = connection.getContentLength();
		while ((length = is.read(b)) != -1) {
			downloaded += length;
			progressBar.setValue((int) getProgress()); // set progress bar
		}
		is.close();
		classLoader = new URLClassLoader(new URL[] { (url) });
		final Applet client = (Applet) classLoader.loadClass(MAIN_CLASS)
				.newInstance();
		client.init();
		client.start();
		loader.openJARFrame(client);
		setFrameTheme();

	}

	protected static void hideSplashScreen() {
		splash.setVisible(false);
		splash.dispose();
	}

	public static void main(final String... args)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IOException,
			UnsupportedLookAndFeelException {
		try {
			showSplashScreen();
		} catch (final MalformedURLException e) {
			logger.severe("Encounter error: " + e.getClass());
			logger.severe("Error message: " + e.getMessage());
			logger.severe("Error cause: " + e.getCause());
		}
		final SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

			@Override
			protected Void doInBackground() throws Exception {
				grabJAR();
				return null;
			}

			@Override
			protected void done() {

				hideSplashScreen();
			}

		};
		worker.execute();

	}

	private static void setFrameTheme() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());

		}
	}

	protected static void showSplashScreen() throws MalformedURLException {
		splash = new JDialog((Frame) null);
		splash.setModal(false);
		splash.setAlwaysOnTop(true);
		splash.setUndecorated(true);
		final JLabel background = new JLabel(new ImageIcon(
				new URL(SPLASH_IMAGE)));
		background.setOpaque(true);
		background.setLayout(new BorderLayout());
		splash.getContentPane().add(background);
		final JLabel text = new JLabel("Loading, please wait...");
		text.setFont(new Font("Consolas", Font.BOLD | Font.ITALIC, 15));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setForeground(Color.WHITE);
		text.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
		background.add(text);
		progressBar = new JProgressBar();
		background.add(progressBar, BorderLayout.SOUTH);
		splash.pack();
		splash.setLocationRelativeTo(null);
		splash.setVisible(true);
	}

	private JFrame clientFrame;

	private final JPanel clientPanel = new JPanel();

	private LayoutManager Layout;

	private final JButton onlineStoreButton = new JButton("Upgrades");
	private final JButton screenShotButton = new JButton("Screenshot");

	public JPanel totalPanel;

	@Override
	public void actionPerformed(final ActionEvent actionevent) {
		final String button = actionevent.getActionCommand();
		switch (button) {
		case "Upgrades":
			LoaderUtils.openURL("http://codeusa.net/store");
			break;
		case "Screenshot":
			LoaderUtils.takeScreenShot();
			break;
		default:
			logger.info("This isn't a button I know of " + button);
			break;

		}

	}

	private void openJARFrame(final Applet client)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException,
			UnsupportedLookAndFeelException {

		clientFrame = new JFrame("CodeUSA Loader");

		clientFrame
				.setIconImage(new ImageIcon(new URL(FRAME_IMAGE)).getImage());
		clientFrame.getContentPane().setLayout(new BorderLayout());
		clientPanel.setLayout(new BorderLayout());
		clientPanel.add(client);
		clientPanel.setPreferredSize(new Dimension(765, 503));
		clientFrame.getContentPane().add(clientPanel, "Center");
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.pack();
		clientFrame.setVisible(true);
		setVisible(false);
		final JMenuBar jmenubar = new JMenuBar();
		clientFrame.setJMenuBar(jmenubar);
		Layout = new FlowLayout();
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		jmenubar.setLayout(Layout);
		jmenubar.add(onlineStoreButton);
		jmenubar.add(screenShotButton);
		onlineStoreButton.addActionListener(this);
		screenShotButton.addActionListener(this);
		onlineStoreButton.setText("Upgrades");
		screenShotButton.setText("Screenshot");
		setFrameTheme();
		clientFrame.setLocationRelativeTo(null);
	}

}