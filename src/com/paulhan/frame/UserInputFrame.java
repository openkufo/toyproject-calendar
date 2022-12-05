package com.paulhan.frame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.paulhan.api.DateManager;
import com.paulhan.main.Info;

public class UserInputFrame implements ActionListener {
	private static UserInputFrame userInputFrame;
	
	public static UserInputFrame getInstance() {
		if(userInputFrame == null) {
			userInputFrame = new UserInputFrame();
		}
		return userInputFrame;
	}
	
	private JFrame frame;
	private JPanel panel;
	private JTextField txtYear;
	private JTextField txtMonth;
	private JButton btnSubmit;
	private JButton btnCancle;
	private GridBagConstraints constraint;
	public JTextField getTxtYear() {
		return this.txtYear;
	}
	
	public JTextField getTxtMonth() {
		return this.txtMonth;
	}
	
	private UserInputFrame() {
		this.frame = new JFrame();
		this.panel = new JPanel();
		
		this.frame.setSize(360,70);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		
		GridBagLayout gridbag = new GridBagLayout();
		constraint = new GridBagConstraints();
		this.constraint.weightx = 1.0;
		this.constraint.weighty = 1.0;
		BevelBorder borderStyle = new BevelBorder(BevelBorder.RAISED);

		this.panel.setBorder(borderStyle);
		this.panel.setLayout(gridbag);

		this.btnSubmit = new JButton("확인");
		this.btnCancle = new JButton("종료");
		
		this.txtYear = new JTextField();
		this.txtMonth = new JTextField();
		
		this.txtYear.setPreferredSize(new Dimension(50,40));
		this.txtMonth.setPreferredSize(new Dimension(30,40));
		
		Info.setFont(this.txtYear, Font.BOLD, 15);
		Info.setFont(this.txtMonth, Font.BOLD, 15);
		
		JLabel lblYear = new JLabel("년");
		JLabel lblMonth = new JLabel("월");
		
		Info.setFont(lblYear, Font.BOLD, 20);
		Info.setFont(lblMonth, Font.BOLD, 20);
		
		this.txtYear.addActionListener(this);
		this.txtMonth.addActionListener(this);
		this.btnSubmit.addActionListener(this);
		this.btnCancle.addActionListener(this);
		
		layout(this.txtYear,	0, 0, 1, 1);
		layout(lblYear,			1, 0, 1, 1);
		layout(this.txtMonth,	2, 0, 1, 1);
		layout(lblMonth, 		3, 0, 1, 1);
		layout(this.btnSubmit,	4, 0, 1, 1);
		layout(this.btnCancle,	5, 0, 1, 1);
		
		this.frame.add(panel);
	}
	
	public void layout(Component obj, int x, int y,int width, int height)
	{
		this.constraint.gridx=x;
		this.constraint.gridy=y;
		this.constraint.gridwidth = width;
		this.constraint.gridheight = height;
		this.panel.add(obj, this.constraint);
	}
	
	public void setVisible() {
		if(this.frame.isVisible()) {
			this.frame.setVisible(false);
			this.txtYear.setText("");
			this.txtMonth.setText("");
		} else {
			Point point = MainFrame.getInstance().getFrame().getLocation();
			point.x += 145;
			point.y += 30;
			this.frame.setLocation(point);
			
			String currentDate = MainFrame.getInstance().getBtnCurrentDate().getText();
			String[] date = currentDate.split(" ");
			int year		= DateManager.getInstance().getNumber(date[0]);
			int month		= DateManager.getInstance().getNumber(date[1]);
			
			this.txtYear.setText(year+"");
			this.txtMonth.setText(String.format("%02d",month));
			
			this.frame.setVisible(true);
		}
	}

	
	public void moveFrame() {
		Point point = MainFrame.getInstance().getFrame().getLocation();
		point.x += 145;
		point.y += 30;
		this.frame.setLocation(point);
	}
	
	/*
	 * JButton, JTextField에서 Event 발생시 호출되는 메서드
	 * 확인버튼 - 입력된 년, 월의 유효성 검사 진행 이후 달력의 날짜 변경
	 * 종료버튼 - 해당 창 닫음
	 * 
	 * JTextField에서 ENTER 입력시 위 확인버튼과 동일
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton btn = (JButton) e.getSource();
			
			switch(btn.getText()) {
				case "확인" :
							showDialogAfterSetText();
							break;
				case "종료" :
							setVisible();
							break;
			}
		} else if(e.getSource() instanceof JTextField) {
			showDialogAfterSetText();
		}
	}
	
	/*
	 * 다이얼로그를 보여주고 달력의 제목을 바꾸는 메서드
	 */
	private void showDialogAfterSetText() {
		String year = this.txtYear.getText();
		String month = this.txtMonth.getText();
		
		try {
			Integer.parseInt(year);
			Integer.parseInt(month);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this.frame,"숫자만 입력 해주세요", "다시 입력", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(year.length() != 4) {
			JOptionPane.showMessageDialog(this.frame,"연도를 yyyy 형식으로 입력 해주세요", "다시 입력", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(month.length() <= 0 || month.length() > 2) {
			JOptionPane.showMessageDialog(this.frame,"월을 MM 형식으로 입력 해주세요", "다시 입력", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		boolean isMonth = Integer.parseInt(month) < DateManager.JANUARY ||
						  Integer.parseInt(month) > DateManager.DECEMBER;
						  
		if(isMonth) {
			JOptionPane.showMessageDialog(this.frame,"월을 MM 형식으로 입력 해주세요", "다시 입력", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		MainFrame.getInstance().btnCurrentDateSetText(Integer.parseInt(year), Integer.parseInt(month));
		
		setVisible();
	}

}
