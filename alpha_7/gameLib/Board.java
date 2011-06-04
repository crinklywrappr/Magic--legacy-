package gameLib;
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.util.LinkedList;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
public class Board extends JPanel implements MouseListener, MouseMotionListener,
	ComponentListener
{
	public int x, y, w, h;
	public Deck deck;
	public CardOptions options;
	public LinkedList<Card> inPlay = new LinkedList<Card>();
	public LinkedList<Card> selected = new LinkedList<Card>();
	public boolean draggingCard = false;
	public boolean drawingBox = false;
	public Rectangle Box = new Rectangle(0,0,0,0);
	public float origX, origY;
	/**
	 * used to determine which card you right clicked on, when performing
	 * an operation on a selection of multiple cards - useful when stacking
	 **/
	public Card specificCardSelected = null;
	public boolean deckClear = true;

	public Board(int x, int y, int w, int h, Deck deck, CardOptions options)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.deck = deck;
		this.options = options;
		setLayout(null);
		setBounds(x, y, w, h);
		add(deck);
		add(options);
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(drawingBox)
		{
			g.setColor(new Color(32, 32, 200, 15));
			g.fill3DRect(
				(int)Box.getX(),
				(int)Box.getY(),
				(int)Box.getWidth(),
				(int)Box.getHeight(),
				true);
		}
	}

	public Card compToCard(Component c)
	{
		Card card;
		try {
			card = (Card)c;
		} catch (ClassCastException e) {
			card = null;
		}
		return card;
	}

	public void transferSelectedToInPlay()
	{
		for(int i=0;i<selected.size();i++)
			selected.get(i).deselect();
		inPlay.addAll(selected);
		selected.clear();
	}

	public void selectThisCardExclusively(Card card)
	{
		/* make this card the only 
		 * card selected */
		transferSelectedToInPlay();
		inPlay.remove(card);
		selected.add(card);
		card.select();
		/* trick to move the card 
		 * above all the others */
		remove(card);
		add(card, 0);
		card.repaint();
	}

	/**
	 * Implements MouseListener
	 **/
	
	public void mouseClicked(MouseEvent e)
	{
		Point p = e.getPoint();
		boolean clickedOnCard = false;
		Component comps[] = getComponents();
		Card card = null;
		for(Component comp : comps)
		{
			card = null;
			if((card = compToCard(comp))!=null && 
				card.getBounds().contains(p))
			{
				clickedOnCard = true;
				if(e.getButton()==MouseEvent.BUTTON1 || 
					!card.selected)
					selectThisCardExclusively(card);
				else if(e.getButton()==MouseEvent.BUTTON3)
				{
					specificCardSelected = card;
					options.show(this, e.getX(), e.getY());
					break;
				}
				break;
			}
		}
		if(!clickedOnCard)
			transferSelectedToInPlay();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e)
	{
		Point p = e.getPoint();
		origX = (float)p.getX();
		origY = (float)p.getY();
		Component comps[] = getComponents();
		Card card = null;
		for(Component comp : comps)
		{
			if((card = compToCard(comp))!=null && 
				card.getBounds().contains(p))
			{
				draggingCard = true;
				if(!card.selected)
					selectThisCardExclusively(card);
				break;
			}
			card = null;
		}
		if(!draggingCard)
			drawingBox = true;
	}

	public void mouseReleased(MouseEvent e)
	{
		if(draggingCard)
		{
			Card card;
			for(int i=selected.size()-1;i>=0;i--)
			{
				card = selected.get(i);
				card.origX = card.getX();
				card.origY = card.getY();
			}
			draggingCard = false;
		}
		else if(drawingBox)
		{
			transferSelectedToInPlay();
			Component comps[] = getComponents();
			Card card = null;
			for(int i=comps.length-1;i>=0;i--)
			{
				if((card = compToCard(comps[i]))!=null && 
					Box.contains(card.getBounds()))
				{
					selected.add(card);
					card.select();
					remove(card);
					add(card, 0);
				}
				card = null;
			}
			Box.setBounds(0,0,0,0);
			drawingBox = false;
		}
		repaint();
	}

	/**
	 * Implements MouseMotionListener
	 **/

	public void mouseDragged(MouseEvent e)
	{
		Point p = e.getPoint();
		if(draggingCard)
		{
			Card card;
			for(int i=0;i<selected.size();i++)
			{
				card = selected.get(i);
				int x = (int)(p.x - origX + card.origX);
				int y = (int)(p.y - origY + card.origY);
				card.setLocation(x, y);
				card.repaint();
			}
		}
		else if(drawingBox)
		{
			Box.setBounds(
				(int)Math.min(origX, p.getX()),
				(int)Math.min(origY, p.getY()),
				(int)Math.abs(p.getX() - origX),
				(int)Math.abs(p.getY() - origY));
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		if(deck.getBounds().contains(e.getPoint()))
		{
			if(deckClear)
			{
				deck.over = true;
				deck.panel.setVisible(true);
				deckClear = false;
			}
			deck.repaint();
		}
		else if(!deckClear)
		{
			deck.over = false;
			deck.repaint();
			deck.panel.setVisible(false);
			deckClear = true;
		}
	}

	/**
	 * Implements ComponentListener
	 **/
	
	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e)
	{
		deck.setLocation(deck.getX(), getHeight() - 340 - 30);
	}

	public void componentShown(ComponentEvent e) {}
}
