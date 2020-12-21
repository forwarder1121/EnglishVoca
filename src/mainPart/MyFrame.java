package mainPart;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyFrame extends JFrame {

	Container contentPane = this.getContentPane();
	JTextPane centerTextPane = new JTextPane();
	JScrollPane centerScrollPane = new JScrollPane(centerTextPane);
	Map<Integer, Word> words;
	ArrayList<Word> temp_incorrect_words = new ArrayList<>();
	ArrayList<Integer> deleted_keys;
	ArrayList<Word> temp_temp_incorrect_wordsOfIncorrect = new ArrayList<>();
	ArrayList<Word> temp_temp_correct_wordsOfIncorrect = new ArrayList<>();
	ArrayList<InformationOfWords_questionNum> total_informationOfWords_questionNum = new ArrayList<>();
	ArrayList<InformationOfWords_incorrectNum> total_informationOfWords_incorrectNum = new ArrayList<>();
	Stack<Word> searchedWords = new Stack<>();

	static String filePath = "quiz.txt";

	Dialog_searchKorWord dialog_searchKorWord;
	Dialog_searchEngWord dialog_searchEngWord;
	Dialog_addWord dialog_addWord;
	Dialog_deleteWord dialog_deleteWord;
	Dialog_game dialog_game;
	
	boolean quiz_nextStage=false;
	int Number_question=1;
	
	int Number_question_incorrectVersion=0;
	int Number_correct = 0;
	int Number_incorrect = 0;
	int searchWord_count=0;
	
	boolean hasIncorrect =false ;//오답노트 가지고 있는지의 유뮤
	
	
	JButton btn_incorrectQuiz ;
	ArrayList<Word> finalList;
	
	
	MyFrame() {
		setTitle("영어 단어장 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		VocaManager vocaManager = new VocaManager("김동환", filePath);
		words = vocaManager.getWords();
		for(int i=0;i<words.size();i++) {
			InformationOfWords_questionNum informationOfWords = new InformationOfWords_questionNum(words.get(i),i,0);
			total_informationOfWords_questionNum.add(informationOfWords);
			InformationOfWords_incorrectNum temp_informationOfWords_incorrectNum = new InformationOfWords_incorrectNum(words.get(i),i,0);
			total_informationOfWords_incorrectNum.add(temp_informationOfWords_incorrectNum);
		}
		contentPane.setLayout(new BorderLayout());

		centerTextPane.setEditable(false);
		centerTextPane.setText("메뉴를 누르세요 ...");
		contentPane.add(centerScrollPane, BorderLayout.CENTER);

		dialog_searchKorWord = new Dialog_searchKorWord(this, "한국어 뜻으로 찾기");
		dialog_searchEngWord = new Dialog_searchEngWord(this, "영어로 찾기");
		dialog_addWord = new Dialog_addWord(this,"단어 추가하기");
		dialog_deleteWord = new Dialog_deleteWord(this,"단어 삭제하기");
		
		deleted_keys = new ArrayList<>();
		createMenu();
		makeButtonAndAttach();

		setSize(500, 300);
		setVisible(true);

	}

	// 버튼 메소드
	// 이렇게 생성된 버튼은 EAST의 패널에 붙는다.
	void makeButtonAndAttach() {
		// 버튼을 생성한다.
		JPanel panel_btn = new JPanel(new GridLayout(6, 1, 5, 5));

		JButton btn_showwords = new JButton("단어 보기");
		JButton btn_searchKorWord = new JButton("한국어로 검색");
		JButton btn_searchEngWord = new JButton("영어로 검색");
		JButton btn_showquiz = new JButton("객관식 퀴즈");
		btn_incorrectQuiz = new JButton("오답 퀴즈");
		
		JButton btn_playGame = new JButton("게임시작");

		// 리스너를 달아준다.
		centerTextPane.addKeyListener(new KeyListener_f5_button_to_incorrectQuiz_Unlock());
		
		btn_playGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_game=new Dialog_game(MyFrame.this,centerTextPane,"게임하기",words,deleted_keys);
				//dialog_game.setVisible(true);
				
			}
			
		});
		
		btn_showwords.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String information_words = "";
				information_words += "파일 이름 : " + filePath + "\n\n";
				for (int i = 0; i < words.size(); i++) {
					if(deleted_keys.indexOf(i)==-1) {
						information_words += words.get(i).eng + "\t\t" + words.get(i).kor + "\n";
					}
					
				}
				information_words +="*새로고침은 F5";
				centerTextPane.setText(information_words);
				centerTextPane.setCaretPosition(0);
				
			}

		});

		btn_searchKorWord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_searchKorWord.setVisible(true);

			}

		});

		btn_searchEngWord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_searchEngWord.setVisible(true);
			}

		});

		btn_showquiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(words.size()>=15) {
					Number_question=1;
					Number_correct=0;
					Number_incorrect=0;
					Dialog_btn_showquiz dialog_btn_showquiz = new Dialog_btn_showquiz(MyFrame.this,"단어퀴즈"); //모달 다이얼로그임
					dialog_btn_showquiz.readyForQuestion();
					dialog_btn_showquiz.setVisible(true); //이게 끝날때까지 리턴하지않음 //멈춰있음
				} else {
					JOptionPane.showMessageDialog(contentPane, "단어장의 단어 개수가 15개 이상이여야 합니다.","Warning",JOptionPane.ERROR_MESSAGE);
				}
				
			
				
				
			}

		});
		
		btn_incorrectQuiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				Number_question=1;
				Number_correct=0;
				Number_incorrect=0;
				Number_question_incorrectVersion=0;
				Dialog_btn_showquiz_incorrectVersion dialog_btn_showquiz_incorrectVersion = new Dialog_btn_showquiz_incorrectVersion(MyFrame.this,"오답퀴즈");
				
				dialog_btn_showquiz_incorrectVersion.readyForQuestion();
				
				dialog_btn_showquiz_incorrectVersion.setVisible(true);
			}
			
		});
		
		

		// 패널에 부착한다.
		panel_btn.add(btn_showwords);
		panel_btn.add(btn_searchKorWord);
		panel_btn.add(btn_searchEngWord);
		panel_btn.add(btn_showquiz);
		panel_btn.add(btn_incorrectQuiz);
		btn_incorrectQuiz.setEnabled(false);
		panel_btn.add(btn_playGame);

		contentPane.add(panel_btn, BorderLayout.EAST);

	}

	// 메뉴 생성 메소드
	void createMenu() {
		JMenuBar mb = new JMenuBar();

		// file 영역 생성
		JMenu fileMenu = new JMenu("File");
		JMenu wordMenu = new JMenu("Word");
		JMenu statisticsMenu = new JMenu("Statistics");
		
		JMenuItem fileMenu_view = new JMenuItem("View Current-file Information");
		JMenuItem fileMenu_changeFile = new JMenuItem("Change Vocalist With New File");
		JMenuItem wordMenu_addWord = new JMenuItem("Add Word");
		JMenuItem wordMenu_deleteWord = new JMenuItem("Delete Word");
		JMenuItem statisticsMenu_frequencyList= new JMenuItem("Show Frequency Occurence List");
		JMenuItem statisticsMenu_latelySearchList = new JMenuItem("Show Lately Search Word List");
		JMenuItem statisticsMenu_incorrectWordList = new JMenuItem("Show Wrong Answer Note");
		
		
		wordMenu.add(wordMenu_addWord);
		wordMenu.add(wordMenu_deleteWord);
		fileMenu.add(fileMenu_view);
		fileMenu.add(fileMenu_changeFile);
		statisticsMenu.add(statisticsMenu_frequencyList);
		statisticsMenu.add(statisticsMenu_latelySearchList);
		statisticsMenu.add(statisticsMenu_incorrectWordList);
		
		wordMenu_deleteWord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_deleteWord.setVisible(true);
			}
			
		});
		
		statisticsMenu_incorrectWordList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String temp_string = "오답 노트\n\n";
				Collections.sort(total_informationOfWords_incorrectNum);
				for(int i=0;i<total_informationOfWords_incorrectNum.size();i++) {
					temp_string+=total_informationOfWords_incorrectNum.get(i).toString();
				}
				centerTextPane.setText(temp_string);
				centerTextPane.setCaretPosition(0);
			}
			
		});
		
		fileMenu_view.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File objectFile = new File(filePath);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a HH:mm");
				String information_file = "";
				information_file += "현재 파일 이름 : " + filePath + "\n";
				information_file += "숨김 파일  여부 : " + objectFile.isHidden() + "\n" + "수정 가능 여부 : "
						+ objectFile.canWrite() + "\n마지막 수정 : " + sdf.format(new Date(objectFile.lastModified()));
				information_file += "\n파일 크기 : " + objectFile.length() + " bytes";
				centerTextPane.setText(information_file);
			}

		});
		
		statisticsMenu_frequencyList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String temp_string = "빈출단어 리스트\n\n";
				Collections.sort(total_informationOfWords_questionNum);
				for(int i=0;i<total_informationOfWords_questionNum.size();i++) {
					temp_string+=total_informationOfWords_questionNum.get(i).toString();
				}
				centerTextPane.setText(temp_string);
				centerTextPane.setCaretPosition(0);
			}
			
		});
		
		wordMenu_addWord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_addWord.setVisible(true);
			}
			
		});

		statisticsMenu_latelySearchList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Stack<Word> temp= new Stack<>();
				temp.addAll(searchedWords);
				String str = "최근 검색한 단어\n\n";
				for(int i=0;i<searchWord_count;i++) {
					if(i<7) {
						str+=temp.pop();
					}
				}
				centerTextPane.setText(str);
				centerTextPane.setCaretPosition(0);
			}
			
		});
		
		fileMenu_changeFile.addActionListener(new ActionListener_fileMenu_changeFile());
		mb.add(fileMenu);
		mb.add(wordMenu);
		mb.add(statisticsMenu);
		this.setJMenuBar(mb);

	}

	// 이벤트 리스너 클래스
	class ActionListener_fileMenu_changeFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
			chooser.setFileFilter(filter);
			int ret = chooser.showOpenDialog(null);
			if (ret == JFileChooser.APPROVE_OPTION) {
				String pathName = chooser.getSelectedFile().getPath();
				MyFrame.filePath = pathName;
				words.clear();
				deleted_keys.clear();
				centerTextPane.setText("파일명 : " + filePath + "\n단어장이 새로 생성되었습니다!\n확인하세요\n\n*단어퀴즈는 단어들이 15개 이상일때 가능합니다.");
				VocaManager vocaManager = new VocaManager("김동환", filePath);
				words = vocaManager.getWords();
				for(int i=0;i<words.size();i++) {
					InformationOfWords_questionNum temp_informationOfWords_questionNum = new InformationOfWords_questionNum(words.get(i),i,0);
					total_informationOfWords_questionNum.add(temp_informationOfWords_questionNum);
					InformationOfWords_incorrectNum temp_informationOfWords_incorrectNum = new InformationOfWords_incorrectNum(words.get(i),i,0);
					total_informationOfWords_incorrectNum.add(temp_informationOfWords_incorrectNum);
				}
			}
		}

	}

	class KeyListener_f5_button_to_incorrectQuiz_Unlock extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			super.keyPressed(e);
			int keyCode = e.getKeyCode();
			if(keyCode==KeyEvent.VK_F5) {
				if(hasIncorrect==true) {
					btn_incorrectQuiz.setEnabled(true);
				}
				String information_words = "";
				information_words += "파일 이름 : " + filePath + "\n\n";
				for (int i = 0; i < words.size(); i++) {
					if(deleted_keys.indexOf(i)==-1) {
						information_words += words.get(i).eng + "\t\t" + words.get(i).kor + "\n";
					}
					
				}
				centerTextPane.setText(information_words);
				centerTextPane.setCaretPosition(0);
			}
		}
		
	}
	
	// 다이얼 로그 클래스
	
	class Dialog_deleteWord extends Dialog{

		JTextField tf_eng;
		JTextField tf_kor;
		public Dialog_deleteWord(JFrame frame, String title) {
			super(frame, title,true);
			
			setTitle("Delete word");
			setLayout(new GridLayout(2,1,5,5));
			
			JLabel guide_label = new JLabel(" 영어 / 한글 입력");
			
			JPanel input_panel = new JPanel();
			input_panel.setLayout(new GridLayout(1,3,10,5));
			tf_eng = new JTextField("eng",15);
			tf_kor = new JTextField("kor",15);
			JButton btn_add = new JButton("Delete");
			input_panel.add(tf_eng);
			input_panel.add(tf_kor);
			input_panel.add(btn_add);
			
			
			this.add(guide_label);
			this.add(input_panel);
			
			this.pack();
			
			btn_add.addActionListener(new ActionListener_dialog_deleteWord());
			tf_kor.addActionListener(new ActionListener_dialog_deleteWord());
		}
			class ActionListener_dialog_deleteWord implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Word inputWord;
					if(!tf_eng.getText().equals("")&&!tf_kor.getText().equals("")) {
						inputWord = new Word(tf_eng.getText().trim(),tf_kor.getText().trim());
						if(words.containsValue(inputWord)) {
							int result = JOptionPane.showConfirmDialog(Dialog_deleteWord.this, "단어  ' "+inputWord.eng+" "+inputWord.kor+" ' 를 삭제합니다.","Confrim",JOptionPane.YES_NO_CANCEL_OPTION);
							if(result==JOptionPane.YES_OPTION) {
								Set<Integer> keySet = words.keySet();
								Iterator<Integer> keyIterator = keySet.iterator();
								while(keyIterator.hasNext()) {
									Integer key = keyIterator.next();
									Word value = words.get(key);
									if(value.equals(inputWord)) {
										words.remove(key);
										JOptionPane.showConfirmDialog(Dialog_deleteWord.this, "삭제했습니다.","Complete",JOptionPane.PLAIN_MESSAGE);
										deleted_keys.add(key);
										
										centerTextPane.requestFocus();
										break;
									}
								}
							}
						} else {
							JOptionPane.showConfirmDialog(Dialog_deleteWord.this, "단어 ' "+inputWord.eng+" "+inputWord.kor+" ' 는 존재하지 않습니다.","Warning",JOptionPane.ERROR_MESSAGE);
						}
						total_informationOfWords_questionNum.add(new InformationOfWords_questionNum(inputWord,words.size(),0));
						Dialog_deleteWord.this.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(contentPane, "단어를 알맞게 입력하세요","Warning",JOptionPane.ERROR_MESSAGE);
					}
					tf_eng.setText("");tf_kor.setText("");
				}
				
			}
			
		}
	
	class Dialog_addWord extends Dialog{

		JTextField tf_eng;
		JTextField tf_kor;
		public Dialog_addWord(JFrame frame, String title) {
			super(frame, title,true);
			
			setTitle("Add word");
			setLayout(new GridLayout(2,1,5,5));
			
			JLabel guide_label = new JLabel(" 영어 / 한글 입력");
			
			JPanel input_panel = new JPanel();
			input_panel.setLayout(new GridLayout(1,3,10,5));
			tf_eng = new JTextField("eng",15);
			tf_kor = new JTextField("kor",15);
			JButton btn_add = new JButton("Add");
			input_panel.add(tf_eng);
			input_panel.add(tf_kor);
			input_panel.add(btn_add);
			
			
			this.add(guide_label);
			this.add(input_panel);
			
			this.pack();
			
			btn_add.addActionListener(new ActionListener_dialog_addWord());
			tf_kor.addActionListener(new ActionListener_dialog_addWord());
			
		}
		
		class ActionListener_dialog_addWord implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Word inputWord;
				if(!tf_eng.getText().equals("")&&!tf_kor.getText().equals("")) {
					inputWord = new Word(tf_eng.getText(),tf_kor.getText());
					words.put(words.size(), inputWord);
					total_informationOfWords_questionNum.add(new InformationOfWords_questionNum(inputWord,words.size(),0));
					Dialog_addWord.this.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(contentPane, "단어를 알맞게 입력하세요","Warning",JOptionPane.ERROR_MESSAGE);
				}
				tf_eng.setText("");tf_kor.setText("");
			}
			
		}

		
		
	}
	
	class Dialog_searchKorWord extends Dialog {
		JTextField tf = new JTextField(10);
		JButton searchButton = new JButton("seach");

		public Dialog_searchKorWord(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new GridLayout(2, 1, 5, 5));
			JLabel guide = new JLabel("뜻을 입력해주세요");
			JPanel tp = new JPanel();
			tp.add(tf);
			tp.add(searchButton);
			this.add(guide);
			this.add(tp);
			setSize(200, 100);

			searchButton.addActionListener(new ActionListener_dialog_searchKorWord());
			tf.addActionListener(new ActionListener_dialog_searchKorWord());

		}

		class ActionListener_dialog_searchKorWord implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int numberOfSearch = 0;
				String search = tf.getText();
				String outputString = "";
				outputString += search + " 으로 단어를 찾은 결과 \n\n";
				for (int i = 0; i < words.size(); i++) {
					
						if(deleted_keys.indexOf(i)==-1) {
							if (words.get(i).kor.equals(search)) {
								outputString += words.get(i).kor + "\t" + words.get(i).eng + "\n";
								numberOfSearch++;
								searchedWords.push(words.get(i));searchWord_count++;
							}
						}
						
					
					
				}
				setVisible(false);
				outputString += "\n의 총 " + numberOfSearch + " 개의 단어가 검색되었습니다.";
				centerTextPane.setText(outputString);
				centerTextPane.setCaretPosition(0);
			}
		}
	}

	class Dialog_searchEngWord extends Dialog {
		JTextField tf = new JTextField(10);
		JButton searchButton = new JButton("seach");

		public Dialog_searchEngWord(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new GridLayout(2, 1, 5, 5));
			JLabel guide = new JLabel("영어를 입력해주세요");
			JPanel tp = new JPanel();
			tp.add(tf);
			tp.add(searchButton);
			this.add(guide);
			this.add(tp);
			setSize(200, 100);

			searchButton.addActionListener(new ActionListener_dialog_searchEngWord());
			tf.addActionListener(new ActionListener_dialog_searchEngWord());

		}

		class ActionListener_dialog_searchEngWord implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int numberOfSearch = 0;
				String search = tf.getText();
				String outputString = "";
				outputString += search + " 으로 단어를 찾은 결과 \n\n";
				for (int i = 0; i < words.size(); i++) {

					
						if(deleted_keys.indexOf(i)==-1) {
							if (words.get(i).eng.indexOf(search) != -1) {
								outputString += words.get(i).kor + "\t" + words.get(i).eng + "\n";
								numberOfSearch++;
								searchedWords.push(words.get(i));searchWord_count++;
							}
						}
						
					
					
				}
				setVisible(false);
				outputString += "\n의 총 " + numberOfSearch + " 개의 단어가 검색되었습니다.";
				centerTextPane.setText(outputString);
				centerTextPane.setCaretPosition(0);
			}
		}
	}

	@SuppressWarnings("serial")
	// 단어 퀴즈 다이얼 클래스 타이머 구현해주자  //스레드로 구현해야할듯
	class Dialog_btn_showquiz extends Dialog {

		ArrayList<Integer> list_selectedNumber = new ArrayList<>();
		
		JLabel question = new JLabel();
		JPanel answer = new JPanel();
		JPanel south_subPanel = new JPanel();
		JRadioButton[] radioButtons = new JRadioButton[4];

		JButton enter = new JButton("enter");
		JLabel notice_result = new JLabel();
		
		int temp_testNumber=0;
		boolean nextStage_sign = false; // 다음 단계로 넘어갈수 있는 권한
		boolean nextStage = false;  //	다음단계로 가기
		JButton nextButton = new JButton("Next");
		
		int answerNumber = (int) (Math.random() * words.size()); // 정답으로 사용할 단어의 번호
		
		int[] list_wrong = new int[3]; // 오답으로 사용할 단어의 번호들
		int[] location_determiner = new int[4]; // 위치를 지정한다
		
		long startTime;
		long endTime;
		
		public Dialog_btn_showquiz(JFrame frame, String title) {
			super(frame, title, true);
			startTime=System.currentTimeMillis();
			temp_incorrect_words.clear();
			centerTextPane.setText("단어 퀴즈 중입니다.");
			setSize(800, 100);
			
			
			setLayout(new BorderLayout());
			south_subPanel.setLayout(new GridLayout(1, 2, 5, 5));
			for (int k = 0; k < 4; k++) {
				radioButtons[k] = new JRadioButton(String.valueOf(k));
				answer.add(radioButtons[k]);
			}

			// 컴포넌트 부착
			JPanel subsubPanel = new JPanel();

			JPanel dialog_centerPanel = new JPanel();
			dialog_centerPanel.setLayout(new GridLayout(1, 2, 5, 5));
			dialog_centerPanel.add(question);
			dialog_centerPanel.add(notice_result);
			this.add(dialog_centerPanel, BorderLayout.CENTER);
			subsubPanel.add(enter);
			subsubPanel.add(nextButton);
			south_subPanel.add(answer);
			south_subPanel.add(subsubPanel);
			this.add(south_subPanel, BorderLayout.SOUTH);
			
			finalList = new ArrayList<>();
			
			enter.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton btn = (JButton)e.getSource();
					boolean correct_sign = true;
					for (int i = 0; i < 4; i++) { // 정답버튼 이외의 버튼이 눌렸으면 false
						if (i != location_determiner[0]) {
							if (radioButtons[i].isSelected()) {
								correct_sign = false;
							}
						}
					}
					if (!radioButtons[location_determiner[0]].isSelected()) { // 정답버튼이 눌리지 않았으면 false
						correct_sign = false;
					}

					if (correct_sign) {
						notice_result.setText("정답입니다!");
						Number_correct++;
					} else {
						notice_result.setText("틀렸습니다. 정답은 " + radioButtons[location_determiner[0]].getText() + " 입니다.");
						temp_incorrect_words.add(words.get(answerNumber));
						total_informationOfWords_incorrectNum.get(answerNumber).numberOfincorrect++;
						Number_incorrect++;
						finalList.add(words.get(answerNumber));
						hasIncorrect=true;
					}

					if(Number_question==10) {
						nextButton.setText("End");
					}
					
					nextStage_sign = true;
					nextButton.setEnabled(true);
					btn.setEnabled(false);
					
				}

			});
			
			nextButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					nextButton.setEnabled(false);
					Number_question++;
					readyForQuestion(); 
					if(Number_question==11) {
						endTime=System.currentTimeMillis();
						Dialog_btn_showquiz.this.setVisible(false);
						String temp_result ="\n=== 퀴즈 결과 ===\n\n";
						temp_result += "총 " + (Number_question - 1) + " 문제 중에 " + Number_correct + "개 맞았습니다.\n\n";

						
						
						if(finalList.size()!=0) {
							temp_result += "틀린 단어들 \n";
							for(int i=0;i<finalList.size();i++) {
								temp_result += temp_incorrect_words.get(i).toString();
							}
							temp_result+="\n총 소요시간 : "+(endTime-startTime)/1000+" 초";
							temp_result += "\n\n오답 퀴즈를 진행하려면 F5키를 누르세요";
						}
						
						centerTextPane.requestFocus(true);
						centerTextPane.setText(temp_result);
						centerTextPane.setCaretPosition(0);
					}

				}

				
			});
			
			
		}		

		public void readyForQuestion() {
			// TODO Auto-generated method stub
			
			while(true) {
				answerNumber = (int) (Math.random() * words.size()); // 정답으로 사용할 단어의 번호
				if(!list_selectedNumber.contains(answerNumber)&&(deleted_keys.indexOf(answerNumber)==-1)) {
					//이미 뽑힌 번호이랑 겹치지 않는다면                                          //제거된게 아니라면
					list_selectedNumber.add(answerNumber);
					total_informationOfWords_questionNum.get(answerNumber).numberOfQuestion++;
					break;
				}
			}
			
			
			list_wrong = new int[3]; // 오답으로 사용할 단어의 번호들
			location_determiner = new int[4]; // 위치를 지정한다
			
			nextButton.setEnabled(false);
			enter.setEnabled(true);
			for(int t=0;t<4;t++) {
				radioButtons[t].setSelected(false);
			}
			question.setFont(new Font("고딕체",Font.BOLD,20));
			list_selectedNumber.add(answerNumber);
			question.setText(" 제시어 :       " + words.get(answerNumber).eng);
			notice_result.setText(Number_question+"번 문제                                   기록 : "+Number_correct+"/"+(Number_question-1)); 
			for (int k = 0; k < 3; k++) { // 오답 번호를 선정한다
				boolean check = true;
				int temp_wrong = (int) (Math.random() * words.size());
				if (words.get(temp_wrong).kor.equals(words.get(answerNumber).kor)||!(deleted_keys.indexOf(temp_wrong)==-1 )) {
					k--;
					check = false;
				} else {
					
					if(check==true) {
						for(int p=0;p<list_wrong.length;p++) {
							if(words.get(list_wrong[p]).kor.equals(words.get(temp_wrong).kor)) {
								k--;
								check=false;
								break;
							}
						}
					}
					
					
				}
				if (check) {
					list_wrong[k] = temp_wrong;
				}

			}

			// location_determiner에 정보를 채운다. 0인방에는 정답의 위치
			int temp_locationOfAnswer = (int) (Math.random() * 4); // 0~3사이의 값 //이 위치의 버튼에 정답이 배치된다
			location_determiner[0] = temp_locationOfAnswer;

			for (int t = 0; t < 3; t++) {
				boolean check = true;
				int temp_locationOfWrong = (int) (Math.random() * 4);
				for (int p = 0; p < 3; p++) {
					if (location_determiner[p] == temp_locationOfWrong) {
						t--;
						check = false;
						break;
					}
				}
				if (check) {
					location_determiner[t + 1] = temp_locationOfWrong;
				}

			}

			radioButtons[location_determiner[0]].setText(words.get(answerNumber).kor);

			for (int t = 0; t < 3; t++) {
				radioButtons[location_determiner[t + 1]].setText(words.get(list_wrong[t]).kor);
			}

			

			this.pack();
			
		}
		
		

		
	}
	
	@SuppressWarnings("serial")
	// 단어 퀴즈 다이얼 클래스 타이머 구현해주자  //스레드로 구현해야할듯
	class Dialog_btn_showquiz_incorrectVersion extends Dialog {

		ArrayList<Integer> list_selectedNumber = new ArrayList<>();
		
		
		JLabel question = new JLabel();
		JPanel answer = new JPanel();
		JPanel south_subPanel = new JPanel();
		JRadioButton[] radioButtons = new JRadioButton[4];

		JButton enter = new JButton("enter");
		JLabel notice_result = new JLabel();
		
		int temp_testNumber=0;
		boolean nextStage_sign = false; // 다음 단계로 넘어갈수 있는 권한
		boolean nextStage = false;  //	다음단계로 가기
		JButton nextButton = new JButton("Next");
		
		int answerNumber=0; // 정답으로 사용할 단어의 번호
		
		int[] list_wrong = new int[3]; // 오답으로 사용할 단어의 번호들
		int[] location_determiner = new int[4]; // 위치를 지정한다
		
		
		int numberOfIncorrect=finalList.size();  //오답퀴즈 문제개수
		
		long startTime;
		long endTime;
		
		public Dialog_btn_showquiz_incorrectVersion(JFrame frame, String title) {
			super(frame, title, true);
			
			startTime=System.currentTimeMillis();
			
			centerTextPane.setText("오답 퀴즈 중입니다.");
			setSize(800, 100);
			temp_temp_incorrect_wordsOfIncorrect.clear();
			temp_temp_correct_wordsOfIncorrect.clear();
			
			list_selectedNumber.clear();
			
			setLayout(new BorderLayout());
			south_subPanel.setLayout(new GridLayout(1, 2, 5, 5));
			for (int k = 0; k < 4; k++) {
				radioButtons[k] = new JRadioButton(String.valueOf(k));
				answer.add(radioButtons[k]);
			}
			numberOfIncorrect=finalList.size(); 
			
			
			
			// 컴포넌트 부착
			JPanel subsubPanel = new JPanel();

			JPanel dialog_centerPanel = new JPanel();
			dialog_centerPanel.setLayout(new GridLayout(1, 2, 5, 5));
			dialog_centerPanel.add(question);
			dialog_centerPanel.add(notice_result);
			this.add(dialog_centerPanel, BorderLayout.CENTER);
			subsubPanel.add(enter);
			subsubPanel.add(nextButton);
			south_subPanel.add(answer);
			south_subPanel.add(subsubPanel);
			this.add(south_subPanel, BorderLayout.SOUTH);
			
			
			
			enter.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton btn = (JButton)e.getSource();
					boolean correct_sign = true;
					for (int i = 0; i < 4; i++) { // 정답버튼 이외의 버튼이 눌렸으면 false
						if (i != location_determiner[0]) {
							if (radioButtons[i].isSelected()) {
								correct_sign = false;
							}
						}
					}
					if (!radioButtons[location_determiner[0]].isSelected()) { // 정답버튼이 눌리지 않았으면 false
						correct_sign = false;
					}

					if (correct_sign) {
						notice_result.setText("정답입니다! 오답에서 제거합니다.");
						Word tempWord=new Word(question.getText(),radioButtons[location_determiner[0]].getText());
						
						
							if(finalList.remove(tempWord)) {
								
							}
						
						temp_temp_correct_wordsOfIncorrect.add(tempWord);  //
						Number_correct++;
						
					} else {
						notice_result.setText("틀렸습니다. 정답은 " + radioButtons[location_determiner[0]].getText() + " 입니다.");
						
						if (answerNumber < finalList.size()) {
							answerNumber++;
							
						}

						
						total_informationOfWords_incorrectNum.get(answerNumber).numberOfincorrect++;
						temp_temp_incorrect_wordsOfIncorrect.add(temp_incorrect_words.get(answerNumber));
						Number_incorrect++;
					}

					if(Number_question==10) {
						nextButton.setText("End");
					}
					
					nextStage_sign = true;
					nextButton.setEnabled(true);
					btn.setEnabled(false);
					
				}

			});
			
			nextButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					nextButton.setEnabled(false);
					boolean check = true;
					
					Number_question_incorrectVersion++;
					
					if (Number_question_incorrectVersion == numberOfIncorrect) {
						check = false;
						endTime=System.currentTimeMillis();
						Dialog_btn_showquiz_incorrectVersion.this.setVisible(false);
						String temp_result = "\n=== 오답 퀴즈 결과 ===\n\n";
						temp_result += "총 " + Number_question_incorrectVersion + " 문제 중에 " + Number_correct
								+ "개 맞았습니다.\n\n";

						if (finalList.size() == 0) {
							temp_result += "모든 오답을 제거했습니다!";
							btn_incorrectQuiz.setEnabled(false);
						} else {
							temp_result += "여전히 틀린 단어들\n";
							for(int i=0;i<finalList.size();i++) {
								temp_result += finalList.get(i).toString();
							}
							
							temp_result += "\n\n오답에서 제거한 단어들\n";
							for(int i=0;i<temp_temp_correct_wordsOfIncorrect.size();i++) {
								temp_result += temp_temp_correct_wordsOfIncorrect.get(i).toString();
							}
							
						}
						temp_result+="\n총 소요시간 : "+(endTime-startTime)/1000+" 초";
						centerTextPane.setText(temp_result);
						centerTextPane.setCaretPosition(0);
					}
					if (check == true) {
						readyForQuestion();
						
						nextButton.setEnabled(false);
						
					}

				}

			});

		}

		public void readyForQuestion() {
			// TODO Auto-generated method stub
			
			
			list_wrong = new int[3]; // 오답으로 사용할 단어의 번호들
			location_determiner = new int[4]; // 위치를 지정한다
			
			nextButton.setEnabled(false);
			enter.setEnabled(true);
			for(int t=0;t<4;t++) {
				radioButtons[t].setSelected(false);
			}
			list_selectedNumber.add(answerNumber);
			question.setFont(new Font("고딕체",Font.BOLD,20));
			question.setText(finalList.get(answerNumber).eng);
			notice_result.setText((Number_question_incorrectVersion+1)+"번 문제                                   기록 : "+Number_correct+"/"+Number_question_incorrectVersion); 
			for (int k = 0; k < 3; k++) { // 오답 번호를 선정한다
				boolean check = true;
				int temp_wrong = (int) (Math.random() * words.size());
				
				if (words.get(temp_wrong).kor.equals(temp_incorrect_words.get(answerNumber).kor)) {
					k--;
					check = false;
				} else {
					for (int p = 0; p < list_wrong.length; p++) {
						if (words.get(list_wrong[p]).kor.equals(words.get(temp_wrong).kor) ) {
							k--;
							check = false;
							break;
						}
					}
					
					
					
				}
				if (check) {
					list_wrong[k] = temp_wrong;
				}

			}

			// location_determiner에 정보를 채운다. 0인방에는 정답의 위치
			int temp_locationOfAnswer = (int) (Math.random() * 4); // 0~3사이의 값 //이 위치의 버튼에 정답이 배치된다
			location_determiner[0] = temp_locationOfAnswer;

			for (int t = 0; t < 3; t++) {
				boolean check = true;
				int temp_locationOfWrong = (int) (Math.random() * 4);
				for (int p = 0; p < 3; p++) {
					if (location_determiner[p] == temp_locationOfWrong) {
						t--;
						check = false;
						break;
					}
				}
				if (check) {
					location_determiner[t + 1] = temp_locationOfWrong;
				}

			}
			
			radioButtons[location_determiner[0]].setText(finalList.get(answerNumber).kor);

			for (int t = 0; t < 3; t++) {
				radioButtons[location_determiner[t + 1]].setText(words.get(list_wrong[t]).kor);
			}

			

			this.pack();
			
		}
		
		

		
	}
	
}


