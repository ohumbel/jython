import os.path
import sys
from warnings import warn

try:
    _console = sys._jy_console
    _reader = _console.reader
except AttributeError:
    raise ImportError("Cannot access JLine setup")

try:
    # jarjar-ed version
    from org.python.jline.reader import Candidate, Completer
except ImportError:
    try:
        from org.jline.reader import Candidate, Completer
    except ImportError:
        Candidate = Completer = None


__all__ = ['add_history', 'clear_history', 'get_begidx', 'get_completer',
           'get_completer_delims', 'get_current_history_length',
           'get_endidx', 'get_history_item', 'get_history_length',
           'get_line_buffer', 'insert_text', 'parse_and_bind',
           'read_history_file', 'read_init_file', 'redisplay',
           'remove_history_item', 'set_completer', 'set_completer_delims',
           'set_history_length', 'set_pre_input_hook', 'set_startup_hook',
           'write_history_file']

_history_list = None

# The need for the following warnings should go away once we update
# JLine. Choosing ImportWarning as the closest warning to what is
# going on here, namely this is functionality not yet available on
# Jython.

class NotImplementedWarning(ImportWarning):
    """Not yet implemented by Jython"""

class SecurityWarning(ImportWarning):
    """Security manager prevents access to private field"""


def parse_and_bind(string):
    pass

def _get_buffer():
    if hasattr(_reader, 'buffer'):
        return _reader.buffer
    elif hasattr(_reader, 'getBuffer'):
        return _reader.getBuffer()
    elif hasattr(_reader, 'cursorBuffer'):
        return _reader.cursorBuffer
    return None

def get_line_buffer():
    buf = _get_buffer()
    if buf is not None:
        if hasattr(buf, 'buffer'):
            return str(buf.buffer)
        return str(buf.toString())
    return ""

def insert_text(string):
    buf = _get_buffer()
    if buf is not None and hasattr(buf, 'write'):
        buf.write(string)
    elif hasattr(_reader, 'putString'):
        _reader.putString(string)

def read_init_file(filename=None):
    warn("read_init_file: %s" % (filename,), NotImplementedWarning, "module", 2)

def read_history_file(filename="~/.history"):
    expanded = os.path.expanduser(filename)
    with open(expanded) as f:
        for line in f:
            add_history(line.rstrip('\r\n'))

def write_history_file(filename="~/.history"):
    expanded = os.path.expanduser(filename)
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    with open(expanded, 'w') as f:
        if hasattr(hist, 'entries'):
            for line in hist.entries():
                f.write(line.value().encode("utf-8"))
                f.write("\n")
        else:
            for entry in hist:
                f.write(entry.line().encode("utf-8"))
                f.write("\n")

def clear_history():
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    if hasattr(hist, 'purge'):
        hist.purge()
    elif hasattr(hist, 'clear'):
        hist.clear()

def add_history(line):
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    hist.add(line)

def get_history_length():
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    if hasattr(hist, 'maxSize'):
        return hist.maxSize
    return hist.size()

def set_history_length(length):
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    if hasattr(hist, 'maxSize'):
        hist.maxSize = length

def get_current_history_length():
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    return hist.size()

def get_history_item(index):
    # JLine indexes from 0 while readline indexes from 1 (at least in test_readline)
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    if index > 0 and index <= hist.size():
        return hist.get(index - 1)
    else:
        return None

def remove_history_item(pos):
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    if hasattr(hist, 'remove'):
        hist.remove(pos)
    else:
        items = [entry.line() for entry in hist]
        if 0 <= pos < len(items):
            items.pop(pos)
            hist.purge()
            for item in items:
                hist.add(item)

def replace_history_item(pos, line):
    hist = _reader.history if hasattr(_reader, 'history') else _reader.getHistory()
    if hasattr(hist, 'set'):
        hist.set(pos, line)
    else:
        items = [entry.line() for entry in hist]
        if 0 <= pos < len(items):
            items[pos] = line
            hist.purge()
            for item in items:
                hist.add(item)

def redisplay():
    _reader.redrawLine()

def set_startup_hook(function=None):
    _console.startupHook = function

def set_pre_input_hook(function=None):
    warn("set_pre_input_hook %s" % (function,), NotImplementedWarning, stacklevel=2)

_completer_function = None

if Completer is not None:
    class _JLineCompleter(Completer):
        def __init__(self, function):
            self.function = function

        def complete(self, reader, parsedLine, candidates):
            line = parsedLine.line()
            cursor = parsedLine.cursor()
            start = _get_delimited(line, cursor)[0]
            delimited = line[start:cursor]

            for state in xrange(100):
                completion = None
                try:
                    completion = self.function(delimited, state)
                except:
                    pass
                if completion:
                    candidates.add(Candidate(completion))
                else:
                    break
else:
    _JLineCompleter = None

def set_completer(function=None):
    """set_completer([function]) -> None
    Set or remove the completer function.
    The function is called as function(text, state),
    for state in 0, 1, 2, ..., until it returns a non-string.
    It should return the next possible completion starting with 'text'."""

    global _completer_function
    _completer_function = function

    if function is None:
        if hasattr(_reader, 'setCompleter'):
            _reader.setCompleter(None)
        return

    if _JLineCompleter is not None and Candidate is not None:
        if hasattr(_reader, 'setCompleter'):
            _reader.setCompleter(_JLineCompleter(function))
    elif hasattr(_reader, 'addCompleter'):
        def complete_handler(buffer, cursor, candidates):
            start = _get_delimited(buffer, cursor)[0]
            delimited = buffer[start:cursor]
            for state in xrange(100):
                completion = None
                try:
                    completion = function(delimited, state)
                except:
                    pass
                if completion:
                    candidates.add(completion)
                else:
                    break
            return start
        _reader.addCompleter(complete_handler)

def get_completer():
    return _completer_function

def _get_delimited(buffer, cursor):
    start = cursor
    for i in xrange(cursor-1, -1, -1):
        if buffer[i] in _completer_delims:
            break
        start = i
    return start, cursor

def get_begidx():
    buf = _get_buffer()
    if buf is not None:
        text = str(buf.buffer) if hasattr(buf, 'buffer') else str(buf.toString())
        cursor = buf.cursor if hasattr(buf, 'cursor') else buf.cursor()
        return _get_delimited(text, cursor)[0]
    return 0

def get_endidx():
    buf = _get_buffer()
    if buf is not None:
        text = str(buf.buffer) if hasattr(buf, 'buffer') else str(buf.toString())
        cursor = buf.cursor if hasattr(buf, 'cursor') else buf.cursor()
        return _get_delimited(text, cursor)[1]
    return 0

def set_completer_delims(string):
    global _completer_delims, _completer_delims_set
    _completer_delims = string
    _completer_delims_set = set(string)

def get_completer_delims():
    return _completer_delims

set_completer_delims(' \t\n`~!@#$%^&*()-=+[{]}\\|;:\'",<>/?')
