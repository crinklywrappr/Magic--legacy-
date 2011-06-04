import gameLib.*;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
public class magicOptions extends CardOptions
{
	public void setup(Board b)
	{
		board = b;
		JMenuItem tap = new JMenuItem("Tap");
		JMenuItem untap = new JMenuItem("Untap");
		JMenuItem flipUp = new JMenuItem("Flip up");
		JMenuItem flipDown = new JMenuItem("Flip down");
		JMenuItem putTop = new JMenuItem("Put on top of library");
		JMenuItem putBottom = new JMenuItem("Put on bottom of library");
		JMenuItem stack = new JMenuItem("Stack these together here");
		JMenuItem destroy = new JMenuItem("Destroy token");
		JMenuItem shuffle = new JMenuItem("Shuffle selection");
		tap.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<board.selected.size();i++)
				board.selected.get(i).tap();
		}});
		untap.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<board.selected.size();i++)
				board.selected.get(i).untap();
		}});
		flipUp.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<board.selected.size();i++)
				board.selected.get(i).flipUp();
		}});
		flipDown.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<board.selected.size();i++)
				board.selected.get(i).flipDown();
		}});
		putTop.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Deck deck = board.deck;
			deck.acquireLock();
			Card card = null;
			for(int i=0;i<board.selected.size();i++)
			{
				if(!board.selected.get(i).token)
				{
					card = board.selected.remove(i);
					card.deselect();
					card.flipDown();
					board.remove(card);
					card.setVisible(false);
					deck.placeOnTop(card);
					i--;
				}
			}
			deck.releaseLock();
			board.repaint();
		}});
		putBottom.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Deck deck = board.deck;
			deck.acquireLock();
			Card card = null;
			for(int i=0;i<board.selected.size();i++)
			{
				if(!board.selected.get(i).token)
				{
					card = board.selected.remove(i);
					card.deselect();
					card.flipDown();
					board.remove(card);
					card.setVisible(false);
					deck.placeOnBottom(card);
					i--;
				}
			}
			deck.releaseLock();
			board.repaint();
		}});
		stack.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Card sel = board.specificCardSelected;
			for(int i=0;i<board.selected.size();i++)
			{
				Card card = board.selected.get(i);
				card.setLocation(sel.getX(), sel.getY());
				card.origX = sel.getX();
				card.origY = sel.getY();
			}
		}});
		destroy.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<board.selected.size();i++)
			{
				if(board.selected.get(i).token)
				{
					board.remove(board.selected.get(i));
					board.selected.remove(i);
					i--;
				}
			}
			board.validate();
			board.repaint();
		}});
		shuffle.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int s = board.selected.size();
			Card tmpCard;
			int tmpNum;
			Random rand = new Random();
			for(int i=0;i<board.selected.size();i++)
			{
				tmpCard = board.selected.get(i);
				tmpNum = rand.nextInt(100);
				if(tmpNum<42)
					tmpNum = 0;
				else
					tmpNum = rand.nextInt(s);
				board.remove(tmpCard);
				board.add(tmpCard, tmpNum);
			}
			board.repaint();
		}});
		add(tap);
		add(untap);
		add(flipUp);
		add(flipDown);
		add(putTop);
		add(putBottom);
		add(stack);
		add(destroy);
		add(shuffle);
	}
}
