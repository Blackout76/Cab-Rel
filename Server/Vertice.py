


##	A vertex
#
#	More details.
class Vertice:
	##	The constructor
	#	@param verticeJson dictionary of a vertex
	def __init__(self, verticeJson):
		#initialise vertex name
		self.verticeName = verticeJson["name"]
		#initialise vertex x
		self.verticeX = verticeJson["x"]
		#initialise vertex y
		self.verticeY = verticeJson["y"]

	##	Return the vertex in a dictionary
	def toDictFormat(self):
		#initialise vertex dictionary
		vertex = {}
		#format the vertex dictionary
		vertex["name"] = self.verticeName
		vertex["x"] = self.verticeX
		vertex["y"] = self.verticeY
		return vertex