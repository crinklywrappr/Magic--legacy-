import gameLib.*;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
public class magicPanel extends ConfigPanel
{
	JButton searchButton = new JButton("Search Library");
	JButton shuffleButton = new JButton("Shuffle");
	JSpinner shuffleAmtSpinner;
	JButton drawButton = new JButton("Draw");
	JSpinner drawAmtSpinner;
	JButton addTokensButton = new JButton("Add Tokens");
	JSpinner addTokensAmtSpinner;
	JLabel lifeLabel = new JLabel("Life");
	JSpinner lifeSpinner;
	Deck deck;
	public void setup(Board b)
	{
		board = b;
		deck = board.deck;

		SpinnerNumberModel sh = new SpinnerNumberModel(8, 1, 20, 1);
		SpinnerNumberModel dr = new SpinnerNumberModel(7, 1, 60, 1);
		SpinnerNumberModel at = new SpinnerNumberModel();
		at.setValue(new Integer(1));
		at.setMinimum(new Integer(1));
		at.setStepSize(new Integer(1));
		SpinnerNumberModel li = new SpinnerNumberModel();
		li.setValue(new Integer(20));

		shuffleAmtSpinner = new JSpinner(sh);
		drawAmtSpinner = new JSpinner(dr);
		addTokensAmtSpinner = new JSpinner(at);
		lifeSpinner = new JSpinner(li);

		lifeLabel.setFont(new Font(lifeLabel.getFont().getName(), 
			Font.BOLD, 24));

		searchButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		}});
		shuffleButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			deck.acquireLock();
			deck.shuffle(((Integer)
				shuffleAmtSpinner.getValue()).intValue());
			board.deck.releaseLock();
		}});
		drawButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			deck.acquireLock();
			LinkedList<Card> drawn = deck.draw(((Integer)
				drawAmtSpinner.getValue()).intValue());
			deck.releaseLock();
			board.transferSelectedToInPlay();
			Card card = null;
			for(int i=0;i<drawn.size();i++)
			{
				card = drawn.get(i);
				card.flipUp();
				card.untap();
				card.select();
				board.add(card, 0);
				board.selected.add(card);
				card.setLocation(0, 0);
				card.origX = 0;
				card.origY = 0;
				card.setVisible(true);
			}
			board.repaint();
		}});
		addTokensButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int n = ((Integer)addTokensAmtSpinner.getValue()).
				intValue();
			board.transferSelectedToInPlay();
			Card card = null;
			for(int i=0;i<n;i++)
			{
				card = new Card(0, 0, CardBack.back);
				card.token = true;
				card.flipUp();
				board.add(card, 0);
				board.selected.add(card);
				card.setVisible(true);
			}
			board.repaint();
		}});

		GroupLayout layout = new GroupLayout(deck);
		deck.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup hGroup = 
			layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = 
			layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING,
			false).
			addGroup(layout.createSequentialGroup().
				addGap(26).
				addComponent(searchButton, 184, 184, 184)).
			addGroup(layout.createSequentialGroup().
				addGap(26).
				addComponent(shuffleButton).
				addComponent(shuffleAmtSpinner)).
			addGroup(layout.createSequentialGroup().
				addGap(26).
				addComponent(drawButton).
				addComponent(drawAmtSpinner)).
			addGroup(layout.createSequentialGroup().
				addGap(26).
				addComponent(addTokensButton).
				addComponent(addTokensAmtSpinner)).
			addGroup(layout.createSequentialGroup().
				addGap(26).
				addComponent(lifeLabel).
				addComponent(lifeSpinner)));

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
			addComponent(searchButton));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
			addComponent(shuffleButton).
			addComponent(shuffleAmtSpinner));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
			addComponent(drawButton).
			addComponent(drawAmtSpinner));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
			addComponent(addTokensButton).
			addComponent(addTokensAmtSpinner));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
			addComponent(lifeLabel).
			addComponent(lifeSpinner));

		layout.linkSize(shuffleButton, drawButton, addTokensButton, 
			lifeLabel);

		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);

		setVisible(false);
	}

	public void setVisible(boolean b)
	{
		searchButton.setVisible(b);
		shuffleButton.setVisible(b);
		shuffleAmtSpinner.setVisible(b);
		drawButton.setVisible(b);
		drawAmtSpinner.setVisible(b);
		addTokensButton.setVisible(b);
		addTokensAmtSpinner.setVisible(b);
		lifeLabel.setVisible(b);
		lifeSpinner.setVisible(b);
	}
}
