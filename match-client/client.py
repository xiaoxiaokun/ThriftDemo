from pydoc import cli
from sys import stdin

from match import Match
from match.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

def operate(op, id, username, score):
    # Make socket
    transport = TSocket.TSocket('localhost', 9090)

    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TBufferedTransport(transport)

    # Wrap in a protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a client to use the protocol encoder
    client = Match.Client(protocol)

    # Connect!
    transport.open()

    if op == 'add':
        matchRes = client.add(id, username, score)
        print("match result is: ", matchRes)
    elif op == 'remove':
        client.remove(id, username)

    # Close!
    transport.close()

def main():
    while True:
        line = stdin.readline()
        if not line:
            break
        line = line.strip()
        if not line:
            continue
        op, id, username, score = line.split()
        operate(op, int(id), username, int(score))

if __name__ == '__main__':
    main()