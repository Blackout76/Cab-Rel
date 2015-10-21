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
			prevNodeCouple = {}
			prevNode = {}
			prevNode["vertex"] = startPoint["vertex"]
			prevNode["area"] = startPoint["area"]
			prevNodeCouple["prevNode"] = prevNode
			prevNodeCouple["node"] = node
			self.prevNode.append(prevNodeCouple)


	def findShortestPath(self, startPoint, endPoint):
		endIsFound = False
		currentNode = {}
		finalPath = []
		currentWeight = -1
		nodeListId = -1
		cmptListId = -1
		self.initDijkstra(startPoint)
		while len(self.nodeLeft) > 0 and endIsFound == False:
			for nodeLeft in self.nodeLeft:
				cmptListId = cmptListId + 1
				for node in self.distNode:
					if node["area"] == nodeLeft["area"] and node["vertex"] == nodeLeft["vertex"]:
						if (node["dist"] < currentWeight and node["dist"] >= 0) or currentWeight < 0:
							currentWeight = node["dist"]
							currentNode["vertex"] = node["vertex"]
							currentNode["area"] = node["area"]
							nodeIndex = cmptListId
			if currentNode["vertex"] == endPoint["vertex"] and currentNode["area"] == endPoint["area"]:
				endIsFound = True

			if endIsFound == False:
				for arc in self.arcsList:
					node1 = {}
					node1 = arc["node1"]
					node2 = {}
					node2 = arc["node2"]
					if node1["area"] == currentNode["area"] and node1["vertex"] == currentNode["vertex"]:
						for node in self.distNode:
							if node2["area"] == node["area"] and node2["vertex"] == node["vertex"]:
								tmpDist = arc["weight"]
								tmpDist = tmpDist + currentWeight
								if tmpDist < node["dist"] or node["dist"] < 0:
									node["dist"] = tmpDist
									for prevNodeCouple in self.prevNode:
										nodeToUpdate = {}
										nodeToUpdate = prevNodeCouple["node"]
										if nodeToUpdate["vertex"] == node2["vertex"] and nodeToUpdate["area"] == node2["area"]:
											print "toto1"
											print currentNode
											prevNode = prevNodeCouple["prevNode"]
											prevNode["area"] = currentNode["area"]
											prevNode["vertex"] = currentNode["vertex"]

					elif node2["area"] == currentNode["area"] and node2["vertex"] == currentNode["vertex"]:
						for node in self.distNode:
							if node1["area"] == node["area"] and node1["vertex"] == node["vertex"]:
								tmpDist = arc["weight"]
								tmpDist = tmpDist + currentWeight
								if tmpDist < node["dist"] or node["dist"] < 0:
									node["dist"] = tmpDist
									for prevNodeCouple in self.prevNode:
										nodeToUpdate = {}
										nodeToUpdate = prevNodeCouple["node"]
										if nodeToUpdate["vertex"] == node1["vertex"] and nodeToUpdate["area"] == node1["area"]:
											print "toto2"
											print currentNode
											prevNode = prevNodeCouple["prevNode"]
											prevNode["area"] = currentNode["area"]
											prevNode["vertex"] = currentNode["vertex"]
			currentWeight = -1
			self.nodeLeft.pop(nodeIndex)
			cmptListId = -1

		finalPath = self.findPath(endPoint)
		return finalPath

	def findPath(self, endPoint):
		print "TODO"
