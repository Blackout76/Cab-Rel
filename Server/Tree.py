import MapManager

tree = None

class Tree:
	def __init__(self):
		self.noeuds = {}
		for area in MapManager.mapManager.areasDict:
			area = MapManager.mapManager.areasDict[area]
			for vertice in area.verticesDict:
				self.noeuds[area.verticesDict[vertice].verticeName]  = area.areaName + ":" + Noeud(area.verticesDict[vertice])
				print 'added noeud ' + area.verticesDict[vertice].verticeName
			
class Noeud:
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