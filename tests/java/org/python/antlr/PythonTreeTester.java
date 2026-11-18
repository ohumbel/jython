package org.python.antlr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.python.core.CompileMode;
import org.python.core.CompilerFlags;
import org.python.core.ParserFacade;

/**
 * A walker producing a <code>PythonTree</code> AST.
 */
public class PythonTreeTester {

    public enum Block { MODULE, INTERACTIVE, EXPRESSION };

    private boolean _parseOnly;
    private Block _block;

    public PythonTreeTester() {
        setParseOnly(false);
        setBlock(Block.MODULE);
    }

    public PythonTree parse(String[] args) throws Exception {
        PythonTree result = null;
        File f = new File(args[0]);
        try (InputStream stream = new FileInputStream(f)) {
            CompilerFlags cflags = new CompilerFlags();
            switch (_block) {
            case MODULE:
                result = ParserFacade.parse(stream, CompileMode.exec, args[0], cflags);
                break;
            case INTERACTIVE:
                result = ParserFacade.parse(stream, CompileMode.single, args[0], cflags);
                break;
            case EXPRESSION:
                result = ParserFacade.parse(stream, CompileMode.eval, args[0], cflags);
                break;
            }
        }
        if (args.length > 1 && result != null) {
            System.out.println(result.toStringTree());
        }
        return result;
    }

    /**
     * If set to <code>true</code>, only <code>PythonParser</code> is
     * called.
     * 
     * @param parseOnly
     */
    public void setParseOnly(boolean parseOnly) {
        _parseOnly = parseOnly;
    }

    public boolean isParseOnly() {
        return _parseOnly;
    }

    public void setBlock(Block block) {
        _block = block;
    }

    public Block getBlock() {
        return _block;
    }

}
