import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ChatFrame extends JFrame implements ActionListener, KeyListener, WindowListener {

	private static final long serialVersionUID = -7574172929809672200L;

	JFrame frame = new JFrame("ChitChat");
	private JTextArea output;
	private JTextField input;
	private JTextField inputKomu;
	private JTextArea oknoAktivni;
	protected JTextField vzdevek;
	private JPanel vzdevekpanel;
	private JLabel vzdeveknapis;
	private JScrollPane sp;
	private JButton prijavaGumb;
	private JButton odjavaGumb;
	private JButton aktivniGumb;
	private IzpisovalniRobot robot;

	private String user;


	public ChatFrame() {
		super();
		this.setTitle("ChitChat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());

		this.vzdevekpanel = new JPanel();
		FlowLayout vzdeveklayout = new FlowLayout(FlowLayout.LEFT);
		vzdevekpanel.setLayout(vzdeveklayout);
		GridBagConstraints vzdevekConstraint = new GridBagConstraints();
		vzdevekConstraint.gridx = 0;
		vzdevekConstraint.gridy = 0;
		vzdevekConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(vzdevekpanel, vzdevekConstraint);

		this.vzdeveknapis = new JLabel();
		vzdeveknapis.setText("Vzdevek:");
		vzdevekpanel.add(vzdeveknapis);

		this.vzdevek = new JTextField(10);
		this.vzdevek.setText(System.getProperty("user.name"));
		vzdevekpanel.add(vzdevek);

		this.prijavaGumb = new JButton("Prijava");
		prijavaGumb.addActionListener(this);
		vzdevekpanel.add(prijavaGumb);

		this.odjavaGumb = new JButton("Odjava");
		odjavaGumb.addActionListener(this);
		vzdevekpanel.add(odjavaGumb);

		this.aktivniGumb = new JButton("Aktivni");
		aktivniGumb.addActionListener(this);
		vzdevekpanel.add(aktivniGumb);
		GridBagConstraints aktivniGumbConstraint = new GridBagConstraints();
		aktivniGumbConstraint.gridx = 1;
		aktivniGumbConstraint.gridy = 1;

		// Ustvarimo okno za izpisovanje sporoèil.
		this.output = new JTextArea(20, 40);
		this.sp = new JScrollPane(output);
		this.output.setEditable(false);
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 1;
		outputConstraint.fill = 1;
		outputConstraint.weightx = 1;
		outputConstraint.weighty = 1;
		pane.add(sp, outputConstraint);

		// Ustvarimo prostor za seznam aktivnih uporabnikov.
		this.oknoAktivni = new JTextArea(20, 10);
		oknoAktivni.setEditable(false);
		oknoAktivni.setBorder(BorderFactory.createLineBorder(Color.green.darker(), 2));
		GridBagConstraints oknoAktivniConstraint = new GridBagConstraints();
		oknoAktivniConstraint.gridx = 1;
		oknoAktivniConstraint.gridx = 1;
		oknoAktivniConstraint.gridy = 1;
		oknoAktivniConstraint.fill = 1;
		oknoAktivniConstraint.weightx = 1;
		oknoAktivniConstraint.weighty = 1;
		pane.add(oknoAktivni, oknoAktivniConstraint);

		// Ustvarimo vpisovalno polje.
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.fill = GridBagConstraints.HORIZONTAL;
		inputConstraint.weightx = 1;
		inputConstraint.weighty = 0;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				input.requestFocus();
			}
		});
		input.setEnabled(false);

		// Ustvarimo polje za vpis prejemnika našega sporoèila.
		this.inputKomu = new JTextField(10);
		GridBagConstraints inputConstraint2 = new GridBagConstraints();
		inputConstraint2.gridx = 1;
		inputConstraint2.gridy = 2;
		inputConstraint2.fill = 1;
		inputConstraint2.weightx = 1;
		inputConstraint2.weighty = 1;
		pane.add(inputKomu, inputConstraint2);

		// Naredimo robota za izpisovanje
		this.robot = new IzpisovalniRobot(this);

		vzdevek.setText(System.getProperty("user.name"));
		addWindowListener(this);
	}

	/**
	 * @param person
	 *            - the person sending the message
	 * @param message
	 *            - the message content
	 */
	
	
	
	// Preverimo ali je uporabnik prijavljen.
	private boolean prijavljen(String uporabnik) throws JsonParseException, JsonMappingException, IOException {
		List<Uporabnik> uporabniki = ExtendedKlient.seznam();
		for (Uporabnik user : uporabniki) {
			if (user.getUsername().equals(uporabnik)) {
				return true;
			} 
		}
		return false;
	}
	
	// Objava sporoèila.
	public void addMessage(String person, String message) {
		String chat = output.getText();
		String prejemnik = inputKomu.getText();
		if (prejemnik.isEmpty()) {
			output.setText(chat + person + ": " + message + "\n");
		} else {
			output.setText(chat + person + "->" + prejemnik + ": " + message + "\n");
		}
	}
	
	// Izpis prejetih sporoèil.
	public void dodajPrejeta() throws ClientProtocolException, URISyntaxException, IOException {
		try {	
			String vzdevek1 = vzdevek.getText();
			if (prijavljen(vzdevek1)) {
				List<Sporocilo> vsaSporocila = ExtendedKlient.prejmi(vzdevek1);
				if (! vsaSporocila.isEmpty()) {
					String chat = output.getText();
					for (Sporocilo sporocilo : vsaSporocila) {
						if (sporocilo.isGlobal()) {
							output.setText(chat + sporocilo.getSender() + ": " + sporocilo.getText() + "\n");
						} else {
							output.setText(chat + sporocilo.getSender() + "->" + sporocilo.getRecipient() + ": " 
																	+ sporocilo.getText() + "\n");
						}
					}
				}
			}
		} catch (Exception HttpResponseException) { }
	}
	
	// Izpišemo aktivne uporabnike.
	public void izpisiAktivne(String person) throws JsonParseException, JsonMappingException, IOException {
		List<Uporabnik> uporabniki = ExtendedKlient.seznam();
		String signedInUsers = "";
		for (Uporabnik user : uporabniki) {
			signedInUsers += user.getUsername() + "\n";
		}
		oknoAktivni.setText(signedInUsers);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == prijavaGumb) {
			try {
				user = vzdevek.getText();
				ExtendedKlient.prijavi(user);
				// Ko prijavimo uporabnika zaklenemo okence za vzdevek, da ne
				// moremo prijaviti drugega dokler smo prijavljeni mi.
				vzdevek.setEnabled(false);
				input.setEnabled(true); // Sedaj lahko tipkamo
				robot.activate();
				System.out.println("Prijavil sem uporabnika " + user + ".");
			} catch (Exception error) {
				System.out.println("Prijavil sem uporabnika " + user + ".");
			}
		} else if (e.getSource() == odjavaGumb) {
			try {
				ExtendedKlient.odjavi(user);
				user = "";
				vzdevek.setText("");
				vzdevek.setEnabled(true);
				input.setEnabled(false);
				robot.deactivate();
			} catch (Exception error) {
				System.out.println("Pojavila se je napaka, uporabnika nismo mogli odjaviti.");
				System.out.println(error);
			}
		} else if (e.getSource() == aktivniGumb) {
			try {
				izpisiAktivne(user);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	} 

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == input) {
			if (e.getKeyChar() == '\n') {
				String message = input.getText();
				this.addMessage(user, message);
				try {
					ExtendedKlient.poslji(user, inputKomu.getText().equals("") ? null : inputKomu.getText(), message);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				input.setText("");
			}
		}
	}

	// Pravilno zapiramo okno.
	@Override
	public void windowClosed(WindowEvent e) {
		input.setEnabled(false);
		System.out.println("Okno se je zaprlo.");
		try {
			if (prijavljen(user) == true) {
				ExtendedKlient.odjavi(user);;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		robot.deactivate();
		System.out.println("Zapiramo okno. Odjavljamo uporabnika. Ustavimo izpisovalnega robota.");
	}

	
	    public void windowClosing( WindowEvent e ) {
	    	try {
				if (prijavljen(user) == true) {
					ExtendedKlient.odjavi(user);;
				}
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
	    	robot.deactivate();
	    	System.out.println("Zapiramo okno. Odjavljamo uporabnika. Ustavimo izpisovalnega robota.");
	    }

	@Override
	public void windowDeactivated(WindowEvent e) {
		;
	} 
	

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println("Pomanjšali okno.");

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println("Živjo!");
		output.setText("Pozdravljen v Chit-Chat klientu. Prijavi se in zaèni klepetati." + "\n");
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
