<?php
  	include "connect_db.php"; 
  	$response = array();
 
	// check for required fields
	if (isset($_POST['qid'])) {
	 
	    $qid = $_POST['qid'];
	    mysql_query("update question_list set view_count=view_count+1 where q_id = $q_id");

	    // mysql update row with matched pid
	    $result = mysql_query("select title, m_id, q_content, q_img_url, keyword from question_list WHERE q_id = $qid");
	 	

	 	if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['question_list'][]=$row;
			}
		}
        echo json_encode($response);
	    
	    mysql_close($con);
	}
	   
?>