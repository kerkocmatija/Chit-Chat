import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SuppressWarnings("deprecation")
public class ExtendedKlient {

	private static String url = "http://chitchat.andrej.com";
	
	// VRNEMO SEZNAM PRIJAVLJENIH UPORABNIKOV. 
	public static List<Uporabnik> seznam() throws JsonParseException, JsonMappingException, IOException {
		
		String prijavljeni = Request.Get("http://chitchat.andrej.com/users").execute().returnContent().asString();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() { };
		List<Uporabnik> uporabniki = mapper.readValue(prijavljeni, t);
		
		return uporabniki;
	}
	
	// PRIJAVA UPORABNIKA.
	public static void prijavi(String uporabnik) throws Exception {
		
		URI uri = new URIBuilder(url + "/users").addParameter("username", uporabnik).build();
		String responseBody;
		responseBody = Request.Post(uri).execute().returnContent().asString();
		System.out.println(responseBody);
	}
	
	// ODJAVLJANJE UPORABNIKA.
	// Problem je, èe smo uporabnika že izpisali in želimo okno zapreti 'na prazno'.
	// Ta problem je boljše reševati drugje.
	public static void odjavi(String uporabnik) throws IOException, URISyntaxException {
		
		URIBuilder builder = new URIBuilder("http://chitchat.andrej.com/users").addParameter("username", uporabnik);
		URI url = new URI(builder.toString());
		String responseBody;
		responseBody = Request.Delete(url).execute().returnContent().asString();
		System.out.println(responseBody);
		System.out.println("Odjavil sem uporabnika " + uporabnik + ".");
	}
	
	// PREJEMANJE SPOROÈIL.
	public static List<Sporocilo> prejmi(String uporabnik) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		
		URI uri = new URIBuilder(url + "/messages").addParameter("username", uporabnik).build();
		String sporocilo = Request.Get(uri).execute().returnContent().asString();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		TypeReference<List<Sporocilo>> t = new TypeReference<List<Sporocilo>>() { };
		List<Sporocilo> sporocila1 = mapper.readValue(sporocilo, t);
		
		return sporocila1;
	}
		
	// POŠILJANJE SPOROÈIL.
	public static void poslji(String sender, String reciever, String message) throws URISyntaxException {
		
		URI uri = new URIBuilder(url + "/messages").addParameter("username", sender).build();
		String celotnoSporocilo = "";
			if (reciever == null) {
			celotnoSporocilo = "{\"global\" : true, \"text\" :\"" + message + "\"}";
			} 
			else { 
			celotnoSporocilo = "{\"global\" : false, \"recipient\" : \"" + reciever + "\", \"text\" :\"" + message + "\"}";	
			}
			try {
			String responseBody = Request.Post(uri).bodyString(celotnoSporocilo, ContentType.APPLICATION_JSON).execute().returnContent().asString();
			System.out.println(responseBody);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
