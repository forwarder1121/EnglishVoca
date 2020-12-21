package mainPart;

public class InformationOfWords_incorrectNum implements Comparable<InformationOfWords_incorrectNum> {
	Word word;
	int numberOfWord = 0;
	int numberOfincorrect = 0;

	public InformationOfWords_incorrectNum(Word word, int numberOfWord, int numberOfincorrect) {
		super();
		this.word = word;
		this.numberOfWord = numberOfWord;
		this.numberOfincorrect = numberOfincorrect;
	}

	@Override
	public int compareTo(InformationOfWords_incorrectNum o) {
		// TODO Auto-generated method stub
		return o.numberOfincorrect - this.numberOfincorrect;
	}

	@Override
	public String toString() {
		String temp = "";
		if (numberOfincorrect > 0) {
			temp += "틀린 횟수 : " + numberOfincorrect + " 회 " + word.eng + " : " + word.kor + "\n";
		}
		return temp;
	}
}

