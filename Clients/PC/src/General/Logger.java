package General;


public class Logger {
	public enum Logger_type {ERROR,SUCCESS}
	
	public static void log(Logger_type type,String className,String content){
		String message = "[" + className + "] " + content;
		if(type.equals(Logger_type.ERROR))
			System.err.println(message);
		else
			System.out.println(message);
	}
}
