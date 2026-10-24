package org.python.antlr;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A regression test suite traversing all *.py files, parsing and walking them
 * <p>
 * The root directory of the *.py files is read from a System property
 * <code>base.dir</code>. If this property is not set, we use the current
 * directory where the runner is started.
 * <p>
 * Example: call the runner with
 * 
 * <pre>
 * -Dbase.dir=/your/dir/containing/py/files
 * </pre>
 */
class RegressionTestSuite {
	private static final String BASEDIR_PROPERTY = "base.dir";

	@TestFactory
	List<DynamicNode> testSuite() throws Exception {
		String baseDirName = System.getProperty(BASEDIR_PROPERTY, System.getProperty("user.dir", null));
		assertNotNull(baseDirName, "unable to determine root directory. Try specifying property " + BASEDIR_PROPERTY);
		File baseDir = new File(baseDirName);
		assertTrue(baseDir.exists(), baseDirName + " does not exist");
		assertTrue(baseDir.isDirectory(), baseDirName + " is not a directory");
		List<DynamicNode> nodes = new ArrayList<>();
		buildSuite(baseDir, new PyFileFilter(), nodes);
		return nodes;
	}

	private static void buildSuite(File currentDir, FilenameFilter filter, List<DynamicNode> currentNodes) throws Exception {
		File[] list = currentDir.listFiles(filter);
		if (list == null || list.length == 0) {
			return;
		}
		List<File> potentialDirectories = Arrays.asList(list);

		List<File> pyFiles = new ArrayList<File>(potentialDirectories.size());
		Collections.sort(potentialDirectories, new FileComparator());
		Iterator<File> directoryIterator = potentialDirectories.iterator();
		while (directoryIterator.hasNext()) {
			File potentialDirectory = directoryIterator.next();
			if (potentialDirectory.isDirectory()) {
				List<DynamicNode> subNodes = new ArrayList<>();
				buildSuite(potentialDirectory, filter, subNodes);
				if (!subNodes.isEmpty()) {
					currentNodes.add(DynamicContainer.dynamicContainer(potentialDirectory.getName(), subNodes));
				}
			} else {
				pyFiles.add(potentialDirectory);
			}
		}
		Iterator<File> fileIterator = pyFiles.iterator();
		while (fileIterator.hasNext()) {
			File f = fileIterator.next();
			currentNodes.add(DynamicTest.dynamicTest(f.getName(), () -> {
				new PythonTreeWalkerTestCase(f).runTest();
			}));
		}
	}

	private static class PyFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			if (name.endsWith(".py")) {
				return true;
			} else {
				if (name.equals(".svn")) {
					return false;
				}
			}
			return new File(dir, name).isDirectory();
		}
	}

	private static class FileComparator implements Comparator<File> {
		public int compare(File f1, File f2) {
			return f1.getAbsolutePath().compareTo(f2.getAbsolutePath());
		}
	}

}
