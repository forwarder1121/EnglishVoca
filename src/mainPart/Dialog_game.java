package mainPart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;





public class Dialog_game extends JDialog {
	
	JTextPane centerTextPane;
	Map<Integer, Word> words;
	ArrayList<Integer> deleted_keys;
	
	Map<Integer,DroppingWord> selected_dropping_words;
	Map<Integer,JLabel> dropping_labels;
	
	//화면 가운데 판넬
	JPanel centerPanel = new JPanel();
	
	//타이머 
	JLabel timerLabel = new JLabel("0");
	
	//이미지
	JLabel imageLabel;
	
	//체력 
	int life=5;
	JLabel lifeLabel = new JLabel("남은 체력 : "+life);
	
	int correct=0;
	JLabel correctLabel;
	
	public Dialog_game(JFrame parent,JTextPane centerTextPane,String title, Map<Integer,Word> words, ArrayList<Integer> deleted_keys) {
		super(parent,title,true);
		
		this.words=words;
		this.centerTextPane=centerTextPane;
		this.setLayout(new BorderLayout());
		setSize(500,700);
		centerPanel.setLayout(null);
		centerPanel.setSize(400,600); 
		selected_dropping_words=new HashMap<>();
		dropping_labels=new HashMap<>();
		
		
		
		
		//문제로 출제될 단어들을 dropping_words에 저장한다
		ArrayList<Integer> selectedNums = new ArrayList<>();
		for(int i=0;i<words.size()-deleted_keys.size()-10;i++) {
			//삭제 번호가 아닌경우에만 선택되어 해당 번호의 단어 정보가 selected_dropping_words에 저장된다. (총 10개)
			while(true) {
				int selectedNumber;
				selectedNumber = (int)((Math.random()*(words.size())));
				if(selectedNums.contains(selectedNumber)) {
					i--; break;
				} selectedNums.add(selectedNumber);
				if(deleted_keys.contains(selectedNumber)==false&&words.get(selectedNumber).kor.length()<9) {
					//단어들을 selected_dropping_words에 넣는다.
					DroppingWord temp_droppinWord = new DroppingWord(words.get(selectedNumber),new Point(0,0));
					selected_dropping_words.put(i, temp_droppinWord);
					//selected_dropping_words에 저장완료
					break;
				}
			}
		}
		
		//추출된 단어(DroppingWord타입)의 맴버 pos를 초기화시킨다.
		
		for(int i=0;i<selected_dropping_words.size();i++) {
			
			int x = (int)(Math.random()*(centerPanel.getWidth()-130));
			int y = 0;
			if(x<10) {
				i--;
			}
			Point temp_point = new Point(x,y);
			selected_dropping_words.get(i).pos=new Point(temp_point);
		}
		
		//추출된 단어들로 라벨을 생성하여 dropping_labels에 저장한다
		for(int i=0;i<selected_dropping_words.size();i++) {
			dropping_labels.put(i,new JLabel(selected_dropping_words.get(i).word.kor));
		}
		
		//주어진 판넬들의 절대 위치와 크기를 준다 
		for(int i=0;i<dropping_labels.size();i++) {
			dropping_labels.get(i).setSize(100, 130);
			dropping_labels.get(i).setLocation(selected_dropping_words.get(i).pos);
			// centerPanel.add(dropping_labels.get(i));
		}

		// 화면 옆 판넬
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(5, 1, 5, 5));
		ImageIcon image = new ImageIcon("smile1.jpg");
		imageLabel = new JLabel(image);
		JLabel noticeResult = new JLabel("퀴즈 중..");
		
		
		correctLabel = new JLabel("맞은 개수 : "+correct);
	
		
		
		
		timerLabel.setFont(new Font("고딕체",Font.PLAIN,30));
		eastPanel.add(timerLabel);
		eastPanel.add(lifeLabel);
		eastPanel.add(noticeResult);
		eastPanel.add(imageLabel);
		eastPanel.add(correctLabel);
		
		
		

		//화면 아래 판넬  
		JPanel sourthPanel = new JPanel();
		JTextField textField = new JTextField(10);
		
		textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				boolean check = false;
				for(int i=0;i<selected_dropping_words.size();i++) {
					if(selected_dropping_words.get(i).word.eng.equals(textField.getText())) { // 알맞게 입력하면
						dropping_labels.get(i).setVisible(false);
						check=true;
					}
				}
				textField.setText("");
				if (check == true) {
					noticeResult.setText("정답입니다!");
					ImageIcon image = new ImageIcon("smile1.jpg");
					correct++;
					correctLabel.setText("맞은 개수 : "+correct);
					imageLabel.setIcon(image);
				} else {

					noticeResult.setText("틀렸습니다.");
					ImageIcon image = new ImageIcon("bad.png");
					
					imageLabel.setIcon(image);
				}

			}

		});
		
		
		sourthPanel.add(textField);
		
		
		//화면 x가 0으로 연이어 나오는 거 수정해야할 필요
		
		
		
		
		
		
		//화면에 붙이는 작업
		this.add(centerPanel,BorderLayout.CENTER);
		this.add(eastPanel,BorderLayout.EAST);
		this.add(sourthPanel,BorderLayout.SOUTH);
		
		
		
		
		
		GameThread gameThread = new GameThread();
		gameThread.start();
		this.setVisible(true);
		
		
	}
	
	class GameThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int timer=0;
			int timeForLabel=0; //1초마다 한개의 라벨을 추가로 보여준다.
			
			
			//여기에서 selected_dropping_words의 각 원소인 DroppingWord의 맴버 pos의 값을 조정한다.
			
			//라벨의 위치를 1초마다 내린다.
			while(true) {
				try {
				
					
					//시간에따라 하나씩 화면에 생성한다.(centerPane에 추가)
					if(timeForLabel<dropping_labels.size()) {
						centerPanel.add(dropping_labels.get(timeForLabel));
					}
					
					
					//모든 라벨의 위치를 조정한다.
					for(int i=0;i<dropping_labels.size();i++) { 
						
						if(i<timeForLabel) { //화면에 출력되는 것만 작업해준다.
							selected_dropping_words.get(i).pos.y+=20; //각 원소의 y값을 증가시킨다.
							Point temp_point = new Point(selected_dropping_words.get(i).pos);
							dropping_labels.get(i).setLocation(temp_point);
						}
						
					}
					
					
					
					// 종료 조건
					for(int i=0;i<selected_dropping_words.size();i++) {
						if(dropping_labels.get(i).isVisible()==true) {
							if(selected_dropping_words.get(i).pos.y>centerPanel.getHeight()-100) {
								life--;
								lifeLabel.setText("남은 체력 : "+life);
								if(life==0) break;
							}
						}
					}
					boolean end=false;
					if(life<0) {
						end = true;
					}
					
					if(end==true) {
						JOptionPane.showMessageDialog(Dialog_game.this, "GameOver!","End",JOptionPane.CLOSED_OPTION);
						Dialog_game.this.setVisible(false);
						
						String result = "\n\n 게임 결과\n\n";
						result += "총 생존시간 : "+timerLabel.getText()+"\n총 맞은 개수 : "+correct+" 개\n"+"\n다시 도전하세요!";
						
						centerTextPane.setText(result);
						
						
						return;
					}
					
					
					Dialog_game.this.repaint();
					
					Thread.sleep(500);
					timer++;
					timerLabel.setText(String.valueOf(timer/2)+" 초");
					if(timeForLabel<dropping_labels.size()) {
						timeForLabel++;
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}

	
	
	
	
	
	
}

////////
