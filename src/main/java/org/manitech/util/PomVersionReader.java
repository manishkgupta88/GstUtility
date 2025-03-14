package org.manitech.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileReader;

/**
 * User : Manish K. Gupta
 */

public class PomVersionReader {
    private static final Logger logger = LogManager.getLogger(PomVersionReader.class);

    public static String getVersion() {
        try {
            File pomFile = new File("pom.xml"); // Adjust path if needed
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(pomFile));

            String version = model.getVersion();
            if (version == null) {
                version = model.getParent().getVersion(); // In case of parent version
            }

            logger.info("POM Version: " + version);
            return version;
        } catch (Exception e) {
            logger.error("Error reading pom file", e);
        }
        return Constants.DefaultJarVersion;
    }
}
