package mainPart;

public class Word {
	String eng;
	String kor;
	
	public Word(String eng,String kor) {
		super();
		this.eng=eng;
		this.kor=kor;
	}

	@Override
	public String toString() {
		return eng + " : " + kor + "\n";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Word) {
			Word temp = (Word)obj;
			if(this.eng.equals(temp.eng)&&this.kor.equals(temp.kor)) {
				return true;
			} else return false;
		}else return false;
		
	}
	
	
}
