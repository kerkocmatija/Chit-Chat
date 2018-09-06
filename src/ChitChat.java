import java.io.IOException;

// GLAVNI PROGRAM S POMOÈJO KATEREGA POŽENEMO VSE SKUPAJ.

public class ChitChat {

	public static void main(String[] args) throws IOException {
		ChatFrame gui = new ChatFrame();
		gui.pack();
		gui.setVisible(true);
	}

}
