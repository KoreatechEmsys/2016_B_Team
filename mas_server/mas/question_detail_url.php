<?php
	include "connect_db.php"; 
	$response = array();

		// check for required fields
	if (isset($_POST['qid'])) {

		$qid = urldecode($_POST['data']);

		mysql_query("update question_list set view_count=view_count+1 where q_id = $qid");
		    // mysql update row with matched pid
		$result = mysql_query("select title, m_id, q_content, q_img_url, keyword, view_count, difficulty, response_count from question_list WHERE q_id = $qid");


		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['question_list'][]=$row;
			}
		}

		$result = mysql_query("select r_id, r_m_id, r_title from response where q_id = $qid");


		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['response_list'][]=$row;
			}
		} else {
			$response['response_list'][] = 0;
		}

		echo json_encode($response);

		mysql_close($con);
	}
?>