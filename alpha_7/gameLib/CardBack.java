package gameLib;
import java.awt.image.BufferedImage;
import java.io.IOException;
public class CardBack extends CardImage
{
	public static CardBack back = new CardBack("back", "data/card.jpg");

	private CardBack(String name, String path)
	{
		super(name, path);
	}

	public static void setImage(String path)
	{
		back.path = path;
		try {
			back.setupImages();
		} catch(IOException e) {
			System.err.println("bad filename: " + path);
			System.exit(0);
		}
	}

	public static String path()
	{
		return back.path;
	}

	public static BufferedImage image()
	{
		return back.image;
	}

	public static BufferedImage selectedImage()
	{
		return back.selectedImage;
	}

	public static BufferedImage tapped()
	{
		return back.tapped;
	}

	public static BufferedImage selectedTapped()
	{
		return back.selectedTapped;
	}
}
