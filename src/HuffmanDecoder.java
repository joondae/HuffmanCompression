import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class HuffmanDecoder {
	HashMap<StringBuffer, Character> map = new HashMap<StringBuffer, Character>(128);
	
	public void decodeFile(String inputFile, String codeFile) {
		readInCodes(codeFile);
		
		writeDecodedFile(inputFile);
	}
	
	//reads codesFile and inputs all codes as <StringBuffer, Character> entries in class variable map
	//***NOTE: the types for the decoder's HashMap are flipped from encoder's HashMap
	private void readInCodes(String fileName) {
		File codeFile = new File(fileName);
		FileReader fr;
		BufferedReader br = null;
					
		//prep for reading
		try {
			fr = new FileReader(codeFile);
			br = new BufferedReader(fr);
		} catch(IOException e) {
			e.printStackTrace();
		}

		//lineCount is used to keep track of which character's code is being read
		int lineCount = 0;
			
		try {
			//create "code" instance variable, assign current character to "character" instance variable
			while(br.ready()) {
				StringBuffer code = new StringBuffer();
				char character = (char) lineCount;
				char c = (char) br.read();
					
				while(br.ready() && (c == '0' || c == '1')) {
					code.append(c);
					c = (char) br.read();
				}
					
				map.put(code, character);
				lineCount++;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}	
					
		//post prep for reading & writing
		try {
			br.close();
		} catch(IOException e) {
			e.printStackTrace();	
		}	
	}
	
	private void writeDecodedFile(String fileName) {
		File inFile = new File(fileName);
		FileReader fr;
		BufferedReader br = null;
		
		File outputFile = new File(fileName.substring(0, fileName.length() - 4) + ".huf");
		FileWriter fw;
		BufferedWriter bw = null;
		
		//prep for reading & writing
		try {
			fr = new FileReader(inFile);
			br = new BufferedReader(fr);
			
			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//Main purpose:
		//create bitLine ins var AND possibleCode ins var
		//possibleCode "lags behind" bitLine
		//constantly checking if possibleCode is a code contained w/in HashMap
			//if yes, write corresponding char and delete possibleCode from bitLine
			//if no, increment both bitLine and possibleCode by br.read() (converted to binary string)
		
		//Special case: <--***JANKY AF FIX LATER
		//also keeping track of how many continuous zeroes appear in bitLine
			//if the number of continuous zeroes equals the int representation of br.read(), you've reached the end of the encoded file
				//--> break out of all loops, close readers & writers, then return out of method
		//(similar to main purpose, zeroCount "lags behind" bits)
		
		StringBuffer bitLine = new StringBuffer();
		String bits = "";
		int zeroCount = 0;
		
		outerloop:
		try {
			while(br.ready()) {
				bits = Integer.toBinaryString(br.read());
				
				//***placement???
				if(Integer.parseInt(bits, 2) == zeroCount) {
					break outerloop;
				}
				
				bitLine.append(bits);
				StringBuffer possibleCode = new StringBuffer();
				
				//***placement???
				//note: to cut down on time, start counting zeroes from a place after the first appearance of a 1 in bitLine
				for(int i = bitLine.charAt(bitLine.indexOf("1") + 1); i < bitLine.length(); i++) {
					if(bitLine.charAt(i) == '1') {
						zeroCount = 0;
					}
					else {
						zeroCount++;
					}
				}
				
				while(br.ready() && map.get(possibleCode) == null) {
					possibleCode.append(bits);
					bits = Integer.toBinaryString(br.read());
					
					//***placement???
					if(Integer.parseInt(bits, 2) == zeroCount) {
						break outerloop;
					}
					
					bitLine.append(bits);
					
					//***placement???
					//note: to cut down on time, start counting zeroes from a place after the first appearance of a 1 in bitLine
					for(int i = bitLine.charAt(bitLine.indexOf("1") + 1); i < bitLine.length(); i++) {
						if(bitLine.charAt(i) == '1') {
							zeroCount = 0;
						}
						else {
							zeroCount++;
						}
					}
				}
				
				bitLine.delete(0, possibleCode.length());
				bw.write(map.get(possibleCode));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//post prep for reading & writing
		try {
			br.close();
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
