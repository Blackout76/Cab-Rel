class DijkstraTree:
	def __init__(self, areasDict):
		self.nodesList = []
		self.arcsList = []
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