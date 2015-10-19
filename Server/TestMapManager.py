#! /usr/bin/env python

from MapManager import *
from TaxiManager import *


mapLoaded = MapManager("mapTest")



if __name__ == '__main__':
	taxi = TaxiManager(mapLoaded)
	mapLoaded.loadFileMap()
	mapLoaded.toDictFormat()
	taxi.newTaxi()