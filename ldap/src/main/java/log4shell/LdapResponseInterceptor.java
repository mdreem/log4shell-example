package log4shell;

import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import java.util.HashMap;
import java.util.Map;

// because I haven't been able to get ldif to work here
public class LdapResponseInterceptor extends InMemoryOperationInterceptor {

    private final Map<String, Entry> responses = new HashMap<>();

    public void addEntry(Entry entry) {
        String dn = entry.getDN();
        responses.put(dn, entry);
    }

    @Override
    public void processSearchResult(InMemoryInterceptedSearchResult result) {
        String baseDN = result.getRequest().getBaseDN();
        System.out.println("searching for " + baseDN);

        Entry entry = responses.get(baseDN);
        System.out.println("found entry: " + entry);
        try {
            result.sendSearchEntry(entry);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        System.out.println("Result: " + result);
    }
}
