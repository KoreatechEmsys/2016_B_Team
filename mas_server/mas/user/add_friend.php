<?php
  	include "connect_db.php"; 
  	$response = array();

  	if (isset($_POST['m_id'])) {
	    $m_id = $_POST['m_id'];
	    $friend_id = $_POST['friend_id'];

		$result = mysql_query("select profile_img_url from member where m_id = '$friend_id';");			//답변수 숫자 출력
	 	$img_url;
	 	if(mysql_num_rows($result)){
			while($row=mysql_fetch_array($result)){
				$img_url = $row['profile_img_url'];
			}
		}
		mysql_query("insert into friend (m_id, friend_id, img_url) values('$m_id', '$friend_id', '$img_url');");			//답변수 숫자 출력
	}
	mysql_close($con);
	   
?>