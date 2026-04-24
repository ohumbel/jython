package org.python.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class VersionMatchingAntTask extends Task {

    private static final Map<String, String> ARTEFACT_NAME_FORMATS;

    static {
        ARTEFACT_NAME_FORMATS = new HashMap<>();
        ARTEFACT_NAME_FORMATS.put("rev_ant", "ant-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_antlr", "antlr-complete-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_asm", "asm-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_bouncycastle", "bcpkix-jdk18on-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_commons_compress", "commons-compress-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_commons_io", "commons-io-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_guava", "guava-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_icu4j", "icu4j-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_informix", "jdbc-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jakarta_servlet_api", "jakarta.servlet-api-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_java_sizeof", "java-sizeof-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jffi", "jffi-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jline", "jline-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jnr_constants", "jnr-constants-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jnr_ffi", "jnr-ffi-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jnr_netdb", "jnr-netdb-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_jnr_posix", "jnr-posix-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_junit", "junit-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_netty", "netty-buffer-%s.jar");
        ARTEFACT_NAME_FORMATS.put("rev_oracle", "ojdbc8-%s.jar");
    }

    @Override
    public void execute() throws BuildException {
        try {
            Set<String> gradleArtefacts = parseGradleArtefacts();
            Set<String> antArtefacts = parseAntArtefacts();
            gradleArtefacts.forEach(gradleArtefact -> checkAntArtefact(gradleArtefact, antArtefacts));
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
    }

    Set<String> parseGradleArtefacts() throws IOException {
        Set<String> gradleArtefacts = new HashSet<>();
        Path buildXml = getBuildXmlPath();
        Path toml = buildXml.getParent().resolve("gradle").resolve("libs.versions.toml");
        if (Files.exists(toml)) {
            List<String> lines = Files.readAllLines(toml);
            lines.forEach(line -> addGradleArtefact(line, gradleArtefacts));
        }
        return gradleArtefacts;
    }

    Set<String> parseAntArtefacts() throws IOException {
        Set<String> antArtefacts = new HashSet<>();
        Path buildXml = getBuildXmlPath();
        if (Files.exists(buildXml)) {
            List<String> lines = Files.readAllLines(buildXml);
            lines.forEach(line -> addAntArtefact(line, antArtefacts));
        }
        return antArtefacts;
    }

    static String calculateGradleArtefact(String revisionName, String revision) throws BuildException {
        String artefactNameFormat = ARTEFACT_NAME_FORMATS.get(revisionName);
        if (artefactNameFormat != null) {
            return artefactNameFormat.formatted(revision);
        } else {
            throw new BuildException("Missing artefact name format for '" + revisionName + "'");
        }
    }

    static String unquote(String input) {
        if (input != null) {
            if (input.startsWith("\"")) {
                input = input.substring(1);
            }
            if (input.endsWith("\"")) {
                input = input.substring(0, input.length() - 1);
            }
        }
        return input;
    }

    static void checkAntArtefact(String gradleArtefact, Set<String> antArtefacts) throws BuildException {
        if (!antArtefacts.contains(gradleArtefact)) {
            throw new BuildException("Gradle artefact '" + gradleArtefact
                            + "' is missing from build.xml. Please synchronize extlibs and build.xml with libs.versions.toml.");
        }
    }

    private static void addGradleArtefact(String line, Set<String> artefacts) {
        line = line.trim();
        if (line.startsWith("rev_")) {
            int assignmentIndex = line.indexOf('=');
            if (assignmentIndex > 0) {
                String revisionName = line.substring(0, assignmentIndex - 1).trim();
                String revision = unquote(line.substring(assignmentIndex + 1).trim());
                artefacts.add(calculateGradleArtefact(revisionName, revision));
            }
        }
    }

    private static void addAntArtefact(String line, Set<String> artefacts) {
        line = line.trim();
        if (line.startsWith("<file name=\"") && line.contains(".jar\"/>")) {
            int quoteIndex = line.indexOf('"');
            artefacts.add(line.substring(quoteIndex + 1, line.indexOf('"', quoteIndex + 1)));
        }
    }

    private Path getBuildXmlPath() {
        return Paths.get(getLocation().getFileName());
    }

}
