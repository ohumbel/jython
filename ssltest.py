import socket
from test import test_support as support
ssl = support.import_module("ssl")

REMOTE_HOST = "self-signed.pythontest.net"

with support.transient_internet(REMOTE_HOST):
  s = ssl.wrap_socket(socket.socket(socket.AF_INET), cert_reqs=ssl.CERT_NONE)
  try:
    s.connect((REMOTE_HOST, 443))
  finally:
    s.close()
