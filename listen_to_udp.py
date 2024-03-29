#!/usr/bin/env python3
import socket

UDP_IP = "127.0.0.1"
UDP_PORT = 1053
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

while True:
    data, addr = sock.recvfrom(1024)
    print("received message: %s" % data)
    sock.sendto(b"received", addr)
