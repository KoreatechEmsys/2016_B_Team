<?php

require_once("recommend.php");
include "connect_db.php"; 
require_once("sample_list.php");


$re = new Recommend();
$roro = new Recommend();
$a = array();
$a = $re->getRecommendations($books, "jill");
// print_r($re->getRecommendations($books, "jill"));




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

// print_r($user_score);
// echo '<br/>';
// print_r($books);
// print_r($re->getRecommendations($books, 'jill'));
	$score_result = $roro->getRecommendations($user_score, 'testid1234');
	$correct_keyword = key($score_result);

	echo $correct_keyword;


// $b = key($a);
// echo $b;
?>
