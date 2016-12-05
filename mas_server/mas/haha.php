<?php
	include "connect_db.php"; 

	$response = array();

		// check for required fields
	if (isset($_POST['qid'])) {

		$qid = $_POST['qid'];
		$m_id = $_POST['m_id'];

		$user_score = array();

		$score_result = mysql_query("select * from my_map");

		if(mysql_num_rows($score_result)){										//사용자 관심도를 인출하여 각 유저당 키워드에 해당하는 관심도 할당
			while($row=mysql_fetch_assoc($score_result)){
				$user_id = $row[m_id];
				$user_keyword = $row[keyword];
				$user_rate = $row[rate];
				$user_score[$user_id][$user_keyword] = $user_rate;
			}
		}

		$correct_keyword = key($score_result);									//가장 관심도가 높은 키워드 추출

		$result = mysql_query("select * from exam where add_user = '$m_id';");         //답변수 숫자 출력
        
        if(mysql_num_rows($result)){
            while($row=mysql_fetch_assoc($result)){
                $response['question_list'][]=$row;
            }
        }
        echo json_encode($response);
		mysql_close($con);
	}
?>