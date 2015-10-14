




class Bridge:
	def __init__(self, verticeName, weight):
		self.bridgeVertice = verticeName
		self.bridgeWeight = weight

	def ToJsonFormat(self):
		bridge = {}
		bridge["from"] = self.bridgeVertice
		bridge["to"] = {}
		bridge["weight"] = self.bridgeWeight
		return bridge
