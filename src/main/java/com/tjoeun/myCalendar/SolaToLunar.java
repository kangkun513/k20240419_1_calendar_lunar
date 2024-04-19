package com.tjoeun.myCalendar;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SolaToLunar {
	
//	월별 양력과 음력을 크롤링하고 양력, 음력 공휴일을 계산해서 리턴하는 메소드
	public static ArrayList<LunarDate> solaToLunar(int year, int month) {
//		System.out.println("SolaToLunar 클래스의 solaToLunar() 메소드 실행");
//		1 ~ 12월의 양력 날짜에 대응되는 음력 날짜를 기억할 ArrayList 선언함
		ArrayList<LunarDate> lunarList = new ArrayList<LunarDate>();
		String targetsite = "";
		
//		인수로 넘겨받은 year에 해당되는 1 ~ 12월의 양력에 대응되는 음력을 크롤링해서 얻어옴
		targetsite = String.format("https://astro.kasi.re.kr/life/pageView/5?search_year=%04d&search_month=%02d&search_check=G", year, month);
//			System.out.println(targetsite);
		
//		크롤링한 데이터를 기억할 org.jsoup.nodes 패키지의 Document 클래스 객체 선언
		Document document = null;
		
		try {
//			org.jsoup 패키지의 Jsoup 클래스의 connect() 메소드로 크롤링할 타겟 사이트에 접속하고
//			get() 메소드로 타겟 사이트의 정보를 얻어옴
			document = Jsoup.connect(targetsite).get();
//			System.out.println(document);
			
//			Document 클래스 객체에 저장된 타겟 사이트의 정보 중에서 날짜 단위(<tr> 태그)로 얻어옴
//			org.jsoup.select 패키지의 Elements 클래스 객체에 Document 클래스 객체로 읽어들인 내용에서
//			select() 메소드를 사용해서 필요한 정보를 얻어옴
			Elements elements = document.select("tbody > tr");
//			System.out.println(elements);
			
//			Elements 클래스 객체 정리 위해 org.jsoup.node 패키지의 Element 클래스 객체 존재
//			Element 클래스 객체에는 크롤링된 전체 데이터가 저장되어 있음
			for (Element element : elements) {
//				System.out.println(element);
				Elements ele = element.select("td");
//				System.out.println(ele);
				
//				text() 메소드로 태그 내부의 문자열만 얻어옴
//				System.out.print("양력: " + ele.get(0).text());
//				System.out.print(" 음력: " + ele.get(1).text());
//				System.out.print(" 간지: " + ele.get(2).text());
//				System.out.print(" 요일: " + ele.get(3).text());
//				System.out.println(" 율리우스력: " + ele.get(4).text());
				
				String sola = ele.get(0).text();
				String lunar = ele.get(1).text();
//				System.out.println(String.format("양력 %s는 음력으로 %s", sola, lunar));
				
//				크롤링한 결과를 LunarDate 클래스 객체에 저장
				LunarDate lunarDate = new LunarDate();
//				System.out.println(lunar.split(" ")[0].substring(0, 4));
				int year1 = Integer.parseInt(sola.split(" ")[0].substring(0, 4));
				int month1 = Integer.parseInt(sola.split(" ")[1].substring(0, 2));
				int day1 = Integer.parseInt(sola.split(" ")[2].substring(0, 2));
				int year2 = Integer.parseInt(lunar.split(" ")[0].substring(0, 4));
				int month2 = 1;
				try {
//					음력 평달
					month2 = Integer.parseInt(lunar.split(" ")[1].substring(0, 2));
				} catch (NumberFormatException e) {
//					음력 윤달
					month2 = Integer.parseInt(lunar.split(" ")[1].substring(1, 3));
					lunarDate.setLunarFlag(true);
				}
				int day2 = Integer.parseInt(lunar.split(" ")[2].substring(0, 2));
				
				lunarDate.setYear(year1);
				lunarDate.setMonth(month1);
				lunarDate.setDay(day1);
				lunarDate.setYearLunar(year2);
				lunarDate.setMonthLunar(month2);
				lunarDate.setDayLunar(day2);
				
//				System.out.println(lunarDate);
//				1년치 양력 날짜와 양력 날짜에 대응되는 음력 날짜 저장
				lunarList.add(lunarDate);
				
			}
			
		} catch (IOException e) {
			System.out.println("사이트 주소 올바르지 않거나 접속에 문제 발생");
		}
		
//		공휴일 처리
		for (int i=0; i < lunarList.size(); i++) {
			// 양력 공휴일
			if (lunarList.get(i).getMonth() == 1 && lunarList.get(i).getDay() == 1) {
				lunarList.get(i).setHoliday("<br><span>신정</span>");
			} else if (lunarList.get(i).getMonth() == 3 && lunarList.get(i).getDay() == 1) {
				lunarList.get(i).setHoliday("<br><span>삼일절</span>");
			} else if (lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 1) {
				lunarList.get(i).setHoliday("<br><span>근로자의 날</span>");
			} else if (lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 5) {
				lunarList.get(i).setHoliday("<br><span>어린이날</span>");
			} else if (lunarList.get(i).getMonth() == 6 && lunarList.get(i).getDay() == 6) {
				lunarList.get(i).setHoliday("<br><span>현충일</span>");
			} else if (lunarList.get(i).getMonth() == 8 && lunarList.get(i).getDay() == 15) {
				lunarList.get(i).setHoliday("<br><span>광복절</span>");
			} else if (lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 3) {
				lunarList.get(i).setHoliday("<br><span>개천절</span>");
			} else if (lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 9) {
				lunarList.get(i).setHoliday("<br><span>한글날</span>");
			} else if (lunarList.get(i).getMonth() == 12 && lunarList.get(i).getDay() == 25) {
				lunarList.get(i).setHoliday("<br><span>크리스마스</span>");
			}
			
			// 음력 공휴일 → 윤달일 경우 공휴일 아님
			// 음력 1월 1일(설날), 음력 4월 8일(석가탄신일), 음력 8월 15일(추석)
			if (!lunarList.get(i).isLunarFlag()) { // 윤달 여부 확인
				if (lunarList.get(i).getMonthLunar() == 1 && lunarList.get(i).getDayLunar() == 1) {
					try {
						lunarList.get(i-1).setHoliday("<br><span>설 연휴</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
					lunarList.get(i).setHoliday("<br><span>설날</span>");
					try {
						lunarList.get(i+1).setHoliday("<br><span>설 연휴</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
				} else if (lunarList.get(i).getMonthLunar() == 4 && lunarList.get(i).getDayLunar() == 8) {
					lunarList.get(i).setHoliday("<br><span>석가탄신일</span>");
				} else if (lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 15) {
					try {
						lunarList.get(i-1).setHoliday("<br><span>추석 연휴</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
					lunarList.get(i).setHoliday("<br><span>추석</span>");
					try {
						lunarList.get(i+1).setHoliday("<br><span>추석 연휴</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
				}
			}
			
			// 대체 공휴일 → 설날, 삼일절, 석가탄신일, 어린이날, 광복절, 추석, 개천절, 한글날, 크리스마스가
			// 주말(토, 일)이나 다른 공휴일과 겹치면 그 다음 첫번째 비공휴일을 대체 공휴일로 함
			
			// 양력 날짜 요일 계산
			int holiday = MyCalendar.weekDay(year, lunarList.get(i).getMonth(), lunarList.get(i).getDay());
			
			// 설날, 추석 대체 공휴일, 설날, 추석이 주말 또는 월요일과 겹침
			if (!lunarList.get(i).isLunarFlag() && 
				(lunarList.get(i).getMonthLunar() == 1 && lunarList.get(i).getDayLunar() == 1) ||
				(lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 15)) {
//				설날, 추석이 주말과 겹쳤을 때
				if (holiday == 6 || holiday == 0 || holiday == 1) {
					try {
						lunarList.get(i+2).setHoliday("<br><span>대체 공휴일</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
				}
			}
			
			// 석가탄신일 대체 공휴일
			if (!lunarList.get(i).isLunarFlag() && lunarList.get(i).getMonthLunar() == 4 
					&& lunarList.get(i).getDayLunar() == 8) {
				if (holiday == 0) {
					try {
						lunarList.get(i+1).setHoliday("<br><span>대체 공휴일</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
				} else if (holiday == 6) {
					try {
						lunarList.get(i+2).setHoliday("<br><span>대체 공휴일</span>");
					} catch (IndexOutOfBoundsException e) {
						
					}
				}
			}

			// 나머지 대체 공휴일
			if ((lunarList.get(i).getMonth() == 3 && lunarList.get(i).getDay() == 1) ||
				(lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 5) ||
				(lunarList.get(i).getMonth() == 8 && lunarList.get(i).getDay() == 15) ||
				(lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 3) ||
				(lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 9) ||
				(lunarList.get(i).getMonth() == 12 && lunarList.get(i).getDay() == 25)) {
				if (holiday == 0) {
					lunarList.get(i+1).setHoliday("<br><span>대체 공휴일</span>");
				} else if (holiday == 6) {
					lunarList.get(i+2).setHoliday("<br><span>대체 공휴일</span>");
				}
			}
			
			// 어린이날과 석가탄신일이 겹치는 경우
			if (!lunarList.get(i).isLunarFlag() && lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 5 
					&& lunarList.get(i).getMonthLunar() == 4 && lunarList.get(i).getDayLunar() == 8) {
				lunarList.get(i).setHoliday("<br><span>어린이날</span><br><span>석가탄신일</span>");
				if (holiday == 6) {
					lunarList.get(i+2).setHoliday("<br><span>대체 공휴일</span>");
				} else {
					lunarList.get(i+1).setHoliday("<br><span>대체 공휴일</span>");
				}
			}
			
			// 추석과 개천절이 겹치는 경우
			if (!lunarList.get(i).isLunarFlag() && lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 3 
					&& lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 15) {
				lunarList.get(i).setHoliday("<br><span>추석</span><br><span>개천절</span>");
				lunarList.get(i+2).setHoliday("<br><span>대체 공휴일</span>");
			}
			
		}
		
//		1년에 존재하는 모든 공휴일을 처리했으므로 달력 출력에 사용할 달의 정보만 별도로 리턴
//		인수로 넘겨받은 month의 양력에 해당되는 음력을 저장해서 리턴할 ArrayList를 선언함
		ArrayList<LunarDate> list = new ArrayList<LunarDate>();
		for (LunarDate lunarDate : lunarList) {
			if (lunarDate.getMonth() == month) {
				list.add(lunarDate);
			}
		}
//		System.out.println(list);
		return list;
	}
	
}
