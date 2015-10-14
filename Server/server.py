#! /usr/bin/env python
from threading import Thread
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from SimpleWebSocketServer import WebSocket, SimpleWebSocketServer

import urlparse
import threading
import sys
from MapManager import *

ip = "192.168.1.44"
port_HTTP = 8080
port_client = 2639
port_device = 2589
clients = []
devices = []

class GetHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        parsed_path = urlparse.urlparse(self.path)
        path = parsed_path.path[1:]

        if path == "deviceConnect":
            #essage =  'ws://' + ip + ':' + `port_device` + "\r\n"
            message = 'ws://192.168.1.44:2589\r\n' #Need to encole uri before send
        elif path == "clientConnect":
            #message =  'ws://' + ip + ':' + `port_client` + "\r\n"
            message = 'ws://192.168.1.44:2639\r\n' #Need to encole uri before send
        else:
            message = 'Not allowed!\r\n'
        self.send_response(200)
        self.end_headers()
        self.wfile.write(message)
        return

class ServerHTTP(Thread):
    def __init__(self, lettre):
        Thread.__init__(self)
        self.server = HTTPServer((ip, port_HTTP), GetHandler)
        print 'HTTP listenning ...'

    def run(self):
        while 1 == 1:
            self.server.serve_forever() 

class NetworkServerSocketClient(Thread):
    def __init__(self, lettre):
        Thread.__init__(self)
        self.server = SimpleWebSocketServer(ip, port_client, WebSocketClient)
        print 'WEBSOCKET clients listenning ...'

    def run(self):
        while 1 == 1:
            self.server.serveforever() 

class WebSocketClient(WebSocket):
    def handleMessage(self):
        print "Message of client: " +  self.data
        self.sendMessage(self.data)
    
    def handleConnected(self):
        clients.append(self)
        print 'Client ' + self.address + 'connected'
          
    def handleClose(self):
        clients.remove(self)
        print 'Client ' + self.address + 'closed'


class NetworkServerSocketDevice(Thread):
    def __init__(self, lettre):
        Thread.__init__(self)
        self.server = SimpleWebSocketServer(ip, port_device, WebSocketDevice)
        print 'WEBSOCKET devices listenning ...'

    def run(self):
        while 1 == 1:
            self.server.serveforever() 

class WebSocketDevice(WebSocket):
    def handleMessage(self):
        print "Message of device: " +  self.data
        self.sendMessage(self.data)
    
    def handleConnected(self):
        devices.append(self)
        print 'Device ' + self.address + 'connected'
          
    def handleClose(self):
        devices.remove(self)
        print 'Device ' + self.address + 'closed'

if __name__ == '__main__':
    MapManager = MapManager("NomMap")
    thread_server_http = ServerHTTP("1")
    thread_server_http.start()
    thread_server_client = NetworkServerSocketClient("2")
    thread_server_client.start()
    thread_server_device = NetworkServerSocketDevice("3")
    thread_server_device.start()
	
	
    #while 1 == 1:
    #    print "gne"