
##	Dijkstra algorithm
#
#	More details.
class DijkstraTree:
	##	The constructor
	#	@param areasDict areas datas to creat the tree for dijkstra algorithm
	def __init__(self, areasDict):
		#Nodes List
		self.nodesList = []
		#Arcs List composed of two nodes and a weight between both
		self.arcsList = []
		#Node with his total weight
		self.distNode = []
		#Node with his prevent node
		self.prevNode = []
		#List of nodes don't choose with the dijkstra algorithm
		self.nodeLeft = []
		#for all area in map
		for area in areasDict:
			#for all vertex in vertexDict creat a node for the tree
			for vertex in areasDict[area].verticesDict:
				#creat node like {"vertex":,"area":}
				node = {}
				node["vertex"] = areasDict[area].verticesDict[vertex].verticeName
				node["area"] = areasDict[area].areaName
				self.nodesList.append(node)
			#for all street in streetDict creat an arc for the tree
			for street in areasDict[area].streetsDict:
				#creat arc like {"node1":,"node2":,"weight"}
				arc = {}
				nodeFrom = {}
				nodeTo = {}
				#creat node1 like {"vertex":,"area":}
				nodeFrom["vertex"] = areasDict[area].streetsDict[street].streetVertices[0].verticeName
				nodeFrom["area"] = areasDict[area].areaName
				arc["node1"] = nodeFrom
				#creat node2 like {"vertex":,"area":}
				nodeTo["vertex"] = areasDict[area].streetsDict[street].streetVertices[1].verticeName
				nodeTo["area"] = areasDict[area].areaName
				arc["node2"] = nodeTo
				arc["weight"] = areasDict[area].streetsDict[street].WeightCalculation()
				self.arcsList.append(arc)
			#for all bridge in bridgeList creat an arc for the tree
			for bridge in areasDict[area].bridgesList:
				#creat arc like {"node1":,"node2":,"weight"}
				arc = {}
				nodeFrom = {}
				nodeTo = {}
				#creat node1 like {"vertex":,"area":}
				nodeFrom["vertex"] = bridge.bridgeVertice.verticeName
				nodeFrom["area"] = areasDict[area].areaName
				arc["node1"] = nodeFrom
				#creat node2 like {"vertex":,"area":}
				nodeTo["vertex"] = bridge.bridgeToVertex
				nodeTo["area"] = bridge.bridgeToArea
				arc["node2"] = nodeTo
				arc["weight"] = bridge.bridgeWeight
				self.arcsList.append(arc)

	##	Initiallise all variable to start the dijsktra algorithm
	#	@param startPoint first point of the path
	def initDijkstra(self, startPoint):
		#Reset all variable each dijkstra
		self.distNode = []
		self.prevNode = []
		self.nodeLeft = []
		#for all node in nodesLIst
		for node in self.nodesList:
			#creat a node dist like {"vertex":,"area":,"dist":}
			distNode = {}
			distNode["vertex"] = node["vertex"]
			distNode["area"] = node["area"]
			#if node == startPoint dist = 0
			if node["vertex"] == startPoint["vertex"] and node["area"] == startPoint["area"]:
				distNode["dist"] = 0
			#if node != startPoint dist = 
			else:
				distNode["dist"] = -1
			self.distNode.append(distNode)
			#add each node in node leftDict
			self.nodeLeft.append(node)
			#add a prevNodeCOuple like {"node":,"prevNode:"} in prevNodeList
			prevNodeCouple = {}
			prevNode = {}
			#creat prevNode like {"vertex":,"area":}
			prevNode["vertex"] = startPoint["vertex"]
			prevNode["area"] = startPoint["area"]
			prevNodeCouple["prevNode"] = prevNode
			#creat node like {"vertex":,"area":}
			prevNodeCouple["node"] = node
			self.prevNode.append(prevNodeCouple)

	##	Initiallise all variable to start the dijsktra algorithm
	#	@param startPoint first point of the path
	#	@param endPoint final point of the path
	def findShortestPath(self, startPoint, endPoint):
		#bool for stop the algorithm if the end point is found
		endIsFound = False
		#current node choose for the algorithm
		currentNode = {}
		#path with all node to parcour to go from startPoint to endPoint
		finalPath = []
		#initialise of variable to execute dijkstra -1 == the infinit
		currentWeight = -1
		nodeListId = -1
		cmptListId = -1
		finalWeight = {}
		#init all dictionary to execute dijkstra
		self.initDijkstra(startPoint)
		#while there are a point to test and the end point is not find
		while len(self.nodeLeft) > 0 and endIsFound == False:
			#for each node left in node left list find the nearest node of the current node
			for nodeLeft in self.nodeLeft:
				#increment the compteur list to find the index of the current point in list left
				cmptListId = cmptListId + 1
				#for each node in distNode list try to find nodeLeft
				for node in self.distNode:
					#if node == nodeLeft
					if node["area"] == nodeLeft["area"] and node["vertex"] == nodeLeft["vertex"]:
						#if node dist to the start point is the lower and currentWeight is higher than -1
						if (node["dist"] < currentWeight and node["dist"] >= 0) or currentWeight < 0:
							#current weight take the lower weight
							currentWeight = node["dist"]
							#final weight take the lower weight
							finalWeight["totalWeight"] = currentWeight
							#currentNode take the node
							currentNode["vertex"] = node["vertex"]
							currentNode["area"] = node["area"]
							nodeIndex = cmptListId
			#if the current node is the final node stop the algorithm
			if currentNode["vertex"] == endPoint["vertex"] and currentNode["area"] == endPoint["area"]:
				endIsFound = True
			#if the end node is not find continu the algorithm
			if endIsFound == False:
				#foreach arc in the arc List try to find the current node
				for arc in self.arcsList:
					node1 = {}
					node1 = arc["node1"]
					node2 = {}
					node2 = arc["node2"]
					#if the current node is node1
					if node1["area"] == currentNode["area"] and node1["vertex"] == currentNode["vertex"]:
						#for each node in node Dist find node2 of the arc
						for node in self.distNode:
							#if node == node2
							if node2["area"] == node["area"] and node2["vertex"] == node["vertex"]:
								#calculate the actual 
								tmpDist = arc["weight"]
								tmpDist = tmpDist + currentWeight
								if tmpDist < node["dist"] or node["dist"] < 0:
									node["dist"] = tmpDist
									for prevNodeCouple in self.prevNode:
										nodeToUpdate = {}
										nodeToUpdate = prevNodeCouple["node"]
										if nodeToUpdate["vertex"] == node2["vertex"] and nodeToUpdate["area"] == node2["area"]:
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
											prevNode = prevNodeCouple["prevNode"]
											prevNode["area"] = currentNode["area"]
											prevNode["vertex"] = currentNode["vertex"]
			#reset variable to do an other turn of dijkstra
			currentWeight = -1
			#remove the current node to the node left list to not rechoose him
			self.nodeLeft.pop(nodeIndex)
			cmptListId = -1
		#end while len(self.nodeLeft) > 0 and endIsFound == False:

		finalPath = self.findPath(endPoint)
		finalPath.insert(0, finalWeight)
		print finalPath
		return finalPath

	##	Initiallise all variable to start the dijsktra algorithm
	#	@param endPoint final point of the path
	def findPath(self, endPoint):
		#ordoned list of node
		finalPath = []
		currentNode = {}
		#current node to find
		currentNode = endPoint
		#bool for stop the path creation
		isPathFinding = False
		#while the path finding is not finish
		while isPathFinding == False:
			isFind = False
			#for all prevNodeCouple find the current vertex
			for prevNodeCouple in self.prevNode:
				node = {}
				node = prevNodeCouple["node"]
				prevNode = {}
				prevNode = prevNodeCouple["prevNode"]
				#if node in prevNodeCouple == currentNode
				if node["vertex"] == currentNode["vertex"] and node["area"] ==  currentNode["area"] and isFind == False:
					#isFind == true because a vetex was find
					isFind = True
					#add the node to the first place of the list
					nodeToAdd = {}
					nodeToAdd["vertex"] = currentNode["vertex"]
					nodeToAdd["area"] = currentNode["area"]
					finalPath.insert(0, nodeToAdd)
					#current node take the prevNode to found his prevent node
					currentNode = prevNode
					#if the node find have the same prevNode as him stop the while
					if node["vertex"] == prevNode["vertex"] and node["area"] ==  prevNode["area"]:
						isPathFinding = True
		return finalPath
