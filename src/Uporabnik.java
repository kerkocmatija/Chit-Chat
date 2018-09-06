import java.util.Date;

public class Uporabnik {
	private String username;
	private Date last_active;

	public Uporabnik() {
	}

	public String getUsername() {
		return username;
	}
	
	public Uporabnik(String username, Date last_active) {
		this.username = username;
		this.last_active = last_active;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLast_active() {
		return last_active;
	}

}
