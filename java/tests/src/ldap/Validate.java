package ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import com.sun.jndi.ldap.LdapCtxFactory;

public class Validate
{

	public static void main(String[] args)
	{
		//String ldapHostname = "192.168.20.6:4389/cn=ADS,dc=Navatech,dc=COM";
		//String ldapUsername = "ads01";
		String ldapHostname = "192.168.20.6:389";
		String ldapUsername = "usr01@ads.navatech.com";
		//String ldapUsername = "ADS\\usr01";
		String ldapPassword = "Advans0l";
		boolean authenticationGranted = false;
		String ldapContext = String.format("ldap://%s", ldapHostname);
		Hashtable<String, String> ldapUserProperties = new Hashtable<String, String>();
		ldapUserProperties.put(Context.SECURITY_PRINCIPAL, ldapUsername);
		ldapUserProperties.put(Context.SECURITY_CREDENTIALS, ldapPassword);
		try {
			DirContext directoryContext = LdapCtxFactory.getLdapCtxInstance(ldapContext, ldapUserProperties);
			authenticationGranted = true;
		} catch (NamingException e) {
			System.out.println("Authentication failed!\n "+e.toString());
		}
		if (authenticationGranted)
			System.out.println("OK");
		else
			System.out.println("NO");

	}

}
