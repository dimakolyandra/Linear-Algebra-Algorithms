package cm.linearalgebra.classes;

public abstract class OpenResult {
	public static void launch(String file){
		try{
			Process proc = Runtime.getRuntime().exec("notepad.exe "+ file);
			proc.waitFor();
		}catch(Exception ex){
			System.out.println("Can not open file");
		}	
	}
}
