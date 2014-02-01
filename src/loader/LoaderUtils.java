package loader;

import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class LoaderUtils extends Loader {
	private final static Logger logger = Logger.getLogger(LoaderUtils.class
			.getName());
	private static int screenshot;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2477796722768143510L;
	private static boolean takeScreenshot = true;

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
				for (int count = 0; (count < browsers.length)
						&& (browser == null); count++) {
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

	public static void takeScreenShot() {
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
				final File screenShots = new File("Screenshots/"
						+ fileExtension + " " + i + ".png");
				if (!screenShots.exists()) {
					screenshot = i;
					takeScreenshot = true;
					break;
				}
			}
			final File directory = new File("./ScreenShots/");
			final File screenShot = new File((new StringBuilder())
					.append("./Screenshots/" + fileExtension + " ")
					.append(screenshot).append(".png").toString());
			if (!directory.exists()) {
				directory.mkdir();
			}
			if (takeScreenshot == true) {
				ImageIO.write(bufferedimage, "png", screenShot);
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
