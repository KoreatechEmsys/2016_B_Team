<?php
  	include "connect_db.php"; 
  	$response = array();
 
	// check for required fields
	if (isset($_POST['rid'])) {
	 
	    $rid = $_POST['rid'];

	    mysql_query("update response set view_count=view_count+1 where r_id = $rid");
	    // mysql update row with matched pid
	    $result = mysql_query("select * from response where r_id = $rid");
	 	

	 	if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['response'][]=$row;
				$add_user = $row[r_m_id];
			}
		}

		$result = mysql_query("select profile_img_url from member where m_id = '$add_user'");


		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['profile'][]=$row;
			}
		} else {
			$response['profile'][] = 0;
		}
	}
	echo json_encode($response);
	mysql_close($con);
	   
?>