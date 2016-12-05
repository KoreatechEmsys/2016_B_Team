<?php
	$m_id = $_POST['email'];
	$m_pass = $_POST['password'];
	$m_phone = $_POST['phone'];
	$m_name = $_POST['name'];

	include "connect_db.php"; 

	$sql = "select count(*) from member where m_id='$m_id'";
	$res = mysql_query($sql,$connect);
	$rs = mysql_fetch_row($res);
	$reg_num=$rs[0];
	
	if($reg_num>0){
		echo '1';		//1은 아이디 중복 처리
		die;
	}

	$sql = "insert into member(m_id, m_pass, m_phone, m_name, favorite_member)";
	$sql.= "values ('$m_id','$m_pass','$m_phone','$m_name', ',$m_id')";
	$res = mysql_query($sql,$connect);

	$tot_row = mysql_affected_rows();

mysql_query("insert into my_level(m_id, keyword) values ('$m_id','수와연산');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','수의체계');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','정수와유리수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','유리수와순환소수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','제곱근과실수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','소인수분해');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','집합과명제');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','집합');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','명제');");


mysql_query("insert into my_level(m_id, keyword) values ('$m_id','문자와식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','다항식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','방정식과부등식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','문자의사용식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','식의계산');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','다항식의곱셈과인수분해');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','일차방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','이차방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','다항식의연산');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '연립일차방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '나머지정리');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '복소수와이차방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '인수분해');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '이차방정식과이차함수');");

mysql_query("insert into my_level(m_id, keyword) values ('$m_id','함수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','함수와그래프');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','좌표평면과그래프');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','일차함수와그래프');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','이차함수와그래프');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','일차함수와일차방정식의관계');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','유리함수와무리함수');");

mysql_query("insert into my_level(m_id, keyword) values ('$m_id','기하');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','평면도형');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','입체도형');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','기본도형');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','작도와합동');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','평면도형의성질');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','입체도형의성질');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','도형의닮음');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','삼각형과사각형의성질');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '피타고라스정리');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '도형의방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '평면좌표');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '직선의방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '원의방정식');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '도형의이동');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '이차곡선');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '평면벡터');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '공간도형과공간좌표');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '벡터의연산');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '직선과평면');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '평면벡터의성분과내적');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '정사영');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '공간좌표');");

mysql_query("insert into my_level(m_id, keyword) values ('$m_id','확률과통계');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','확률');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','통계');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','확률과기본성질');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','자료정리와해석');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','조건부확률');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','대푯값과산포도');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','상관관계');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','경우의수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '순열과조합');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '확률분포');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '통계적추정');");

mysql_query("insert into my_level(m_id, keyword) values ('$m_id','해석');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','지수와로그');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','삼각함수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','지수함수와로그함수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','함수의극한과연속');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','미분');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','적분');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','미분계수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id','도함수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '부정적분');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '정적분');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '도함수의활용');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '정적분의활용');");

mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '대수');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '수열');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '등차수열과등비수열');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '수열의합');");
mysql_query("insert into my_level(m_id, keyword) values ('$m_id', '수학적귀납법');");


	mysql_close($connect);

	if($tot_row > 0){
		echo '회원등록 성공';		//2는 회원가입 성공
	}	
?>
