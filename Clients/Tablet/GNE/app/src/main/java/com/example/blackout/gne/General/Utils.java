package com.example.blackout.gne.General;

import java.util.HashMap;


public class Utils {

	public static double computeAngleOrient(CPoint A, CPoint B, CPoint C){
		/*
		 * Compute oriented angle between vector(AB) and vector (AC)
		 */
		double vector_AB_x = B.getX() - A.getX();
		double vector_AB_y = B.getY() - A.getY();
		double vector_AC_x = C.getX() - A.getX();
		double vector_AC_y = C.getY() - A.getY();

		double norme_AB = Math.sqrt(Math.pow(vector_AB_x, 2)+Math.pow(vector_AB_y, 2));
		double norme_AC = Math.sqrt(Math.pow(vector_AC_x, 2)+Math.pow(vector_AC_y, 2));

		double scal_cl_cg = vector_AB_x * vector_AC_x + vector_AB_y * vector_AC_y;
		double angle_orient = Math.acos((scal_cl_cg)/(norme_AB*norme_AC)) *  (180/Math.PI);
		return angle_orient;
	}

	public static double computeHeightOfTriangle(CPoint A, CPoint B, CPoint C){
		/*
		 * Compute height of Triangle
		 */
		double angle = computeAngleOrient(A,B,C);
		double norme_AC = Math.sqrt(Math.pow(C.getX() - A.getX(), 2)+Math.pow(C.getY() - A.getY(), 2));
		double height = Math.sin(angle*(Math.PI/180))*norme_AC;
		return height;
	}
	public static double computeDistOfHeightTriangle(CPoint A, CPoint B, CPoint C){
		/*
		 * Compute distance between A and the height of Triangle
		 */
		double angle = computeAngleOrient(A,B,C);
		double norme_AC = Math.sqrt(Math.pow(C.getX() - A.getX(), 2)+Math.pow(C.getY() - A.getY(), 2));
		double dist = Math.cos(angle*(Math.PI/180))*norme_AC;
		
		return dist;
	}
	public static HashMap<String, Object> computeInfoOfTriangle(CPoint A, CPoint B, CPoint C){
		HashMap<String, Object> result = new HashMap<>();
		double norme_AB = Math.sqrt(Math.pow(B.getX() - A.getX(), 2)+Math.pow(B.getY() - A.getY(), 2));
		result.put("height", computeHeightOfTriangle(A,B,C));
		result.put("distHeight", computeDistOfHeightTriangle(A,B,C));
		result.put("pourcentHeight", ((double)result.get("distHeight")) / (norme_AB) );
		result.put("pointIntercept", computePointOfHeightIntercept((Double) result.get("distHeight"),A,B,C));
		
		/* Inverse origin point on street if %>50% */
		if((double)result.get("pourcentHeight") > 0.5){
			result.put("pourcentHeight", 1-((double)result.get("pourcentHeight")) );
			if( (int)((HashMap<String, Object>)result.get("pointIntercept")).get("indexVertice") == 1)
				((HashMap<String, Object>)result.get("pointIntercept")).put("indexVertice", 0);
			else
				((HashMap<String, Object>)result.get("pointIntercept")).put("indexVertice", 1);
		}
			
		return result;
	}

	private static HashMap<String, Object> computePointOfHeightIntercept(Double distHeight,CPoint A, CPoint B, CPoint C) {
		HashMap<String, Object> result = new HashMap<>();
		double angle_absis = computeAngleOrient(A,B,new CPoint( (int)(A.getX()+50) , (int) A.getY() ));
		double norme_AB = Math.sqrt(Math.pow(B.getX() - A.getX(), 2)+Math.pow(B.getY() - A.getY(), 2));
		if(distHeight <= 0){
			result.put("x", (double) A.getX());
			result.put("y", (double) A.getY());
			double norme_AC = Math.sqrt(Math.pow(C.getX() - A.getX(), 2)+Math.pow(C.getY() - A.getY(), 2));
			result.put("distanceToPoint", norme_AC);
			result.put("indexVertice",  0);
		}
		else if(distHeight >= norme_AB){
			result.put("x", (double) B.getX());
			result.put("y", (double) B.getY());
			double norme_BC = Math.sqrt(Math.pow(C.getX() - B.getX(), 2)+Math.pow(C.getY() - B.getY(), 2));
			result.put("distanceToPoint", norme_BC);
			result.put("indexVertice",  1);
		}
		else{
			result.put("x" , A.getX() + Math.cos(angle_absis*(Math.PI/180))*distHeight);
			if(A.getY() >= B.getY()) 
				result.put("y" , A.getY() - Math.sin(angle_absis*(Math.PI/180))*distHeight);
			else
				result.put("y" , A.getY() + Math.sin(angle_absis*(Math.PI/180))*distHeight);
			result.put("indexVertice",  0);
		}
		return result;
	}
	
}
