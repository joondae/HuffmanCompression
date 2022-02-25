import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class HuffmanCodeGenerator {
	public void generateCodes(String frequencyFile, String codeFile) {
		//records the frequencies of every character in frequencyFile
		HashMap<Character, Integer> map = inputFreqs(frequencyFile);
		
		//creates binary tree with given character frequencies, "summed frequencies", and "zero frequencies"
		PriorityQueue<HuffmanNode> q = addToPQAndCreateBT(map);
		
		//makes codes and inputs them into arr in order
		StringBuffer[] codeList = makeCodes(q);
		
		//writes codeFile
		writeCodeFile(codeFile, codeList);
	}
	
	private HashMap<Character, Integer> inputFreqs(String fileName) {
		//Prep to read the frequency file
		File freqFile = new File(fileName);
		FileReader fr;
		BufferedReader br = null;
		
		//FileNotFoundException is included in IOException (former inherits the latter)
		try {
			fr = new FileReader(freqFile);
			br = new BufferedReader(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Java's HashMap already takes care of load factor
		//size must be 128 to include all possible ASCII values
		HashMap<Character, Integer> map = new HashMap<Character, Integer>(128);
		
		//put entire ASCII Table as entries in HashMap
		for(int i = 0; i < 128; i++) {
			map.put((char) i, 0);
		}
		
		//when a ASCII character is read from frequency file, increment its entry's frequency by 1
		try {
			while(br.ready()) {
				char character = (char) br.read();
				map.replace(character, map.get(character) + 1);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//post prep for reading
		try {
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	private PriorityQueue<HuffmanNode> addToPQAndCreateBT(HashMap<Character, Integer> m) {
		//enter constructor arg's
			//don't need to assign a comparator, just implement in HuffmanNode class and override the compareTo method
			//***Can you use the collection argument for the PriorityQueue constructor to somehow make the method more efficient?
		PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(128);
		
		//transfer HashMap data (128 entries) into 128 HuffmanNodes & add them into the priority queue
		//(will assign lt and rt when making binary tree)
		//all HuffmanNodes will be organized in ascending order (HuffmanNode with 0 or minimum frequency will be first)
		for(Map.Entry<Character, Integer> entry : m.entrySet()) {
			HuffmanNode node = new HuffmanNode(entry.getKey(), entry.getValue(), null, null);
			
			q.add(node);
		}
		
		//take 2 HuffmanNodes at a time, link them with a newly created HuffmanNode (the summed frequency node)
		//then keep cycling through the priority queue until the entire binary tree has been created
		//NOTE: either/both of the 2 HuffmanNodes taken may be a "zero freq node" (its character didn't appear in the frequency file)
		while(q.size() > 1) {
			HuffmanNode x = q.peek();
			q.poll();
					
			HuffmanNode y = q.peek();
			q.poll();
					
			//store -1 for the character class var b/c this node is a "summed freq node"
			//(ASCII values (not extended) go from 0-127)
			//chose -1 to not confuse w/ the assignment of -2 in method makeCodes()
			HuffmanNode freqNode = new HuffmanNode(-1, x.data + y.data, x, y);
			
			q.add(freqNode);
		}
		
		//at this point .peek() points to the root of the binary tree
		//the rest of the binary tree is "hidden" underneath the root since its connected by lt's and rt's only,
		//not by any pointer in the priority queue
		return q;
	}
	
	private StringBuffer[] makeCodes(PriorityQueue<HuffmanNode> bTree) {
		StringBuffer[] codeList = new StringBuffer[128];
		
		//traverse the tree & add each code to codeList
		Stack<HuffmanNode> s = new Stack<HuffmanNode>();
		//initially, curr points to the root of the tree (see comment above return statement in addToPQandCreateBT method)
		HuffmanNode curr = bTree.peek();
		
		StringBuffer codeBuild = new StringBuffer();
		
		//1st condition means you've traversed to the end of a "branch"
		//2nd condition means you've traversed the entire tree
		while(curr != null || s.size() > 0) {
			
			
			//again, assigning negative number b/c they don't exist in ASCII table
			int chUnicode = -2;
			
			//watch order of statements
			while(curr != null) {
				//when you traverse to a character, add its code to the array
				if(curr.character >= 0) {
					chUnicode = curr.character;
					codeList[chUnicode] = new StringBuffer(codeBuild);
				}
				
				s.push(curr);
				//does changing curr pointer also change chUnicode pointer???
				curr = curr.right;
				//you've traversed to the right, so add chosen number (1 or 0) to the code
				codeBuild.append("0");
			}
			
			curr = s.pop();
			//must delete the last "digit" from the code b/c you're "backtracking" up the tree
			codeBuild.delete(codeBuild.length() - 1, codeBuild.length());
			
			if(curr.character >= 0) {
				chUnicode = curr.character;
				codeList[chUnicode] = new StringBuffer(codeBuild);
			}
			
			curr = curr.left;
			//you've traversed to the left, so add chosen number (1 or 0) to the code
			codeBuild.append("1");
		}
		
		return codeList;
	}
	
	private void writeCodeFile(String fileName, StringBuffer[] codeList) {
		//prep to write the codes in the given file (specified by name)
		File codeFile = new File(fileName);
		FileWriter fw;
		BufferedWriter bw = null;
		
		//FileNotFoundException is included in IOException (former inherits the latter)
		try {
			fw = new FileWriter(codeFile);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			//add all codes to the new txt file in order
			//(1st line of file is 0th ASCII character's code, etc.)
			for(int i = 0; i < 128; i++) {
				//expensive to use escape sequence???
				//note: codeList[i] is getting automatically converted from StringBuffer to String
				//bw.write((char) i + " "); //***FOR DEBUGGING ONLY
				bw.write(codeList[i] + "\n");
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//post prep for writing
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
