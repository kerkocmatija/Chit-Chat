import java.io.IOException;

// GLAVNI PROGRAM S POMO�JO KATEREGA PO�ENEMO VSE SKUPAJ.

public class ChitChat {

	public static void main(String[] args) throws IOException {
		ChatFrame gui = new ChatFrame();
		gui.pack();
		//gui.setPreferredSize(new Dimension(600, 600));
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
	}

}
