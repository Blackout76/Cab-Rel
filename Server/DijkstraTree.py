class DijkstraTree:
	def __init__(self, areasDict):
		self.nodesList = []
		self.arcsList = []
		self.distNode = []
		self.prevNode = []
		self.nodeLeft = []
		for area in areasDict:
			for vertex in areasDict[area].verticesDict:
				node = {}
				node["vertex"] = areasDict[area].verticesDict[vertex].verticeName
				node["area"] = areasDict[area].areaName
				self.nodesList.append(node)
			for street in areasDict[area].streetsDict:
				arc = {}
				nodeFrom = {}
				nodeTo = {}
				nodeFrom["vertex"] = areasDict[area].streetsDict[street].streetVertices[0].verticeName
				nodeFrom["area"] = areasDict[area].areaName
				arc["node1"] = nodeFrom
				nodeTo["vertex"] = areasDict[area].streetsDict[street].streetVertices[1].verticeName
				nodeTo["area"] = areasDict[area].areaName
				arc["node2"] = nodeTo
				arc["weight"] = areasDict[area].streetsDict[street].WeightCalculation()
				self.arcsList.append(arc)
			for bridge in areasDict[area].bridgesList:
				arc = {}
				nodeFrom = {}
				nodeTo = {}
				nodeFrom["vertex"] = bridge.bridgeVertice.verticeName
				nodeFrom["area"] = areasDict[area].areaName
				arc["node1"] = nodeFrom
				nodeTo["vertex"] = bridge.bridgeToVertex
				nodeTo["area"] = bridge.bridgeToArea
				arc["node2"] = nodeTo
				arc["weight"] = bridge.bridgeWeight
				self.arcsList.append(arc)

	def initDijkstra(self, startPoint):
		for node in self.nodesList:
			distNode = {}
			distNode["vertex"] = node["vertex"]
			distNode["area"] = node["area"]
			if node["vertex"] == startPoint["vertex"] and node["area"] == startPoint["area"]:
				distNode["dist"] = 0
			else:
				distNode["dist"] = -1
			self.distNode.append(distNode)
			self.nodeLeft.append(node)
			prevNode = {}
			prevNode["prevNode"] = startPoint
			prevNode["node"] = node
			self.prevNode.append(prevNode)


	def findShortestPath(self, startPoint, endPoint):
		endIsFound = False
		currentWeight = -1
		currentNode = {}
		self.initDijkstra(startPoint)
		while len(self.nodeLeft) > 0 and endIsFound == False:
			for node in self.distNode:
				if (node["dist"] < currentWeight and node["dist"] >= 0) or currentWeight < 0:
					currentWeight = node["dist"]
					currentNode["vertex"] = node["vertex"]
					currentNode["area"] = node["area"]

		print "TODO"


"""			
	def __init__(self,vertice):
		self.noeuds = []
		self.point =  Point(vertice.verticeX,vertice.verticeY,vertice.verticeName)

	def getPoint(self):
		return self.point
		
	def getNoeuds(self):
		return self.noeuds
	
	def addNoeud(self,noeud):
		if not noeud in self.noeuds:
			self.noeuds.append(noeud)
	
	def toString(self) :
		a = ""
		for n in self.noeuds:
			a += n.getPoint()
		return self.point + '=>' + a

class Point:
	def __init__(self,x,y,name):
		self.name = name
		self.x = x
		self.y = y
"""