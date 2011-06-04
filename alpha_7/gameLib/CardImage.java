package gameLib;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Rectangle;
public class CardImage
{
	String name, path;
	BufferedImage image;
	BufferedImage selectedImage;
	BufferedImage tapped;
	BufferedImage selectedTapped;
	public CardImage(String name, String path)
	{
		this.name = name;
		this.path = path;
		try {
			setupImages();
		} catch(IOException e) {
			System.err.printf("invalid filename: %s\n", path);
			System.exit(0);
		}
	}

	public void setupImages()throws IOException
	{
		BufferedImage insideImg = ImageIO.read(new File(path));
		scaleImage(insideImg, 177, 150);
		image = combine(CardBorder.normal(), insideImg);
		selectedImage = combine(CardBorder.selected(), insideImg);
		int type = BufferedImage.TYPE_INT_ARGB_PRE;
		tapped = new BufferedImage(image.getHeight(), image.getWidth(), 
			type);
		selectedTapped = new BufferedImage(image.getHeight(), 
			image.getWidth(), type);
		AffineTransform at = AffineTransform.getRotateInstance(
			Math.toRadians(-90), image.getWidth() / 2, 
			image.getHeight() / 2.75);
		Graphics2D g = tapped.createGraphics();
		g.drawImage(image, at, null);
		g = selectedTapped.createGraphics();
		g.drawImage(selectedImage, at, null);
		g.dispose();
	}

	public void scaleImage(BufferedImage image, int w, int h)
	{
		double sx = w / image.getWidth();
		double sy = h / image.getHeight();
		AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
		Graphics2D g = image.createGraphics();
		g.drawImage(image, at, null);
		g.clip(new Rectangle(0, 0, w, h));
		g.dispose();
	}

	public BufferedImage combine(BufferedImage border, BufferedImage inside)
	{
		BufferedImage combined = new BufferedImage(
			border.getWidth(), border.getHeight(), 
			BufferedImage.TYPE_INT_ARGB_PRE);
		Rectangle r = new Rectangle(18, 15, 195, 165);
		Graphics2D g = combined.createGraphics();
		g.drawImage(border, 0, 0, null);
		g.drawImage(inside, r.x, r.y, null);
		g.dispose();
		return combined;
	}

	public void scaleAllImages(AffineTransform at, int w, int h)
	{
		AffineTransform at2 = AffineTransform.getScaleInstance(1, 1);
		int type = BufferedImage.TYPE_INT_ARGB_PRE;
		BufferedImage tmp;
		Graphics2D g;

		tmp = new BufferedImage(w, h, type);
		g = tmp.createGraphics();
		g.drawImage(image, at, null);
		image = new BufferedImage(w, h, type);
		g = image.createGraphics();
		g.drawImage(tmp, at2, null);

		tmp = new BufferedImage(w, h, type);
		g = tmp.createGraphics();
		g.drawImage(selectedImage, at, null);
		selectedImage = new BufferedImage(w, h, type);
		g = selectedImage.createGraphics();
		g.drawImage(tmp, at2, null);

		tmp = new BufferedImage(h, w, type);
		g = tmp.createGraphics();
		g.drawImage(tapped, at, null);
		tapped = new BufferedImage(h, w, type);
		g = tapped.createGraphics();
		g.drawImage(tmp, at2, null);

		tmp = new BufferedImage(h, w, type);
		g = tmp.createGraphics();
		g.drawImage(selectedTapped, at, null);
		selectedTapped = new BufferedImage(h, w, type);
		g = selectedTapped.createGraphics();
		g.drawImage(tmp, at2, null);

		g.dispose();
	}
}
