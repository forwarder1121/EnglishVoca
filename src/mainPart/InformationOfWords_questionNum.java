package mainPart;

public class InformationOfWords_questionNum implements Comparable <InformationOfWords_questionNum>{
	
	Word word;
	int numberOfWord=0;
	int numberOfQuestion=0;
	
	public InformationOfWords_questionNum(Word word, int numberOfWord,int numberOfQuestion) {
		super();
		this.word = word;
		this.numberOfWord=numberOfWord;
		this.numberOfQuestion = numberOfQuestion;
	}


	@Override
	public int compareTo(InformationOfWords_questionNum o) {
		// TODO Auto-generated method stub
		return o.numberOfQuestion-this.numberOfQuestion;
	}


	@Override
	public String toString() {
		String temp = "";
		if(numberOfQuestion>0) {
			temp+= "출제 수 : "+numberOfQuestion+" 회 " +word.eng + " : " + word.kor + "\n";
		} 
		return temp;
	}
	
	
	
	
}
