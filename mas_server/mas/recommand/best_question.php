<?php
	include "connect_db.php"; 
	require_once("recommend.php");

	$response = array();
	$re = new Recommend();

	// check for required field
	$m_id = 'test@a';

	$user_score = array();

	$score_result = mysql_query("select * from my_map;");

	if(mysql_num_rows($score_result)){										//사용자 관심도를 인출하여 각 유저당 키워드에 해당하는 관심도 할당
		while($row=mysql_fetch_assoc($score_result)){
			$user_id = $row[m_id];
			$user_keyword = $row[keyword];
			$user_rate = $row[rate];
			$user_score[$user_id][$user_keyword] = $user_rate;
		}
	}
	// print_r($user_score);
	// $score_result = $re->getRecommendations($user_score, $m_id);

	print_r($re->getRecommendations($user_score, $m_id));

	// $correct_keyword = key($score_result);									//가장 관심도가 높은 키워드 추출

	// echo $correct_keyword;

	$result = mysql_query("select * from question_list where keyword like '%$correct_keyword%';");         //답변수 숫자 출력
    
    if(mysql_num_rows($result)){
        while($row=mysql_fetch_assoc($result)){
            $response['best'][]=$row;
        }
    }
	
	// echo json_encode($response);
	mysql_close($con);
?>