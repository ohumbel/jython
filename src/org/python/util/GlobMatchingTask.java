package org.python.util;

import java.io.File;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

public abstract class GlobMatchingTask extends MatchingTask {

    private Path src;

    protected File destDir;

    private Set<File> toExpose = Generic.set();

    /**
     * Set the source directories to find the class files to be exposed.
     */
    public void setSrcdir(Path srcDir) {
        if (src == null) {
            src = srcDir;
        } else {
            src.append(srcDir);
        }
    }

    /**
     * Gets the source dirs to find the class files to be exposed.
     */
    public Path getSrcdir() {
        return src;
    }

    /**
     * Set the destination directory into which the Java source files should be compiled.
     *
     * @param destDir
     *            the destination director
     */
    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Gets the destination directory into which the java source files should be compiled.
     *
     * @return the destination directory
     */
    public File getDestdir() {
        return destDir;
    }

    @Override
    public void execute() throws BuildException {
        checkParameters();
        toExpose.clear();
        for (String srcEntry : src.list()) {
            File srcDir = getProject().resolveFile(srcEntry);
            if (!srcDir.exists()) {
                throw new BuildException("srcdir '" + srcDir.getPath() + "' does not exist!",
                                         getLocation());
            }
            String[] files = getDirectoryScanner(srcDir).getIncludedFiles();
            scanDir(srcDir, destDir != null ? destDir : srcDir, files);
        }
        process(toExpose);
    }

    protected abstract void process(Set<File> matches);

    protected abstract String getFrom();

    protected abstract String getTo();

    protected void scanDir(File srcDir, File destDir, String[] files) {
        GlobPatternMapper m = new GlobPatternMapper();
        m.setFrom(getFrom());
        m.setTo(getTo());
        SourceFileScanner sfs = new SourceFileScanner(this);
        for (File file : sfs.restrictAsFiles(files, srcDir, destDir, m)) {
            toExpose.add(file);
        }
    }

    /**
     * Check that all required attributes have been set and nothing silly has been entered.
     */
    protected void checkParameters() throws BuildException {
        if (src == null || src.size() == 0) {
            throw new BuildException("srcdir attribute must be set!", getLocation());
        }
        if (destDir != null && !destDir.isDirectory()) {
            throw new BuildException("destination directory '" + destDir + "' does not exist "
                    + "or is not a directory", getLocation());
        }
    }

    public void runFromCommandLine(String[] args) {
        Project project = new Project();
        project.init();
        project.setBaseDir(new File("."));
        DefaultLogger logger = new DefaultLogger();
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        logger.setMessageOutputLevel(Project.MSG_INFO);
        project.addBuildListener(logger);
        setProject(project);
        if (getTaskName() == null || getTaskName().isEmpty()) {
            setTaskName(getClass().getSimpleName());
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--srcdir".equals(arg) || "-srcdir".equals(arg)) {
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                setSrcdir(new Path(project, args[i]));
            } else if ("--destdir".equals(arg) || "-destdir".equals(arg)) {
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                setDestdir(new File(args[i]));
            } else if ("--includesfile".equals(arg) || "-includesfile".equals(arg)) {
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                setIncludesfile(new File(args[i]));
            } else if ("--includes".equals(arg) || "-includes".equals(arg)) {
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                setIncludes(args[i]);
            } else if ("--excludesfile".equals(arg) || "-excludesfile".equals(arg)) {
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                setExcludesfile(new File(args[i]));
            } else if ("--excludes".equals(arg) || "-excludes".equals(arg)) {
                if (++i >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                setExcludes(args[i]);
            } else if (arg.startsWith("--srcdir=")) {
                setSrcdir(new Path(project, arg.substring("--srcdir=".length())));
            } else if (arg.startsWith("-srcdir=")) {
                setSrcdir(new Path(project, arg.substring("-srcdir=".length())));
            } else if (arg.startsWith("--destdir=")) {
                setDestdir(new File(arg.substring("--destdir=".length())));
            } else if (arg.startsWith("-destdir=")) {
                setDestdir(new File(arg.substring("-destdir=".length())));
            } else if (arg.startsWith("--includesfile=")) {
                setIncludesfile(new File(arg.substring("--includesfile=".length())));
            } else if (arg.startsWith("-includesfile=")) {
                setIncludesfile(new File(arg.substring("-includesfile=".length())));
            } else if (arg.startsWith("--includes=")) {
                setIncludes(arg.substring("--includes=".length()));
            } else if (arg.startsWith("-includes=")) {
                setIncludes(arg.substring("-includes=".length()));
            } else if (arg.startsWith("--excludesfile=")) {
                setExcludesfile(new File(arg.substring("--excludesfile=".length())));
            } else if (arg.startsWith("-excludesfile=")) {
                setExcludesfile(new File(arg.substring("-excludesfile=".length())));
            } else if (arg.startsWith("--excludes=")) {
                setExcludes(arg.substring("--excludes=".length()));
            } else if (arg.startsWith("-excludes=")) {
                setExcludes(arg.substring("-excludes=".length()));
            } else {
                throw new IllegalArgumentException("Unknown argument: " + arg);
            }
        }

        if (destDir != null && !destDir.exists()) {
            destDir.mkdirs();
        }

        execute();
    }
}