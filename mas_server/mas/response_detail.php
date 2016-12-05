<?php
  	include "connect_db.php"; 
  	$response = array();
 
	// check for required fields
	if (isset($_POST['qid'])) {
	 
	    $qid = $_POST['qid'];

	    // mysql update row with matched pid
	    $result = mysql_query("select r_id, r_m_id, r_title from response where q_id = $qid");
	 	

	 	if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['response_list'][]=$row;
			}
		}
        echo json_encode($response);
	    
	    mysql_close($con);
	}
	   
?>