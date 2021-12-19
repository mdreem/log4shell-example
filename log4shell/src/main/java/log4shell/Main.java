package log4shell;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Configurator.setRootLevel(Level.INFO);
        String logString = args[0];
        logger.info("log4shell: {}", logString);
    }
}
