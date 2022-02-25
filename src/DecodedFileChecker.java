import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DecodedFileChecker {
	
	public boolean checkAgainstOriginal(String decodedFileName, String originalFileName) {
		File decodedFile = new File(decodedFileName);
		FileReader dFR;
		BufferedReader dBR = null;
		
		File originalFile = new File(originalFileName);
		FileReader oFR;
		BufferedReader oBR = null;
		
		//prep for reading
		try {
			dFR = new FileReader(decodedFile);
			dBR = new BufferedReader(dFR);
			
			oFR = new FileReader(originalFile);
			oBR = new BufferedReader(oFR);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			while(dBR.ready() && oBR.ready()) {
				if(dBR.read() != oBR.read()) {
					return false;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//post prep for reading
		try {
			dBR.close();
			oBR.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
