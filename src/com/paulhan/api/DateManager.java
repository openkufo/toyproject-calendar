package com.paulhan.api;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateManager {
	
	private static DateManager dateManager;
	public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;
    
	public static final int WEEK_UNIT = 7;
	
	private List<Integer> anotherMonthIndex = new ArrayList<Integer>();
	
	public static DateManager getInstance() {
		if(dateManager == null) {
			dateManager = new DateManager();
		}
		return dateManager;
	}
	
	private DateManager() {	}
	
	public List<Integer> getAnotherMonthIndex() {
		return this.anotherMonthIndex;
	}
	
	public void setAnotherMonthIndex() {
		this.anotherMonthIndex.clear();
	}
	
	/*
	 * 지정한 포맷으로 현재 날짜 반환
	 * @param	format	'yyyyMMdd' 같은 형식
	 * @return	String	지정한 형식대로 반환
	 */
	public String getCurrentDate(String format) {
		// https://hianna.tistory.com/607
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		String now = localDate.format(dateTimeFormatter);	
		return now;
	}
	
	/*
	 * 대체 함수가 있어 Deprecated 처리
	 * 해당 날짜의 요일 반환
	 * @param	date		해당 날짜
	 * @return	LocalDate	요일 반환
	 */
	@Deprecated
	public int getDayOfWeek(LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return dayOfWeek.getValue();
	}
	
	/*
	 * 1년부터 현재년도까지의 총 일수를 반환
	 * @param	year	연도
	 * @return	int		총 일수
	 */
	public int getTotalOfDays(int year) {
		int lastYear = year-1;
		return lastYear * 365 + lastYear / 4 + lastYear / 400 - lastYear / 100;
	}
	
	/*
	 * 지정한 달까지 총 일수를 계산해서 첫번째 날이 무슨 요일인지 판단
	 * @param	year	지정한 년도
	 * @param	month	지정한 달
	 * @return	int		요일 넘버링을 반환
	 * 					0 - 월요일
	 * 					1 - 화요일
	 * 					. . .
	 * 					6 - 일요일
	 */
	public int getDayOfWeek(int year, int month) {
		int totalDay = getTotalOfDays(year);
		for (int i = 1; i < month; i++) {
			totalDay += getDayOfMonth(year, i);
		}
		
		return totalDay % WEEK_UNIT;
	}
	
	/*
	 * 요일 출력
	 * @param	week	요일
	 * @return	String	"일", "월" ... "토"
	 */
	public String getWeekToString(int week) {
		String[] weekArray = { "일", "월", "화", "수", "목", "금", "토" };
		return weekArray[week];
	}
	
	/*
	 * 해당 월의 총 일수를 반환
	 * @param	year	연도
	 * @param	month	월(1월, 2월 ... 12월 등)
	 * @return	int		해당 월의 총 일수
	 */
	public int getDayOfMonth(int year, int month) {
		int[] days = new int[]{
		//	1월,	2월,	3월,	4월,	5월,	6월,	7월,	8월,	9월,	10월, 11월, 12월
			31, 28, 31, 30, 31, 30, 31, 31, 30, 31,  30,   31
		};
		
		if(month == 0) {
			month = 12;
		}
		
		if(month == 13) {
			month = 1;
		}
		
		if(month == 2 && isLeapYear(year)) {
			days[month-1]++;
		}
		
		return days[month-1];
	}
	
	/*
	 * 윤년인지 판단
	 * @param	year	연도
	 * @return	int		윤년이면 true
	 */
	public boolean isLeapYear(int year) {
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			return true; // 29
		} else {
			return false; // 28
		}
	}

	/*
	 * 달력의 일자를 ArrayList로 변경
	 * ex) 2022년 8월이면 7월 31일, 1일, 2일, 3일 ... 9월 10일 저장
	 * @param  String[]	0번 인덱스엔 년도,
	 * 					1번 인덱스엔 달이 저장됨
	 * @param  int		달력의 크기가 저장된 파라미터
	 * @return dayList	ArrayList로 변경된 달력 일자.
	 */
	public List<String> getDayToList(String[] date, int calendarSize) {
		final int yearIndex		= 0;
		final int monthIndex	= 1;
		
		List<String> dayList = new ArrayList<String>(calendarSize);
		
		int year	= getNumber(date[yearIndex]);
		int month	= getNumber(date[monthIndex]);
		int week	= getDayOfWeek(year, month) + 1;	// 일요일을 0으로 세팅하기위해 +1
		int day 	= 1;
		
		int prevDayOfMonth		= getDayOfMonth(year, month-1);
		int currentDayOfMonth 	= getDayOfMonth(year, month);
		
		for(int i = 0; i < calendarSize; i++) {
			dayList.add(null);
		}
	
		/*
		 * 해당 월의 시작 요일이 수요일이면
		 * 2부터 시작, 0이상까지 반복 / 이전달의 마지막 날짜부터 1씩 감소시켜 달력 앞부분 채우기
		 * 
		 * 해당 월의 시작 요일이
		 * 	일요일이면 List의 0번
		 * 	화요일이면 List의 2번
		 * 	...
		 * 	토요일이면 List의 6번
		 * 부터 시작, 해당 월의 마지막 날짜까지 반복(윤년까지 계산)
		 * 
		 * 해당 월의 마지막 날짜의 index + week 부터 마지막 calendarSize or dayList.size()까지 반복시켜 1씩 증가하여 채우기
		 */
		
		// 전달의 일수를 계산
		for(int i = week-1; i >= 0; i--) {
			dayList.set(i, (prevDayOfMonth--) + "일");
			this.anotherMonthIndex.add(i);
		}
		
		// 해당하는 달의 일수를 계산
		for(int i = week; i < currentDayOfMonth + week; i++) {
			dayList.set(i, (day++) + "일");
		}
		
		// 다음달의 일수를 계산
		day = 1;
		for(int i = currentDayOfMonth + week; i < dayList.size(); i++) {
			dayList.set(i, (day++) + "일");
			this.anotherMonthIndex.add(i);
		}
		
		return dayList;
	}
	
	/*
	 * 숫자가 아닌 문자열이 있다면 모두 제거하는 메서드
	 * ex) 2022년 -> 2022
	 * @param	str	검사할 문자열
	 * @return	int 모두 제거한 후 남은 숫자 반환
	 */
	public int getNumber(String str) {
		return Integer.parseInt(str.replaceAll("[^0-9]", "")); 
	}
}