# Created by:	Colin Dyson
# Student #:	7683407

import sys
import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = "cormorant.cs.umanitoba.ca"
port = 13059
sock.settimeout(10)

print("Client starting")

exit = False

while exit == False:
	try:
		term_in = input()
	except Exception:
		print("Failed to read from terminal.")
		sys.exit()
	if term_in != "E":
		try:
			sock.connect((host, port))
		except socket.error as e:
			print("Failed to create socket.", e)
			sys.exit()
		try:
			sock.send(term_in.encode())
			response = sock.recv(1024)
			print("[Server]:", response)
		except Exception:
			print("Communication with the server has failed.")
			sys.exit()
	else:
		exit = True

try:
	sock.close()
except Exception:
	print("Client couldn't close socket")
	sys.exit()

print("Client finished")
