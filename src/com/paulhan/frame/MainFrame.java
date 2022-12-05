package com.paulhan.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.paulhan.api.DateManager;
import com.paulhan.main.Info;

public class MainFrame {
	private static MainFrame mainFrame;
	private JFrame frame;
	private List<JButton> btnDayList = new ArrayList<JButton>(DateManager.WEEK_UNIT * (DateManager.WEEK_UNIT-1));
	private JButton btnCurrentDate = null;
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public JButton getBtnCurrentDate() {
		return this.btnCurrentDate;
	}
	
	public static MainFrame getInstance() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		return mainFrame;
	}

	private MainFrame() {
		this.frame = new JFrame(Info.getTitle());					// 제목
		this.frame.setLayout(new BorderLayout());					// 레이아웃 설정
		this.frame.setSize(new Dimension(650, 600));					// 크기 설정
		this.frame.setResizable(false);								// 크기 변경 제한
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// X(종료) 버튼 활성화
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		panel1.setLayout(new FlowLayout(FlowLayout.CENTER));	// 선택된 날짜
		panel2.setLayout(new GridLayout(DateManager.WEEK_UNIT, DateManager.WEEK_UNIT));	// 일자
		
		this.btnCurrentDate = new JButton(DateManager.getInstance().getCurrentDate("yyyy년 MM월"));
		JButton btnLeft = new JButton("<");
		JButton btnRight = new JButton(">");
		
		Info.setBackground(this.btnCurrentDate, Info.COLOR_CURRENT_DATE);
		Info.setBackground(btnLeft, Info.COLOR_ARROW);
		Info.setBackground(btnRight, Info.COLOR_ARROW);
		
		Info.setFont(this.btnCurrentDate, Font.BOLD, Info.CURRENT_DATE);
		
		panel1.setPreferredSize(new Dimension(0,70));
		Info.setBackground(panel1, Info.COLOR_PANEL);
		panel1.add(btnLeft);
		panel1.add(this.btnCurrentDate);
		panel1.add(btnRight);
		
		this.frame.add(panel1, BorderLayout.NORTH);
		this.frame.add(panel2, BorderLayout.CENTER);
		
		showWeekButton(panel2);
		
		int calendarSize = DateManager.WEEK_UNIT * (DateManager.WEEK_UNIT-1);
		showCalendarButton(panel2, calendarSize);
		
		this.frame.setVisible(true);
		
		/*
		 * btnCurrentDate의 속성이 바뀔떄마다 작동(title)
		 */
		this.btnCurrentDate.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				changeCalendarDay(panel2, calendarSize);
			}
		});
		
		/*
		 * btnCurrentDate의 버튼이 눌릴때마다 작동
		 */
		this.btnCurrentDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserInputFrame userInputFrame = UserInputFrame.getInstance();
				userInputFrame.setVisible();
			}
		});
		
		/*
		 * btnLeft의 버튼이 눌릴때마다 작동
		 */
		btnLeft.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
            	moveMonth(true);
            }
		});
		
		/*
		 * btnRight의 버튼이 눌릴때마다 작동
		 */
		btnRight.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
            	moveMonth(false);
            }
		});
	}
	
	/*
	 * btnCurrentDate의 문자열을 날짜 형식으로 바꿔주는 메서드.
	 * @param	int		연도를 의미 함
	 * @param	int		달을 의미 함
	 */
	public void btnCurrentDateSetText(int year, int month) {
		String dateFormat = String.format("%d년 %02d월", year, month);
		this.btnCurrentDate.setText(dateFormat);
	}
	
	/*
	 * currentDate가 바뀔때 마다 달력의 일자들을 변경해주는 메서드
	 * @param	JPanel	변경할 panel
	 * @param	int		달력 크기
	 */
	private void changeCalendarDay(JPanel panel, int calendarSize) {
		String[] date	= this.btnCurrentDate.getText().split(" ");
		int year		= DateManager.getInstance().getNumber(date[0]);
		int month		= DateManager.getInstance().getNumber(date[1]);
		
		date[0] = year + "";
		date[1] = month + "";

		List<String> dayList = DateManager.getInstance().getDayToList(date, calendarSize);
		
		for(int i = 0; i < this.btnDayList.size(); i++) {
			JButton btnDay = this.btnDayList.get(i);
			btnDay.setText(dayList.get(i));
			
		}
		
		setName();
		
		for(int i = 0; i < this.btnDayList.size(); i++) {
			JButton btnDay = this.btnDayList.get(i);
			
			Info.setFont(btnDay, Font.BOLD, Info.DAY);
			
			boolean shouldChange = btnDay.getName() != null && btnDay.getName().equals("change");
			if(shouldChange) {
				Info.setBackground(btnDay, Info.COLOR_ANOTHER_DAYS_BACKGROUND);
				Info.setForeground(btnDay, Info.COLOR_ANOTHER_DAYS_FOREGROUND);
			} else if(i % DateManager.WEEK_UNIT == 0) {
				Info.setBackground(btnDay, Info.COLOR_SUNDAY_BACKGROUND);
				Info.setForeground(btnDay, Info.COLOR_SUNDAY_FOREGROUND);
			} else if(i % DateManager.WEEK_UNIT == 6) {
				Info.setBackground(btnDay, Info.COLOR_SATURDAY_BACKGROUND);
				Info.setForeground(btnDay, Info.COLOR_SATURDAY_FOREGROUND);
			} else {
				Info.setBackground(btnDay, Info.COLOR_DAYS_BACKGROUND);
				Info.setForeground(btnDay, Info.COLOR_DAYS_FOREGROUND);
			}
			
			btnDay.setName(null);
		}
		
	}
	
	/*
	 * 화살표 버튼이 눌릴때마다 다음달이나 이전달로 세팅하는 메서드
	 * @param	boolean	true - 이전달
	 * 					false - 다음 달
	 */
	private void moveMonth(boolean isPrev) {
		String[] date = this.btnCurrentDate.getText().split(" ");
		int year 	  =	DateManager.getInstance().getNumber(date[0]);
		int month	  = DateManager.getInstance().getNumber(date[1]);
		
		if(isPrev) {
			month--;
			
			if(month < DateManager.JANUARY) {
				year--;
				month = 12;
			}
		} else {
			month++;
			
			if(month > DateManager.DECEMBER) {
				year++;
				month = 1;
			}
		}
		btnCurrentDateSetText(year, month);
	}
	
	/*
	 * 일 ~ 토까지 버튼 추가
	 * @param	panel	추가할 panel
	 */
	private void showWeekButton(JPanel panel) {
		JButton[] btnWeek = new JButton[DateManager.WEEK_UNIT];
		for (int i = 0; i < btnWeek.length; i++) {
			btnWeek[i] = new JButton(DateManager.getInstance().getWeekToString(i));
			Info.setFont(btnWeek[i], Font.BOLD, Info.WEEK);
			
			if(i == 0) {
				Info.setBackground(btnWeek[i], Info.COLOR_SUNDAY_BACKGROUND);
				Info.setForeground(btnWeek[i], Info.COLOR_SUNDAY_FOREGROUND);
			} else if(i == 6) {
				Info.setBackground(btnWeek[i], Info.COLOR_SATURDAY_BACKGROUND);
				Info.setForeground(btnWeek[i], Info.COLOR_SATURDAY_FOREGROUND);
			} else {
				Info.setBackground(btnWeek[i], Info.COLOR_WEEKS_BACKGROUND);
				Info.setForeground(btnWeek[i], Info.COLOR_WEEKS_FOREGROUND);
			}
			
			panel.add(btnWeek[i]);
		}
	}
	
	/*
	 * 달력의 일자를 생성하여 표시하는 메서드
	 * @param 	panel			추가할 패널
	 * @param	calendarSize	달력의 크기
	 */
	private void showCalendarButton(JPanel panel, int calendarSize) {
		String[] 	 date 	 = this.btnCurrentDate.getText().split(" ");
		List<String> dayList = DateManager.getInstance().getDayToList(date, calendarSize);
		
		for(int i = 0; i < calendarSize; i++) {
			this.btnDayList.add(new JButton(dayList.get(i)));
		}
		
		setName();
		
		for(int i = 0; i < this.btnDayList.size(); i++) {
			JButton btnDay = this.btnDayList.get(i);
			
			Info.setFont(btnDay, Font.BOLD, Info.DAY);
				
			setColor(i, btnDay);
			
			panel.add(this.btnDayList.get(i));
			
		}
		

	}

	/*
	 * 해당 날짜의 일자가 아닌 버튼들의 이름을 바꾸는 메서드
	 * 8월이면 7월 31일, 9월 1일 ~ 9월 10일
	 */
	private void setName() {
		List<Integer> anotherMonthIndex = DateManager.getInstance().getAnotherMonthIndex();
		for(int i = 0; i < anotherMonthIndex.size(); i++) {
			int index = anotherMonthIndex.get(i);
			this.btnDayList.get(index).setName("change");
			Info.setBackground(this.btnDayList.get(index), Info.COLOR_ANOTHER_DAYS_BACKGROUND);
			Info.setForeground(this.btnDayList.get(index), Info.COLOR_ANOTHER_DAYS_FOREGROUND);
			
		}
		DateManager.getInstance().setAnotherMonthIndex();
	}

	/*
	 * 해당 날짜의 일자가 아닌 버튼들의 색을 지정해주는 메서드
	 * @param	int			반복문에 쓰이는 index
	 * @param	JButton		색상을 지정할 버튼
	 */
	private void setColor(int i, JButton btnDay) {
		boolean isChange = btnDay.getName() != null && btnDay.getName().equals("change");
		if(isChange) {
			Info.setBackground(btnDay, Info.COLOR_ANOTHER_DAYS_BACKGROUND);
			Info.setForeground(btnDay, Info.COLOR_ANOTHER_DAYS_FOREGROUND);
		}else if(i % DateManager.WEEK_UNIT == 0) {
			Info.setBackground(btnDay, Info.COLOR_SUNDAY_BACKGROUND);
			Info.setForeground(btnDay, Info.COLOR_SUNDAY_FOREGROUND);
		} else if(i % DateManager.WEEK_UNIT == 6) {
			Info.setBackground(btnDay, Info.COLOR_SATURDAY_BACKGROUND);
			Info.setForeground(btnDay, Info.COLOR_SATURDAY_FOREGROUND);
		} else {
			Info.setBackground(btnDay, Info.COLOR_DAYS_BACKGROUND);
			Info.setForeground(btnDay, Info.COLOR_DAYS_FOREGROUND);
		}
		btnDay.setName(null);
	}
	
	/*
	 * JPanel중 gridplayout에서 행 반환
	 * @param	panel	전달받은 패널
	 * @return	int		몇행인지 반환, gridlayout이 아니면 -1 반환
	 */
	@Deprecated
	private int getRows(JPanel panel) {
		if(panel.getLayout() instanceof GridLayout) {
			return ((GridLayout) panel.getLayout()).getRows();
		} else {
			return -1;
		}
	}
	
	/*
	 * JPanel중 gridplayout에서 열 반환
	 * @param	panel	전달받은 패널
	 * @return	int		몇행인지 반환, gridlayout이 아니면 -1 반환
	 */
	@Deprecated
	private int getColumns(JPanel panel) {
		if(panel.getLayout() instanceof GridLayout) {
			return ((GridLayout) panel.getLayout()).getColumns();
		} else {
			return -1;
		}
	}
	
	/*
	 * MainFrame에서 btnCurrentDate 반환
	 * @return	JButton	Mainframe에서 현재 날짜 버튼 반환
	 */
	@Deprecated
	private JButton getButton(String btnName) {
		JButton btn = null;
		
		for(Component c : frame.getContentPane().getComponents()) {
			for(Component sc : ((JPanel)c).getComponents()) {
				if(sc.getName() == null) {
					continue;
				}
				if(sc.getName().equals(btnName)) {
					btn = (JButton) sc;
				}
			}
		}
		
		if(btn == null) {
			System.out.println(btnName + " - 찾을 수 없음");
			return new JButton("0001년 01월");
		}
		
		return btn;
	}
	
}
