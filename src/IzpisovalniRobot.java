import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class IzpisovalniRobot extends TimerTask {
	private ChatFrame chat;
	private Timer timer;

	public IzpisovalniRobot(ChatFrame chat) {
		this.chat = chat;
		this.timer = new Timer();
	}

	/**
	 * Aktiviraj robota!
	 */
	public void activate() {
		timer.scheduleAtFixedRate(this, 0, 500);

	}

	public void deactivate() {
		timer.cancel();
	}

	// ROBOTA LE AKTIVIRAMO TER MU POVEMO, KAJ MORA DELAT.
	@Override
	public void run() {
		try {
			chat.izpisiAktivne(null);
			chat.dodajPrejeta();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
