package cm.linearalgebra.classes;

/** Class that create new process for opening text file*/
public abstract class OpenResult {
	
	/** Launch new process */
	public static void launch(String file){
		try{
			Process proc = Runtime.getRuntime().exec("notepad.exe "+ file);
			proc.waitFor();
		}catch(Exception ex){
			System.out.println("Can not open file");
		}	
	}
}
