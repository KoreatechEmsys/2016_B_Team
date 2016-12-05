<?php
  	include "connect_db.php"; 
  	$response = array();

  	if (isset($_POST['target'])) {
	    $target = $_POST['target'];
	    $targetTok =explode(',' , $target);                    //문제를 파싱해서 questionTok에 넣음

        //배열 크기 가져오기 
        $cnt = count($targetTok);
        $sql = "select * from member where m_id = ";
        
        for($i = 0; $i < $cnt - 1; $i++) {
        	$sql = $sql . "'" . $targetTok[$i] . "'" . " or m_id = ";
        }
        $sql = $sql . "'" . $targetTok[$i] . "'" . ";";
        $result = mysql_query($sql);

        if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['member_list'][]=$row;
			}
		}
	}
	echo json_encode($response);
	mysql_close($con);
?>