<?php
	$m_id = $_POST['email'];
	$m_pass = $_POST['password'];

	include "connect_db.php"; 

	$sql = "select count(*) from member where m_id='$m_id'";
	$res = mysql_query($sql,$connect);
	$rs = mysql_fetch_row($res);
	$reg_num=$rs[0];
	
	if($reg_num>0){
		echo '1';		//1은 아이디 중복 처리
		die;
	}

	$sql = "insert into member (m_id, m_pass)";
	$sql.= "values ('$m_id','$m_pass')";
	$res = mysql_query($sql,$connect);

	$tot_row = mysql_affected_rows();

	mysql_close($connect);

	if($tot_row > 0){
		echo '회원등록 성공';		//2는 회원가입 성공
	}	
?>