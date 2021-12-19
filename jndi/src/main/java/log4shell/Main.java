package log4shell;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import java.util.Properties;

public class Main {

    public static void main(String[] args) throws NamingException {
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, "ldap://127.0.0.1:1389/");

        Context ctx = new InitialDirContext(properties);
        ctx.lookup("ldap://127.0.0.1:1389/dc=javaNamingReference");
    }
}
