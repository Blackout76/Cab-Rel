from Vertice import *



##	A bridge
#
#	More details.
class Bridge:
	##	The constructor
	#	@param vertex vertex from
	#	@param bridgeJson bridge dictionary
	def __init__(self, vertex, bridgeJson):
		#vertex from
		self.bridgeVertice = vertex
		bridgeTo = bridgeJson["to"]
		#area linked
		self.bridgeToArea = bridgeTo["area"]
		#vertex linked with from
		self.bridgeToVertex = bridgeTo["vertex"]
		#bridge weight
		self.bridgeWeight = bridgeJson["weight"]

	##	Return the bridge in a dictionary
	def toDictFormat(self):
		#initialise street dictionnary
		bridge = {}
		bridge["from"] = self.bridgeVertice.verticeName
		#initialisation of bridge["to"]
		bridgeTo = {}
		bridgeTo["area"] = self.bridgeToArea
		bridgeTo["vertex"] = self.bridgeToVertex
		bridge["to"] = bridgeTo
		bridge["weight"] = self.bridgeWeight	
		return bridge
