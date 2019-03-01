# Created by:	Colin Dyson
# Student #:	7683407

import sys
import socket

sock = ''
host = "www3.cs.umanitoba.ca"
port = 13059

print("Client starting")

exit = False

while exit == False:
	try:
		term_in = raw_input(">")
	except Exception:
		print("Failed to read from terminal.")
		sys.exit()

	if term_in != "E":
		try:
			sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
			sock.connect((host, port))
		except socket.error as e:
			print("Failed to create socket.", e)
			sys.exit()

		try:
			sock.sendall(term_in + '\n')
			response = sock.recv(1024)
			print("[Server]: " + response)
		except Exception:
			print("Communication with the server has failed.")
			sys.exit()

		try:
			sock.close()
		except Exception:
			print("Failed to close socket")
			sys.exit()

	else:
		exit = True

try:
	sock.close()
except Exception:
	print("Failed to close socket")
	sys.exit()

print("Client finished")
