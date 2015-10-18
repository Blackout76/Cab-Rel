package General;

import java.awt.Point;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class Utils {

	public static double computeAngleOrient(Point A, Point B, Point C){
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

	public static double computeHeightOfTriangle(Point A, Point B, Point C){
		/*
		 * Compute height of Triangle
		 */
		double angle = computeAngleOrient(A,B,C);
		double norme_AC = Math.sqrt(Math.pow(C.getX() - A.getX(), 2)+Math.pow(C.getY() - A.getY(), 2));
		double height = Math.sin(angle*(Math.PI/180))*norme_AC;
		return height;
	}
	public static double computeDistOfHeightTriangle(Point A, Point B, Point C){
		/*
		 * Compute distance between A and the height of Triangle
		 */
		double angle = computeAngleOrient(A,B,C);
		double norme_AC = Math.sqrt(Math.pow(C.getX() - A.getX(), 2)+Math.pow(C.getY() - A.getY(), 2));
		double dist = Math.cos(angle*(Math.PI/180))*norme_AC;
		
		return dist;
	}
	public static HashMap<String, Object> computeInfoOfTriangle(Point A, Point B, Point C){
		HashMap<String, Object> result = new HashMap<>();
		double norme_AB = Math.sqrt(Math.pow(B.getX() - A.getX(), 2)+Math.pow(B.getY() - A.getY(), 2));
		result.put("height", computeHeightOfTriangle(A,B,C));
		result.put("distHeight", computeDistOfHeightTriangle(A,B,C));
		result.put("pourcentHeight", ((double)result.get("distHeight")*100) / (norme_AB) );
		result.put("pointIntercept", computePointOfHeightIntercept((Double) result.get("distHeight"),A,B));
		return result;
	}

	private static HashMap<String, Double> computePointOfHeightIntercept(Double distHeight,Point A, Point B) {
		HashMap<String, Double> result = new HashMap<>();
		double angle_absis = computeAngleOrient(A,B,new Point( (int)(A.getX()+50) , (int) A.getY() ));
		result.put("x" , A.getX() + Math.cos(angle_absis*(Math.PI/180))*distHeight);
		result.put("y" , A.getY() + Math.cos(angle_absis*(Math.PI/180))*distHeight);
		return result;
	}
	
}
