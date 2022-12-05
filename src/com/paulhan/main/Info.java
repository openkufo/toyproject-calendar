package com.paulhan.main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Info {
	private static final String FILE_NAME = "달력";
	private static final String VERSION = "1.0.0";
	
	public static final int DAY = 15;
	public static final int WEEK = 20;
	public static final int CURRENT_DATE = 40;
	public static final int COLOR_PANEL = 0xeaf2f4;
	public static final int COLOR_CURRENT_DATE = 0xffffff;
	public static final int COLOR_ARROW = 0xFEFFEB;
	public static final int COLOR_SUNDAY_BACKGROUND = 0xFFF3F1;
	public static final int COLOR_SUNDAY_FOREGROUND = 0xFE9882;
	public static final int COLOR_SATURDAY_BACKGROUND = 0xEBFAFA;
	public static final int COLOR_SATURDAY_FOREGROUND = 0x89E0E6;
	public static final int COLOR_WEEKS_BACKGROUND = 0xD3D2D1;
	public static final int COLOR_WEEKS_FOREGROUND = 0xffffff;
	public static final int COLOR_DAYS_BACKGROUND = 0xffffff;
	public static final int COLOR_DAYS_FOREGROUND = 0x565656;
	public static final int COLOR_ANOTHER_DAYS_BACKGROUND = 0xE5E4E2;
	public static final int COLOR_ANOTHER_DAYS_FOREGROUND = 0x565656;
	
	public static String getTitle() {
		return FILE_NAME + " v " + VERSION;
	}
	
	public static void setFont(Object obj, int style, int size) {
		if(obj instanceof JButton) {
			((JButton) obj).setFont(new Font("맑은 고딕", style, size));
		} else if(obj instanceof JPanel) {
			((JPanel) obj).setFont(new Font("맑은 고딕", style, size));
		}
	}
	
	public static void setBackground(Object obj, int color) {
		if(obj instanceof JButton) {
			((JButton) obj).setBackground(new Color(color));
		} else if(obj instanceof JPanel) {
			((JPanel) obj).setBackground(new Color(color));
		}
	}
	
	public static void setForeground(Object obj, int color) {
		if(obj instanceof JButton) {
			((JButton) obj).setForeground(new Color(color));
		} else if(obj instanceof JPanel) {
			((JPanel) obj).setForeground(new Color(color));
		}
	}

}
