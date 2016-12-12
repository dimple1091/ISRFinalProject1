package Classes;

import java.net.MalformedURLException;
import java.net.URL;
//import java.security.cert.Certificate;
import java.io.*;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLPeerUnverifiedException;

public class HttpsClient{
	private String API_KEY = "AIzaSyDzEZw27jj8cm_IJ5IHw7o7SENiYoPdk2A";
	HttpsURLConnection con;
	public HttpsClient(String https_url){
//		String https_url = "https://www.google.com/";
		URL url;
		try {
			url = new URL(https_url);
			con = (HttpsURLConnection)url.openConnection();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void get_content(HttpsURLConnection con){
		if(con!=null){
			try {
//				System.out.println("****** Content of the URL ********");
//				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				Object obj = con.getContent();
				String input;
	
//				while ((input = br.readLine()) != null){
//					System.out.println(input);
//				}
//				br.close();
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		String url = "http://maps.google.com/maps/api/geocode/json?address=downtown+pittsburgh";
	}
//   private void print_https_cert(HttpsURLConnection con){
//
//    if(con!=null){
//
//      try {
//
//	System.out.println("Response Code : " + con.getResponseCode());
//	System.out.println("Cipher Suite : " + con.getCipherSuite());
//	System.out.println("\n");
//
//	Certificate[] certs = con.getServerCertificates();
//	for(Certificate cert : certs){
//	   System.out.println("Cert Type : " + cert.getType());
//	   System.out.println("Cert Hash Code : " + cert.hashCode());
//	   System.out.println("Cert Public Key Algorithm : "
//                                    + cert.getPublicKey().getAlgorithm());
//	   System.out.println("Cert Public Key Format : "
//                                    + cert.getPublicKey().getFormat());
//	   System.out.println("\n");
//	}
//
//	} catch (SSLPeerUnverifiedException e) {
//		e.printStackTrace();
//	} catch (IOException e){
//		e.printStackTrace();
//	}
//
//     }
//
//   }

   

}
