import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class HuffmanEncoder {
	HashMap<Character, StringBuffer> map = new HashMap<Character, StringBuffer>(128);
	
	public void encodeFile(String inputFile, String codeFile) {
		readInCodes(codeFile);
		
		writeEncodedFile(inputFile);
	}
	
	//reads codesFile and inputs all codes as <Character, StringBuffer> entries in class variable map
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
				
				map.put(character, code);
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
	
	private void writeEncodedFile(String fileName) {
		File inputFile = new File(fileName);
		FileReader fr;
		BufferedReader br = null;
		
		File outputFile = new File(fileName + ".huf");
		FileWriter fw;
		BufferedWriter bw = null;
		
		//prep for reading & writing
		try {
			fr = new FileReader(inputFile);
			br = new BufferedReader(fr);
			
			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		StringBuffer bitLine = new StringBuffer();
		int leftoverBitCount = 0;
		
		try {
			while(br.ready()) {
				//look up each .read()'s map entry to retrieve its code, then add it to the line of bits
				bitLine.append(map.get((char) br.read()));
				
				//writing (whenever possible) bitLine encoded as chars
				//(avoids memory overflow)
				while(bitLine.length()/8 > 1) {
					try {
						//take 8 bits at a time
						//convert the byte to a base 10 int
						//convert that int to its char representation
						//delete the 8 converted bits from bitLine
						bw.write((char) Integer.parseInt(bitLine.substring(0, 8).toString(), 2));
						bitLine.delete(0, 8);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//making sure (remaining) total number of outputted bits is divisible by 8
		while(bitLine.length() % 8 != 0) {
			bitLine.append("0");
			leftoverBitCount++;
		}
		
		//writing remaining bitLine encoded as chars
		while(bitLine.length() > 0) {
			try {
				bw.write((char) Integer.parseInt(bitLine.substring(0, 8).toString(), 2));
				bitLine.delete(0, 8);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		//printing char representation of how many 0s had to be tacked on
		try {
			bw.write((char) leftoverBitCount);
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
