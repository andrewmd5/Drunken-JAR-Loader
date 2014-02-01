package loader;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
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
	// to
	// splash
	// image
	private final static String FRAME_IMAGE = "http://i.imgur.com/UW5ZfQ5.jpg"; // icon
																				// for
																				// the
																				// frame
	private final static String JAR_URL = "http://codeusa.net/play/client.jar"; // link
	// to
	// jar
	final static Loader loader = new Loader();
	private final static String LOADER_IMAGE = "http://codeusa.net/images/logo.png"; // link
	private final static Logger logger = Logger.getLogger(Loader.class
			.getName());
	private static String MAIN_CLASS = "RunClient"; // put your jars main class
													// here
	private static JProgressBar progressBar;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1162207878136245145L;
	private static int size; // size of download in bytes
	private static JDialog splash;

	public static float getProgress() {
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
		if (connection.getResponseCode() / 100 != 2) {
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
		loader.loadClient(client);
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

	public static void openURL(final String url) { // Because the default Java
													// url opener sucks
		final String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Windows")) {
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url);
			} else {
				final String[] browsers = { "firefox", "opera", "konqueror",
						"epiphany", "mozilla", "netscape", "chrome", "safari" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++) {
					if (Runtime.getRuntime()
							.exec(new String[] { "which", browsers[count] })
							.waitFor() == 0) {
						browser = browsers[count];
					}
				}
				Runtime.getRuntime().exec(new String[] { browser, url });
			}
		} catch (final Exception e) {
			logger.info("Could not open browser");
			logger.severe("Encounter error: " + e.getClass());
			logger.severe("Error message: " + e.getMessage());
			logger.severe("Error cause: " + e.getCause());
		}
	}

	public static void setFrameTheme() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException

	{
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
				new URL(LOADER_IMAGE)));
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

	private JButton Button1;

	private JButton Button2;

	private JButton Button3;
	private JButton Button6;

	private JFrame clientFrame;

	private final JPanel clientPanel = new JPanel();
	private LayoutManager Layout;

	private int screenshot;
	private boolean takeScreenshot = true;

	public JPanel totalPanel;

	@Override
	public void actionPerformed(final ActionEvent actionevent) {
		final String s = actionevent.getActionCommand();

		if (s.equals("Vote")) {
			openURL("http://codeusa.net/vote/");
		} else if (s.equals("Hiscores")) {
			openURL("http://codeusa.net/hiscores");
		} else if (s.equals("Forum")) {
			openURL("http://codeusa.net/forums");
		} else

		if (s.equals("Store")) {
			openURL("http://codeusa.net/store");
		} else if (s.equals("Screenshot")) {
			takeScreenShot();
		}
	}

	private void loadClient(final Applet client) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException,
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
		Button1 = new JButton("Screenshot");
		Button2 = new JButton("Vote");
		Button3 = new JButton("Forum");
		Button6 = new JButton("Upgrades");
		jmenubar.setLayout(Layout);
		jmenubar.add(Button2);
		jmenubar.add(Button3);
		jmenubar.add(Button6);
		jmenubar.add(Button1);
		Button2.addActionListener(this);
		Button3.addActionListener(this);
		Button6.addActionListener(this);
		Button1.addActionListener(this);
		Button2.setBackground(Color.BLACK);
		Button2.setForeground(Color.ORANGE);
		Button2.setText("Forum");
		Button3.setBackground(Color.BLACK);
		Button3.setForeground(Color.ORANGE);
		Button3.setText("Vote");
		Button6.setBackground(Color.BLACK);
		Button6.setForeground(Color.ORANGE);
		Button6.setText("Upgrades");
		Button1.setBackground(Color.BLACK);
		Button1.setForeground(Color.ORANGE);
		Button1.setText("Screenshot");
		setFrameTheme();
	}

	public void takeScreenShot() {
		try {
			final Window window = KeyboardFocusManager
					.getCurrentKeyboardFocusManager().getFocusedWindow();
			final Point point = window.getLocationOnScreen();
			final int x = (int) point.getX();
			final int y = (int) point.getY();
			final int w = window.getWidth();
			final int h = window.getHeight();
			final Robot robot = new Robot(window.getGraphicsConfiguration()
					.getDevice());
			final Rectangle captureSize = new Rectangle(x, y, w, h);
			final java.awt.image.BufferedImage bufferedimage = robot
					.createScreenCapture(captureSize);
			final String fileExtension = "CodeUSA";
			for (int i = 1; i <= 1000; i++) {
				final File file = new File("Screenshots/" + fileExtension + " "
						+ i + ".png");
				if (!file.exists()) {
					screenshot = i;
					takeScreenshot = true;
					break;
				}
			}
			final File file2 = new File("./ScreenShots/");
			final File file = new File((new StringBuilder())
					.append("./Screenshots/" + fileExtension + " ")
					.append(screenshot).append(".png").toString());
			if (!file2.exists()) {
				file2.mkdir();
			}
			if (takeScreenshot == true) {
				ImageIO.write(bufferedimage, "png", file);
			} else {
				logger.info("Unable to write file");
			}
		} catch (final Exception e) {
			logger.severe("Encounter error: " + e.getClass());
			logger.severe("Error message: " + e.getMessage());
			logger.severe("Error cause: " + e.getCause());
		}
	}

}