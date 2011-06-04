package gameLib;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
public class Card extends JComponent
{
	public CardImage images;
	public BufferedImage draw;
	public int origX, origY, w, h;
	public boolean selected = true;
	public boolean up = false;
	public boolean untapped = true;
	public boolean token = false;
	public Card(int x, int y, CardImage images)
	{
		origX = x;
		origY = y;
		this.images = images;
		draw = CardBack.selectedImage();
		w = draw.getWidth();
		h = draw.getHeight();
		setBounds(origX, origY, w, h);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(draw, 0, 0, this);
	}

	public void resetDraw()
	{
		if(selected)
		{
			if(untapped && up)
				draw = images.selectedImage;
			else if(!untapped && up)
				draw = images.selectedTapped;
			else if(untapped && !up)
				draw = CardBack.selectedImage();
			else if(!untapped && !up)
				draw = CardBack.selectedTapped();
		}
		else
		{
			if(untapped && up)
				draw = images.image;
			else if(!untapped && up)
				draw = images.tapped;
			else if(untapped && !up)
				draw = CardBack.image();
			else if(!untapped && !up)
				draw = CardBack.tapped();
		}
		if(!untapped)
			setBounds(origX, origY, h, w);
		else
			setBounds(origX, origY, w, h);
		repaint();
	}

	public void selectOrDeselect()
	{
		selected = !selected;
		resetDraw();
	}

	public void tapOrUntap()
	{
		untapped = !untapped;
		resetDraw();
	}

	public void flip()
	{
		up = !up;
		resetDraw();
	}

	public void flipUp()
	{
		up = true;
		resetDraw();
	}

	public void flipDown()
	{
		up = false;
		resetDraw();
	}

	public void select()
	{
		selected = true;
		resetDraw();
	}

	public void deselect()
	{
		selected = false;
		resetDraw();
	}

	public void tap()
	{
		untapped = false;
		resetDraw();
	}

	public void untap()
	{
		untapped = true;
		resetDraw();
	}
}
