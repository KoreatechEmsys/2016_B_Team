<?php
	include "connect_db.php"; 
	$response = array();

		// check for required fields
	if (isset($_POST['qid'])) {

		$qid = $_POST['qid'];
		$m_id = $_POST['m_id'];

		mysql_query("update question_list set view_count=view_count+1 where q_id = $qid");
		    // mysql update row with matched pid
		//여기서 부터 가중치 계산 시작
		$result = mysql_query("select * from question_list WHERE q_id = $qid");


		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['question_list'][]=$row;
				$String = $row[keyword];
				$add_user = $row[m_id];
			}
		}

		//문자열 자르기 : 배열로 저장된다.
		$strTok =explode(',' , $String);

		//배열 크기 가져오기 
		$cnt = count($strTok);
		for($i = 0; $i < $cnt; $i++){
			$type_result = mysql_query("select type from knowledge_map where name = '$strTok[$i]';");
			if(mysql_num_rows($type_result)){
				while($row=mysql_fetch_assoc($type_result)){
					$type_num = $row[type];
				}
			}
			$temp_result = mysql_query("select * from my_map where keyword = '$strTok[$i]' and m_id = '$m_id'");
			if(mysql_num_rows($temp_result)){
				mysql_query("update my_map set rate=rate+1 where keyword = '$strTok[$i]' and m_id = '$m_id'");
			} else {
				mysql_query("insert into my_map(m_id, keyword,type) values ('$m_id', '$strTok[$i]', $type_num);");
			}
		}

		//여기까지

		$result = mysql_query("select profile_img_url from member where m_id = '$add_user'");


		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['profile'][]=$row;
			}
		} else {
			$response['profile'][] = 0;
		}

		$result = mysql_query("select r_id, r_m_id, r_title from response where q_id = $qid");


		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['response_list'][]=$row;
			}
		} else {
			$response['response_list'][] = 0;
		}

		 $result = mysql_query("select r_id, r_m_id, r_title from response where q_id = $qid and is_correct = 1;");


                if(mysql_num_rows($result)){
                        while($row=mysql_fetch_assoc($result)){
                                $response['correct_list'][]=$row;
                        }
                } else {
                        $response['correct_list'][] = 0;
                }


		echo json_encode($response);

		mysql_close($con);
	}
?>
