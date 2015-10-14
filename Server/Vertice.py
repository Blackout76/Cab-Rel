



class Vertice:
	def __init__(self, verticeJson):
		self.verticeName = verticeJson["name"]
		self.verticeX = verticeJson["x"]
		self. verticeY = verticeJson["y"]

	def ToJsonFormat(self):
		vertex = {}
		vertex["name"] = self.verticeName
		vertex["x"] = self.verticeX
		vertex["y"] = self.verticeY
		return vertex