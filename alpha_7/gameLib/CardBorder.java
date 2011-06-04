package gameLib;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class CardBorder
{
	public String nPath, sPath;
	public BufferedImage normal, selected;

	public static CardBorder border = new CardBorder("data/border.png", 
		"data/border_highlight.png");

	private CardBorder(String normal, String selected)
	{
		nPath = normal;
		try {
			this.normal = ImageIO.read(new File(normal));
		} catch (IOException e) {
			System.err.println("unable to find the border image");
			System.exit(0);
		}
		sPath = selected;
		try {
			this.selected = ImageIO.read(new File(selected));
		} catch (IOException e) {
			System.err.println("unable to find the selected " + 
				"border image");
			System.exit(0);
		}
	}

	public static void setNormal(String normal)
	{
		border.nPath = normal;
		try {
			border.normal = ImageIO.read(new File(normal));
		} catch (IOException e) {
			System.err.println("unable to find the border image");
		}
	}
	
	public static void setSelected(String selected)
	{
		border.sPath = selected;
		try {
			border.selected = ImageIO.read(new File(selected));
		} catch (IOException e) {
			System.err.println("unable to find the selected " + 
				"border image");
		}
	}

	public static BufferedImage normal()
	{
		return border.normal;
	}

	public static BufferedImage selected()
	{
		return border.selected;
	}
}
