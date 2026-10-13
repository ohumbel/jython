package org.python.antlr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Decorates the PythonTreeWalker class as a JUnit test case for a single .py
 * file
 */
public class PythonTreeWalkerTestCase {

	private String _path;

	public PythonTreeWalkerTestCase() {
	}

	/**
	 * Constructor called from rerun menu point in eclipse
	 * 
	 * @param name The name of the test.
	 */
	public PythonTreeWalkerTestCase(String name) {
		setPath(name);
	}

	/**
	 * Create a test case which walks <code>pyFile</code>.
	 * 
	 * @param pyFile The *.py file
	 */
	public PythonTreeWalkerTestCase(File pyFile) {
		this(pyFile.getAbsolutePath());
	}

	@Test
	public void runTest() throws Throwable {
		String path = getPath();
		if (path == null) {
			return;
		}
		File file = new File(path);
		assertTrue(file.exists(), "file " + path + " not found");
		PythonTreeWalker treeWalker = new PythonTreeWalker();
		treeWalker.setTolerant(false);
		treeWalker.setParseOnly(false);
		PythonTree tree = treeWalker.parse(new String[] { path });
		if (tree == null) {
			if (!isEmpty(file)) {
				assertNotNull(tree, "no tree generated for file ".concat(path));
			}
		}
	}

	public int countTestCases() {
		return 1;
	}

	private void setPath(String path) {
		_path = path;
	}

	private String getPath() {
		return _path;
	}

	/**
	 * 'empty' check for a .py file
	 * 
	 * @param file The file to check
	 * @return <code>true</code> if the file is really empty, or only contains
	 *         comments
	 * @throws IOException
	 */
	private boolean isEmpty(File file) throws IOException {
		boolean isEmpty = true;
		assertTrue(file.canRead(), "file " + file.getAbsolutePath() + " is not readable");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line = reader.readLine();
			while (line != null && isEmpty) {
				line = line.trim();
				if (line.length() > 0 && !line.startsWith("#")) {
					isEmpty = false;
				}
				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
		return isEmpty;
	}

}
