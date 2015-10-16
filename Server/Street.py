

from math import sqrt
from Vertice import *

##	A street
#
#	More details.
class Street:
	##	The constructor
	#	@param name name of the street
	#	@param vertices vertices list
	#	@param oneway bool
	def __init__(self, name, vertices, oneway):
		#street name
		self.streetName = name
		#vertex list
		self.streetVertices = []
		self.streetVertices = vertices
		#oneway bool
		self.streetOneway = oneway
		self.streetWeight = self.WeightCalculation()
	
	##	Calulation of the street weight
	def WeightCalculation(self):
		difX = self.streetVertices[0].verticeX - self.streetVertices[1].verticeX
		difY = self.streetVertices[0].verticeY - self.streetVertices[1].verticeY
		#calculation of vertices distance
		result = sqrt((difX*difX)+(difY*difY))
		return result

	##	Return the street in a dictionary
	def toDictFormat(self):
		#initialise street dictionnary
		street = {}
		street["name"] = self.streetName
		street["path"] = []
		#for each vertices add vertice name
		for vertexToJson in self.streetVertices:
			street["path"].append(vertexToJson.verticeName)
		street["oneway"] = self.streetOneway
		return street
