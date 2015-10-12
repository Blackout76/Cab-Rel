#! /usr/bin/env python
# I want to use : utf-8 please

#*******************************************************************************
#********************************* Import **************************************
#*******************************************************************************

import sys, json, os, string
reload(sys)
sys.setdefaultencoding('utf-8')
from flask import *
from serverDatabase import *
from flask.ext.socketio import SocketIO

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

serverHost = '0.0.0.0'

#*******************************************************************************
#********************************** LOGGER *************************************
#*******************************************************************************

import logging
from logging.handlers import RotatingFileHandler
 
logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

"""
CRITICAL	50
ERROR		40
WARNING		30
INFO		20		<-- file lvl
DEBUG		10		<-- Global level / Console lvl
"""
 
if not os.path.isdir('log'):
	os.mkdir('log')
 
# create formatter for the type of log
formatter = logging.Formatter('%(asctime)s :: %(levelname)s :: %(message)s')
# file handler, filename = server.log, 1Mo max, 5 log (4  archived and actual log)
file_handler = RotatingFileHandler('log/server.log', 'a', 1000000, 5)

# level INFO with the previous formatter
file_handler.setLevel(logging.INFO)
file_handler.setFormatter(formatter)
logger.addHandler(file_handler)

# one handler for write DEBUG in the console
steam_handler = logging.StreamHandler()
steam_handler.setLevel(logging.DEBUG)
logger.addHandler(steam_handler)

logger.info('******************** NEW SESSION ********************')
logger.info('Logger is ready')

#*******************************************************************************
#*********************************** CORS **************************************
#*******************************************************************************
##	Empeche les erreurs de type CORS
#	@param resp
def addCorsHeaders(resp):
    resp.headers['Access-Control-Allow-Origin'] = '*'
    resp.headers['Access-Control-Allow-Methods'] = 'POST, GET, DELETE, PUT'
    resp.headers['Access-Control-Max-Age'] = '21600'
    resp.headers['Access-Control-Allow-Headers'] = 'accept, origin, authorization, content-type'

#*******************************************************************************
#**************************** Variable global **********************************
#*******************************************************************************

#*********************************** / *****************************************
## root et redirection
@app.route('/')
##	acces a la racine du service web
def root():
	resp = make_response()
	resp.status_code = 201
	resp.data = 'Root, nothing to see here'
	return resp

@app.route('/')
## Redirige sur la page /static/main.html
def redirectToMain():
	return redirect('/static/main.html')

## test reception message socket
@socketio.on('message')
def handle_message(message):
    print('received message: ' + message)

#******************************* /exemple ************************************
@app.route('/exemple', methods=['GET'])
## Exemple d'une requête get requête http
def exemple_GET():
	logger.info('/exemple		method : GET')
	resp = make_response()
	addCorsHeaders(resp)
	try:
		resp.headers['Content-Type'] = 'application/json'
		#si tu se passe bien retour d'un code erreur 200
		resp.status_code = 200
		resp.data = get_point_of_interest_locations()
	except:
		#si erreur retour d'un code erreur 500
	 	resp.status_code = 500
	 	resp.data = 'error 500 : Unexpected error'
	 	return resp
	return resp


@app.route('/exemple', methods=['POST'])
##	Permet de creer une nouvelle locations
def locations_POST():
	logger.info('/exemple		method : POST')
	resp = make_response()
	addCorsHeaders(resp)
	#si aucune erreur alors le JSON est au bon format
	try:
		resp.headers['Content-Type'] = 'application/json'
		#recuperation de la donnee envoyer au serveur
		req_data = json.loads(request.data)
		name = str(req_data["name"])
		kind = str(req_data["kind"])
		coord = req_data["coord"]
		lat = coord["lat"]
		lon = coord["lon"]
	except:
		#si une erreur de format retour erreur 400
		resp.status_code = 400
		resp.data = "error 400 : Bad format json"
		return resp

	try:
		#si aucune erreur retour 201
		resp.status_code = 201
		#lancement des enregistrement en BDD
		add_new_point_of_interest(name, kind, lat, lon)
	except:
		#si une erreur interne au serveur retour erreur 500
		resp.status_code = 500
		resp.data = 'error 500 : Unexpected error'
		return resp
	return resp

#************************ /exemple/exempleid ******************************
@app.route('/exemple/<int:exempleId>', methods=['GET'])
##	Exemple de get avec un id
#	@param exempleId paramêtre souhaité
def locations_by_id_GET(exempleId):
	logger.info('/exemple/<int:exempleId>		method : GET')
	resp = make_response()
	addCorsHeaders(resp)
	try:
		point_of_interest_data = get_point_of_interest_by_id(locationId)
		resp.headers['Content_Type'] = 'application/json'
		if point_of_interest_data == "none":
			resp.status_code = 404
			resp.data = "error 404 : not found"
		else:
			resp.headers['Content-Type'] = 'application/json'
			#si tu se passe bien retour d'un code erreur 200
			resp.status_code = 200
			resp.data = point_of_interest_data
	except:
		#si erreur retour d'un code erreur 500
		resp.status_code = 500
		resp.data = 'error 500 : Unexpected error'
		return resp
	return resp


@app.route('/exemple/<int:exempleId>', methods=['DELETE'])
##	Exemple de delete avec un id
#	@param exempleId exemple de paramètre
def locations_by_id_DELETE(exempleId):
	logger.info('/exemple/<int:exempleId>		method : DELETE')
	# curl -i -X DELETE -H "Content-Type : application/json" http://172.31.1.121:1337/exemple/3
	resp = make_response()
	addCorsHeaders(resp)
	int(locationId)
	try:
		point_of_interest_data = get_point_of_interest_by_id(locationId)
		if point_of_interest_data == "none":
			resp.status_code = 404
			resp.data = "error 404 : point of interest not found"
		else:
			resp.headers['Content-Type'] = 'application/json'
			resp.status_code = 200
			#exemple de requete
			fkId = readRequest("SELECT fk_coordinates_id FROM POINT_OF_INTEREST WHERE point_of_interest_id=" + str(locationId))
			sendRequest("DELETE FROM POINT_OF_INTEREST WHERE point_of_interest_id=" + str(fkId[0]["fk_coordinates_id"]))
	except:
		#si erreur retour d'un code erreur 500
		resp.status_code = 500
		resp.data = 'error 500 : Unexpected error'
		return resp
	return resp

@app.route('/exemple/<int:exempleId>', methods=['PUT'])
##	Exemple de put
#	@param exempleId exemple de paramètre
def transports_by_id_post(transportId):
	logger.info('/exemple/<int:exempleId>		method : PUT')
	resp = make_response()
	addCorsHeaders(resp)
	try:
		drone_dict = json.loads(request.data)
		drone_data = drone_dict['drone_location']
		put_transports_return = put_transport(transportId, drone_data)

		if put_transports_return == "none":
			#si le transport n'existe pas
			resp.status_code = 404
			resp.data = "error 404 : no transport was found"
		else:
			#retour status 200 et renvoi du json formate
			resp.headers['Content-Type'] = 'application/json'
			resp.status_code = 200
			resp.data = put_transports_return
	except:
		#si une erreur interne au serveur retour status code 500
		resp.status_code = 500
		resp.data = 'error 500 : Unexpected error'
		return resp

	return resp

#*******************************************************************************
#*************************** Launch the server *********************************
#*******************************************************************************

if __name__ == '__main__':
	socketio.run(app)
app.run(host=serverHost, port=8080, debug=True)
