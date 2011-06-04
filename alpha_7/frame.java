import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import java.util.Vector;
import javax.swing.JFrame;
import java.io.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import gameLib.*;

public class frame extends JFrame
{
	DefaultListModel deckList = new DefaultListModel();
	Vector<String> pathList = new Vector<String>();
	String currDir = ".";
	String baseDir = "";
	int index = 0;
	int w = 1400;
	int h = 1000;

	public void loadList(String[]args)
	{
		for(int i=1;i<args.length;i+=2)
		{
			deckList.addElement(args[i]);
			pathList.add(args[i+1]);
			index++;
		}
	}

	public frame(String[]args)
	{
		baseDir = args[0];
		initComponents(args);
	}
	
        private void initComponents(String[]args)
        {
                createButton = new javax.swing.JButton();
                editButton = new javax.swing.JButton();
                playButton = new javax.swing.JButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                JDeckList = new javax.swing.JList(deckList);


                setDefaultCloseOperation(
			javax.swing.WindowConstants.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					public void run() {
						System.exit(0);
					}
				}).start();
			}
		});
                createButton.setText("Create");
                createButton.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                createButtonMouseClicked(evt);
                        }
                });

                editButton.setText("Edit");
                editButton.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                editButtonMouseClicked(evt);
                        }
                });

                playButton.setText("Play!");
                playButton.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                playButtonMouseClicked(evt);
                        }
                });

                JDeckList.setBorder(
			javax.swing.BorderFactory.createTitledBorder(
				null, 
				"Deck List", 
				javax.swing.border.TitledBorder.
					DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.
					DEFAULT_POSITION, new java.awt.Font(
						"Dialog", 1, 24)));
                JDeckList.setFont(new java.awt.Font("SansSerif", 1, 18));
                JDeckList.setSelectionMode(
			javax.swing.ListSelectionModel.SINGLE_SELECTION);
                jScrollPane1.setViewportView(JDeckList);

                org.jdesktop.layout.GroupLayout layout = 
			new org.jdesktop.layout.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(layout.createParallelGroup(
			org.jdesktop.layout.GroupLayout.LEADING).add(
			org.jdesktop.layout.GroupLayout.TRAILING, 
			layout.createSequentialGroup().addContainerGap().add(
			jScrollPane1, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, 
			Short.MAX_VALUE).addPreferredGap(
			org.jdesktop.layout.LayoutStyle.RELATED).add(
			layout.createParallelGroup(
			org.jdesktop.layout.GroupLayout.LEADING, false).add(
			org.jdesktop.layout.GroupLayout.TRAILING, editButton, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 
			Short.MAX_VALUE).add(
			org.jdesktop.layout.GroupLayout.TRAILING, createButton, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 
			Short.MAX_VALUE).add(
			org.jdesktop.layout.GroupLayout.TRAILING, playButton, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 
			Short.MAX_VALUE)).addContainerGap()));
                layout.setVerticalGroup(layout.createParallelGroup(
			org.jdesktop.layout.GroupLayout.LEADING).add(
			layout.createSequentialGroup().addContainerGap().add(
			layout.createParallelGroup(
			org.jdesktop.layout.GroupLayout.LEADING).add(
			org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, 
			org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 543, 
			Short.MAX_VALUE).add(layout.createSequentialGroup().add(
			createButton).addPreferredGap(
			org.jdesktop.layout.LayoutStyle.RELATED).add(
			editButton).addPreferredGap(
			org.jdesktop.layout.LayoutStyle.RELATED).
			addPreferredGap(
			org.jdesktop.layout.LayoutStyle.RELATED, 400, 
			Short.MAX_VALUE).add(playButton))).addContainerGap()));
                pack();
		loadList(args);
        }

	private void playButtonMouseClicked(java.awt.event.MouseEvent evt)
	{
		if(index<=0)
			return;
		String cardList = pathList.get(JDeckList.getSelectedIndex());
		magicPanel panel = new magicPanel();
		magicOptions options = new magicOptions();
		Deck deck = new Deck(20 + 231, h - 340 - 30, baseDir, cardList, 
			"data/deck.png", panel);
		deck.shuffle(8);
		Board board = new Board(0, 0, w, h, deck, options);
		panel.setup(board);
		options.setup(board);
		JFrame f = new JFrame();
		f.add(board);
		f.setSize(w, h);
		cleanup c = new cleanup(panel, options, deck, board);
		f.addWindowListener(new adaptor(c));
		f.setVisible(true);
	}

	private void editButtonMouseClicked(java.awt.event.MouseEvent evt) {}

	private void createButtonMouseClicked(java.awt.event.MouseEvent evt) {}
	
	public static void main(final String args[])
	{
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new frame(args).setVisible(true);
			}
		});
	}

	public class adaptor extends WindowAdapter
	{
		cleanup c;
		public adaptor(cleanup c)
		{
			this.c = c;
		}
		public void windowClosing(WindowEvent e)
		{
			c.start();
		}
	}

	public class cleanup extends Thread
	{
		ConfigPanel panel;
		CardOptions options;
		Deck deck;
		Board board;
		public cleanup(ConfigPanel panel, CardOptions options, 
			Deck deck, Board board)
		{
			this.panel = panel;
			this.options = options;
			this.deck = deck;
			this.board = board;
		}
		public void run()
		{
			panel = null;
			options = null;
			deck = null;
			board = null;
			System.runFinalization();
		}
	}
	
        private javax.swing.JList JDeckList;
        private javax.swing.JButton createButton;
        private javax.swing.JButton editButton;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JButton playButton;
	
}
