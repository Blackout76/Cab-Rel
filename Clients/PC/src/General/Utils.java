package General;

import java.awt.Point;

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
}
