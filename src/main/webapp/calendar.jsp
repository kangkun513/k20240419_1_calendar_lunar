<%@page import="com.tjoeun.myCalendar.LunarDate"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tjoeun.myCalendar.SolaToLunar"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tjoeun.myCalendar.MyCalendar"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>만년 달력</title>

<style type="text/css">

	table {
		/* border: 선 두께, 선 종류, 선 색상 */
		border: 5px solid pink; /* 단축 속성 */
	}

	tr {
		height: 70px; /* 높이 */
		border-width: 0px;
	}
	
	th {
		font-size: 20px;
		width: 80px; /* 너비 */
		border-width: 0px;
		background-color: #CCFFEE;
	}

	th#title {
		font-size: 30px; /* 글꼴 크기 */
		font-family: D2Coding; /* 글꼴 이름 */
		font-weight: bold; /* 굵게 */
		color: teal; /* 글자 색 */
	}
	
	th#sunday {
		color: red;
	}
	
	th#saturday {
		color: blue;
	}
	
	td {
		text-align: right; /* 수평 정렬 */
		vertical-align: top;
		font-weight: bold;
		border-width: 5px solid black;
		border-radius: 10px;
		background-color: #CCFFEE;
	}
	
	td.priv {
		color: #DCDCDC;
		background-color: #FFFFFF;
	}
	
	td.sun {
		color: red;
	}
	
	td.privsun {
		color: #FFCCCC;
		background-color: #FFFFFF;
	}
	
	td.sat {
		color: blue;
	}
	
	td.privsat {
		color: #CCCCFF;
		background-color: #FFFFFF;
	}
	
	td#choice {
		text-align: center;
		vertical-align: middle;
		background-color: white;
	}
	
	td.holiday {
		color: red;
		background-color: #FDE8EC;
	}
	
	span {
		font-size: 12px;
	}
	
	/*
		하이퍼링크 스타일 지정
		link: 1번도 클릭하지 않은 하이퍼링크
		visited: 1번 이상 클릭한 하이퍼링크
		hover: 하이퍼링크에 마우스를 올리고 있을 때
		active: 하이퍼링크를 마우스로 누르고 있을 때
		
		a:link {
		color: black;
		text-decoration: none;
		}
		
		a:visited {
		color: black;
		text-decoration: none;
		}
		
		a:link와 a:visited에 같은 스타일이 적용되므로 ","로 나열해서 스타일을 지정할 수 있음
		a:link, a:visited {
		color: black;
		text-decoration: none;
		}
	*/
	
	a {
		color: black;
		text-decoration: none; /* 텍스트 장식 선*/
	}
	
	a:hover {
		color: hotpink;
		text-decoration: underline;
	}
	
	a:active {
		color: yellow;
		text-decoration: underline;
	}
	
	.button {
	  background-color: #04AA6D; /* Green */
	  border: none;
	  color: white;
	  padding: 16px;
	  text-align: center;
	  text-decoration: none;
	  display: inline-block;
	  font-size: 12px;
	  margin: 0px;
	  transition-duration: 0.4s;
	  cursor: progress;
	}
	
	.button1 {
	  background-color: white;
	  color: black;
	  border: 2px solid #DC143C;
	}
	
	.button1:hover {
	  background-color: #DC143C;
	  color: black;
	  border: 2px solid white;
	}

	.button2 {
	  background-color: white;
	  color: black;
	  border: 2px solid #008CBA;
	}
	
	.button2:hover {
	  background-color: #008CBA;
	  color: black;
	  border: 2px solid white;
	}
	
	.select{
		width: 100px;
		height: 30px;
	}
	
	fieldset {
		width: 100px;
		display: inline;
	}
		
	
</style>

</head>
<body>

<%
//	달력 메소드 테스트
//	out.println(MyCalendar.isLeapYear(2023));
//	out.println(MyCalendar.lastDay(2023,12));
//	out.println(MyCalendar.totalDay(2023,12,26));
//	out.println(MyCalendar.weekDay(2023,12,26));

//	컴퓨터 시스템의 년, 월을 얻어옴
	Date date = new Date();
	int year = date.getYear() + 1900;
	int	month = date.getMonth() + 1;
	Calendar calendar = Calendar.getInstance();
	
//	이전 달, 다음 달 하이퍼링크 또는 버튼을 클릭하면 get() 방식으로 넘어오는 달력을 출력할 년, 월을 받음
//	달력이 최초 실행되면 이전 페이지가 존재하지 않기 때문에 넘어오는 데이터가 없으므로
//	year와 month가 null이기 때문에 NumberFormatException이 발생 → 예외 처리
	try {
		year = Integer.parseInt(request.getParameter("year"));
		month = Integer.parseInt(request.getParameter("month"));
		
		//	month에 13이 넘어오면 달력에 다음 해 1월을 출력해야 하고 month에 0이 넘어왔으면 전 년도 12월을 달력에 출력
		if(month >= 13){
			year++;
			month = 1;
		} else if(month <= 0){
			year--;			
			month = 12;
		}
	} catch(NumberFormatException e) {
		
	}

//	달력을 출력할 달의 양력/음력 대응 결과 얻어옴
	ArrayList<LunarDate> lunarDate = SolaToLunar.solaToLunar(year, month);
%>

<table border="1" width="700" align="center" cellspacing="0" cellpadding="5">
	<tr>
		<th>
		<!-- 
			<a> 태그가 설정된 문자열을 클릭하면 href 속성의 지정된 곳으로 이동
			href 속성 "#" 뒤에 id(해시)를 지정하면 현재 문서에서 id가 지정된 요소(책갈피)로 이동
			url이 지정되면 지정된 url로 이동
			"?" 뒤에 이동하려는 페이지로 데이터를 넘겨줄 수 있는 데 이 때 넘겨줄 데이터가 2건 이상이라면
			데이터와 데이터 사이에 "&" 넣어서 구분
			"?" 뒤에 절대로 띄어쓰기를 하면 안됨
		-->
			<%-- <a href="?year=<%=year%>&month=<%=month - 1%>">이전 달</a> --%>
			<%-- <input class="button button1" value="이전 달" onclick="location.href='?year=<%=year%>&month=<%=month - 1%>'"> --%>
			<button class="button button1" onclick="location.href='?year=<%=year%>&month=<%=month - 1%>'">이전 달</button>
			</th>
			<th id="title" colspan="5">
			<%=year%>년 <%=month%>월
		</th>
		<th>
			<%-- <a href="?year=<%=year%>&month=<%=month + 1%>">다음 달</a> --%>
			<!-- 
			<button> 태그는 type 속성 생략 시 submit이 기본값이므로 onclick와 같은 이벤트를 사용하려면 type 속성을 button으로 변경
			-->
			<%-- <input type="button" value="다음 달" onclick="location.href='?year=<%=year%>&month=<%=month + 1%>'"> --%>
			<button class="button button2" onclick="location.href='?year=<%=year%>&month=<%=month + 1%>'">다음 달</button>
		</th>
	</tr>
	<tr>
		<th id="sunday">일</th>
		<th>월</th>
		<th>화</th>
		<th>수</th>
		<th>목</th>
		<th>금</th>
		<th id="saturday">토</th>
	</tr>
	<!-- 달력에 날짜를 출력 -->
	<tr align="center">
<%
//	1일이 출력될 위치(요일)를 맞추기 위해 달력을 출력할 달 1일의 요일만큼 반복하며 빈 칸 출력
//	for (int i = 0; i < MyCalendar.weekDay(year, month, 1); i++){
//		out.println("<td>&nbsp;</td>");
//	}

		
//	1일이 출력될 위치(요일)를 맞추기 위해 달력을 출력할 달 1일의 요일만큼 반복하며 전 달 날짜 출력
	for (int i = MyCalendar.weekDay(year, month, 1); i > 0; i--){
		if(month == 1){ // 1월
			if(i == MyCalendar.weekDay(year, month, 1)) {
				out.println("<td class='privsun'>12/" + (MyCalendar.lastDay(year - 1, 12) - i + 1) + "</td>");			
			} else {
				out.println("<td class='priv'>12/" + (MyCalendar.lastDay(year - 1, 12) - i + 1) + "</td>");
			}
		} else { // 2 ~ 12월
			if(i == MyCalendar.weekDay(year, month, 1)) {
				out.println("<td class='privsun'>" + (month - 1) + "/" + (MyCalendar.lastDay(year, month - 1) - i + 1) + "</td>");
			} else {
				out.println("<td class='priv'>" + (month - 1) + "/" + (MyCalendar.lastDay(year, month - 1) - i + 1) + "</td>");
			}
		}
	}

//	대체 공휴일 구분 변수
	int rep = 0;
	
//	1일부터 달력을 출력할 달의 마지막 날짜까지 반복하며 날짜를 출력
	for (int i = 1; i <= MyCalendar.lastDay(year, month); i++){
		if (lunarDate.get(i - 1).getHoliday().length() > 0) { // 양력, 음력 공휴일 여부
			out.println("<td class='holiday'>" + i + lunarDate.get(i - 1).getHoliday() + "</td>");
		} else {	
			switch(MyCalendar.weekDay(year, month, i)){
				case 0:
					out.println("<td class='sun'>" + i + "</td>");
					break;
				case 6:
					out.println("<td class='sat'>" + i + "</td>");
					break;
				default:
					out.println("<td>" + i + "</td>");
					break;
			}
		
		}
		
		//	출력한 날짜가 토요일이고 그 달의 마지막 날짜가 아니면 줄을 바꿈
		if(MyCalendar.weekDay(year, month, i) == 6 && i != MyCalendar.lastDay(year, month)) {
			out.println("</tr><tr>");
		}
		
	}

//	날짜를 다 출력하고 남은 빈 칸에 다음 달 날짜 출력
	for (int i = 1; i <= 6 - MyCalendar.weekDay(year, month, MyCalendar.lastDay(year, month)); i++){
		if(month == 12){ // 12월
			if(i == 6 - MyCalendar.weekDay(year, month, MyCalendar.lastDay(year, month))){
				out.println("<td class='privsat'>1/" + i + "</td>");
			} else {
				out.println("<td class='priv'>1/" + i + "</td>");				
			}
			
		} else { // 1 ~ 11월
			if(i == 6 - MyCalendar.weekDay(year, month, MyCalendar.lastDay(year, month))){
				out.println("<td class='privsat'>" + (month + 1) + "/" + i + "</td>");
			} else {
				out.println("<td class='priv'>" + (month + 1) + "/" + i + "</td>");
			}
		}
	}
	
		
%>
	</tr>
	<!--  년, 월을 선택하고 보기 버튼을 클릭하면 선택된 달의 달력으로 한번에 넘어가게 함 -->
	<tr>
		<td id="choice" colspan="7">
			<form action="?">
				<fieldset>
					<legend>년</legend>
					<select class="select" name="year">
<%
	for (int i = 1900; i <= 2100; i++){
		if (calendar.get(Calendar.YEAR) == i){
			out.println("<option selected='selected'>"+ i +"</option>");			
		} else {
			out.println("<option>"+ i +"</option>");
		}
	}
%>
					</select>
				</fieldset>
				<fieldset>
					<legend>월</legend>
					<select class="select" name="month">
<%
	for (int i = 1; i <= 12; i++){
		if (calendar.get(Calendar.YEAR) == i){
			out.println("<option> selected='selected'>"+ i +"</option>");			
		} else {
			out.println("<option>"+ i +"</option>");
		}
	}
%>
					</select>
				</fieldset>
				<input class="select" type="submit" value="보기">
			</form>
		</td>
	</tr>
</table>

</body>
</html>