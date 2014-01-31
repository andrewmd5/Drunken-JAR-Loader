package loader;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Loader extends JWindow implements ActionListener {
	private static URLClassLoader classLoader;
	private final static String JAR_URL = "http://codeusa.net/play/client.jar";
	private final static String LOADER_IMAGE = "http://codeusa.net/images/logo.png";
	private final static Logger logger = Logger.getLogger(Loader.class
			.getName());
	private static String MAIN_CLASS = "RunClient";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1162207878136245145L;

	public static String createString(final Random random, final String s,
			final int i) {
		final char ac[] = new char[i];
		for (int j = 0; j < i; j++) {
			ac[j] = s.charAt(random.nextInt(s.length()));
		}

		return new String(ac);
	}

	public static void main(final String... args)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IOException,
			UnsupportedLookAndFeelException {
		final Loader loader = new Loader();
		if (!new File(loader.IMAGE_LOCATION).exists()) {
			loader.saveImage(Loader.LOADER_IMAGE, loader.IMAGE_LOCATION);
		}
		loader.showSplash(false);
		logger.info("Starting to download the client from " + Loader.JAR_URL
				+ ".");
		final URL url = new URL(Loader.JAR_URL);
		classLoader = new URLClassLoader(new URL[] { (url) });
		final Applet client = (Applet) classLoader.loadClass(MAIN_CLASS)
				.newInstance();
		client.init();
		client.start();
		loader.loadClient(client);
		setFrameTheme();

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

	private JButton Button1;

	private JButton Button2;

	private JButton Button3;
	private JButton Button6;

	private JFrame clientFrame;
	private final JPanel clientPanel = new JPanel();

	private final String IMAGE_LOCATION = "" + System.getProperty("user.home")
			+ "/codeusasplash.png";

	private LayoutManager Layout;
	private int screenshot;

	private final int splashDuration = 5000;

	private boolean takeScreenshot = true;
	public JPanel totalPanel;

	public Loader() {

	}

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
		clientFrame.setLayout(new BorderLayout());
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

	/**
	 * Saves an image from a remote URL to the destination you specify.
	 * 
	 * @param imageUrl
	 * @param destinationFile
	 * @throws IOException
	 */
	private void saveImage(final String imageUrl, final String destinationFile)
			throws IOException {
		logger.info("Downloading image from " + imageUrl + " and saving to "
				+ destinationFile);
		final URL url = new URL(imageUrl);
		final InputStream is = url.openStream();
		final OutputStream os = new FileOutputStream(destinationFile);
		final byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
	}

	public void showSplash(final boolean hideAfterDuration) {
		final JPanel content = (JPanel) getContentPane();
		final Image img = new ImageIcon(IMAGE_LOCATION).getImage();
		logger.info("Showing splash from " + IMAGE_LOCATION);
		final int width = img.getWidth(null);
		final int height = img.getHeight(null);
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (screen.width - width) / 2;
		final int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);
		final JLabel label = new JLabel(new ImageIcon(IMAGE_LOCATION));
		content.setOpaque(false);
		label.setOpaque(false);
		content.add(label, BorderLayout.CENTER);
		setVisible(true);
		if (hideAfterDuration) {
			try {
				Thread.sleep(splashDuration);
			} catch (final Exception e) {
				logger.severe("Encounter error: " + e.getClass());
				logger.severe("Error message: " + e.getMessage());
				logger.severe("Error cause: " + e.getCause());
			}
			setVisible(false);
		}
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