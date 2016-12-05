<?php
	include "connect_db.php"; 
	$response = array();

		// check for required fields
	if (isset($_POST['eid'])) {

		$eid = $_POST['eid'];

		$result = mysql_query("select question_list from exam WHERE e_id = $eid;");

		$row = mysql_fetch_assoc($result);

		$String = $row[question_list];

		//문자열 자르기 : 배열로 저장된다.
		$strTok =explode(',' , $String);

		//배열 크기 가져오기 
		$cnt = count($strTok);

		$sql = "select * from question_list where q_id = ";
		for($i = 0 ; $i < $cnt - 1 ; $i++){
			$sql = $sql . $strTok[$i] . " or q_id = ";
		}

		$sql = $sql . $strTok[$i] . ";";

		$result = mysql_query($sql);
	 	
	 	if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['question_list'][]=$row;
			}
		}
	}

	echo json_encode($response);
	mysql_close($con);
?>