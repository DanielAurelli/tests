package https;



import java.io.DataOutputStream;
import java.net.*;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/*
 * This example illustrates using a URL to access resources
 * on a secure site.
 *
 * If you are running inside a firewall, please also set the following
 * Java system properties to the appropriate value:
 *
 *   https.proxyHost = <secure proxy server hostname>
 *   https.proxyPort = <secure proxy server port>
 *
 */

public class URLReader {
  public static void main(String[] args) throws Exception 
  {
    //String urlParameters  = "firstname=jose&lastname=bb&param3=c";
    String urlParameters  = "a=b";
    //URL url = new URL("http://192.168.2.90:8080/examples/servlets/servlet/RequestParamExample");
    //URL url = new URL("https://localhost:8888/SAMPLES/SRC/ADSNSUM.NSN");
    //URL url = new URL("https://www.google.com/search?q=hello");
    //URL url = new URL("https://www.verisign.com/");
    //URL url = new URL("https://docs.oracle.com/en/java/index.html");
    //URL url = new URL("https://adsteam.deyel.com");
    //URL url = new URL("http://192.168.1.45:8080/ads/ShowConfig");
    URL url = new URL("https://177.93.109.92:8443/ads/login.htm");
    String httpMethod="GET";
    //URLConnection client = url.openConnection();
    if (url.getProtocol().equals("https"))
    {
      HttpsURLConnection server = (HttpsURLConnection) url.openConnection();
      server.setDoOutput( true );
      ((HttpsURLConnection) server).setRequestMethod( httpMethod );
      server.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
      server.setRequestProperty( "Content-Length", Integer.toString( urlParameters.length() ));
      server.setUseCaches( false );
      DataOutputStream wr = new DataOutputStream( server.getOutputStream());
      wr.write( urlParameters.getBytes() );
      wr.flush();
      server.connect();

      Scanner sc = new Scanner(server.getInputStream());
      while (sc.hasNext())
      {
        System.out.println(sc.nextLine());
      }
      sc.close();
      System.out.println("\nRETURN : "+((HttpURLConnection) server).getResponseCode()+" Size : "+server.getContentLength());
      
    } else {
      HttpURLConnection client = (HttpURLConnection) url.openConnection();
      if ((!httpMethod.equals("GET")) && urlParameters.length()>0)
      {
        client.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        client.setRequestProperty( "Content-Length", Integer.toString( urlParameters.length() ));
        client.setDoOutput( true );
        DataOutputStream wr = new DataOutputStream( client.getOutputStream());
        wr.write( urlParameters.getBytes() );
        wr.flush();
      } else {
        client.setRequestProperty( "Content-Type", "text/html"); 
      }
      client.setUseCaches( false );
      client.connect();

      Scanner sc = new Scanner(client.getInputStream());
      while (sc.hasNext())
      {
        System.out.println(sc.nextLine());
      }
      sc.close();
      System.out.println("\nRETURN : "+((HttpURLConnection) client).getResponseCode()+" Size : "+client.getContentLength());
    }
  }
}
