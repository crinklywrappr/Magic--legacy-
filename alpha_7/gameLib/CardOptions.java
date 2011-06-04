package gameLib;
import javax.swing.JPopupMenu;
public abstract class CardOptions extends JPopupMenu
{
	public Board board;
	public abstract void setup(Board b);
}
