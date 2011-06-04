package gameLib;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.concurrent.Semaphore;
public class Deck extends JComponent
{
	public int x, y, w, h;
	public String file;
	public BufferedImage image;
	public ConfigPanel panel;
	public LinkedList<Card> library = new LinkedList<Card>();
	public LinkedList<CardImage> images = new LinkedList<CardImage>();
	public boolean over = false;
	public Semaphore sem = new Semaphore(1);
	public Deck(int x, int y, String baseDir, String file, String image, 
		    ConfigPanel panel)
	{
		this.x = x;
		this.y = y;
		setLayout(null);
		try {
			Scanner s = new Scanner(new BufferedReader(
				new FileReader(file)));
			int num;
			String name;
			String picLoc;
			while(s.hasNextLine())
			{
				Scanner l = new Scanner(s.nextLine());
				l.useDelimiter("\t+");
				num = l.nextInt();
				name = l.next();
				picLoc = baseDir + "/" + l.next();
				images.add(new CardImage(name, picLoc));
				System.out.printf("%d\t%s\t%s\n", num, name, 
					picLoc);
				for(int i=0;i<num;i++)
					library.add(new Card(0,0, 
						images.getLast()));
				l.close();
			}
		}catch(IOException e) {
			System.err.println("File not properly formatted.");
			System.exit(0);
		}
		try {
			this.image = ImageIO.read(new File(image));
		}catch(IOException e) {
			System.err.println("unable to find deck image\n");
			System.exit(0);
		}
		w = this.image.getWidth();
		h = this.image.getHeight();
		setBounds(x, y, w, h);
		this.panel = panel;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
		if(over)
		{
			Color c = Color.GRAY;
			g.setColor(new Color(c.getRed(), c.getGreen(), 
				c.getBlue(), 100));
			g.fillRoundRect(26, 23, 184, 265, 7, 12);
		}
	}

	public void shuffle()
	{
		Card tmp;
		int n;
		Random rand = new Random();
		for(int i=0;i<library.size();i++)
		{
			n = rand.nextInt(library.size());
			tmp = library.get(n);
			library.set(n, library.get(i));
			library.set(i, tmp);
		}
	}

	public void shuffle(int times)
	{
		for(int i=0;i<times;i++)
			shuffle();
	}

	public LinkedList<Card> draw(int amt)
	{
		LinkedList<Card> drawn = new LinkedList<Card>();
		for(int i=0;i<amt;i++)
			drawn.add(library.remove());
		return drawn;
	}

	public void placeOnTop(Card card)
	{
		library.addFirst(card);
	}

	public void placeOnBottom(Card card)
	{
		library.addLast(card);
	}

	public void acquireLock()
	{
		try {
			sem.acquire();
		} catch(InterruptedException e) {}
	}

	public void releaseLock()
	{
		sem.release();
	}
}
