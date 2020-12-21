package mainPart;

import java.util.*;
import java.io.*;

public class VocaManager {
	
	private String userName;
	private String filePath;
	private int count=0;
	public Map<Integer,Word> words = new HashMap<>();
	
	public VocaManager(String userName,String filePath) {
		this.userName=userName;
		this.filePath=filePath;
		readTXT();
	}
	
	public void readTXT() {
		try {
			Scanner scanner = new Scanner(new File(filePath));
			while(scanner.hasNext()) {
				String temp_line = scanner.nextLine();
				String[] temp = temp_line.split("\t");
				Word word = new Word(temp[0],temp[1]);
				words.put(count,word);
				count++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("파일이 존재하지 않습니다.");
		}
	}

	public Map<Integer,Word> getWords() {
		return words;
	}

	

	
}
