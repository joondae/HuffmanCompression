
public class HuffmanCompressionTester {
	public static void main(String [] args) {
		//STOUT'S TESTS
		HuffmanCodeGenerator g = new HuffmanCodeGenerator();
		g.generateCodes("frequencyCountInput.txt", "CodeFile.txt");
		
		
		HuffmanEncoder e = new HuffmanEncoder();
		e.encodeFile("fileToCompress.txt", "CodeFile.txt");
		
		
		
		HuffmanDecoder d = new HuffmanDecoder();
		d.decodeFile("fileToCompress.txt.huf", "CodeFile.txt");
		
		
		
		DecodedFileChecker dFC = new DecodedFileChecker();
		System.out.println(dFC.checkAgainstOriginal("fileToCompress.txt", "fileToCompressOriginal.txt"));
		
		
		
		//MY TESTS
		/*
		HuffmanCodeGenerator g = new HuffmanCodeGenerator();
		g.generateCodes("FrequencyFile.txt", "CodeFile.txt");
		*/
		
		/*
		HuffmanEncoder e = new HuffmanEncoder();
		e.encodeFile("FileToEncode.txt", "CodeFile.txt");
		*/
		
		//***For some reason, FileToEncode.txt.huf becomes blank after the decoder reads it, then nothing gets written in the decoded file???
		
		//Test 1: Use encoder to encode something into the long line of bits; then comment encoder out and run decoder: the latter should decode the long line of bits back into the original text
		/*
		HuffmanDecoder d = new HuffmanDecoder();
		d.decodeFile("FileToEncode.txt.huf", "CodeFile.txt");
		*/
		
		//Test 2: Decoding custom (user) inputted bits
		/*
		HuffmanDecoder d = new HuffmanDecoder();
		d.decodeFile("FileToDecode.txt.huf", "CodeFile.txt");
		*/
		
		
	}
}
