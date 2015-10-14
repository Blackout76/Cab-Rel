

from math import sqrt
from Vertice import *

class Street:
	def __init__(self, name, vertices, oneway):
		self.streetName = name
		self.streetVertices = []
		self.streetVertices = vertices
		self.streetOneway = oneway
		self.streetWeight = self.WeightCalculation()

	def WeightCalculation(self):
		difX = self.streetVertices[0].verticeX - self.streetVertices[1].verticeX
		difY = self.streetVertices[0].verticeY - self.streetVertices[1].verticeY
		result = sqrt((difX*difX)+(difY*difY))
		return result

	def ToJsonFormat(self):
		street = {}
		street["name"] = self.streetName
		street["path"] = []
		for vertexToJson in self.streetVertices:
			street["path"].append(vertexToJson.verticeName)
		street["oneway"] = self.streetOneway
		return street
