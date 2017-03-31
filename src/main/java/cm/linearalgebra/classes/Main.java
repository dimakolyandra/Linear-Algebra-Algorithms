package cm.linearalgebra.classes;


import org.springframework.context.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import cm.linearalgebra.interfaces.LinearAlgebraAlgorithm;

public class Main {
	
	private static String fileWithInputData;
	private static String cmd;
	private static LinearAlgebraAlgorithm algo;
	
	public static String getFileWithInputData() {
		return fileWithInputData;
	}

	public static void setFileWithInputData(String fileWithInputData) {
		Main.fileWithInputData = fileWithInputData;
	}

	public static String getCmd() {
		return cmd;
	}

	public static void setCmd(String cmd) {
		Main.cmd = cmd;
	}
	private static void menu(){
		System.out.println(" -sw --- Sweep Method");
		System.out.println(" -ze --- Zeidel Method");
		System.out.println(" -sp --- Spins Method");
		System.out.println(" -si --- Simple Iteration Method");
		System.out.println(" -qr --- QR Method, only  for 3x3 matrix");
		System.out.println(" -ga --- Gauss Method");
	}
	
	public static void main(String [] args){
		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");	
		if(args.length==0){
			System.out.println("Wrong arguments: enter -h");
			return;
		}
		else if(args.length == 1){
			cmd = args[0];	
			if(cmd.equals("-h")){
				menu();
				return;
			}
		}
		else if(args.length == 2){
			cmd = args[0];
			fileWithInputData = args[1];
			if(cmd.equals("-sw")){
				algo = (SweepMethod) context.getBean("SweepMeth");
			}
			if(cmd.equals("-ze")){
				algo = (ZeidelMethod) context.getBean("ZeidelMeth");		
			}
			if(cmd.equals("-sp")){
				algo = (SpinsMethod) context.getBean("SpinsMeth");				
			}
			if(cmd.equals("-si")){
				algo = (SimpleIterationMethod) context.getBean("SimpleIterationMeth");					
			}
			if(cmd.equals("-qr")){
				algo = (QR) context.getBean("qrMeth");
			}
			if(cmd.equals("-ga")){
				System.out.println("!!");
				algo = (GaussAlgorithm) context.getBean("GaussMeth");		
			}
			algo.algorithm();			
		}
	}
}

