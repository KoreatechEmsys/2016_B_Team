<?php
  	include "connect_db.php"; 
  	$response = array();

  	if (isset($_POST['m_id'])) {
	    $m_id = $_POST['m_id'];
	    $friend_id = $_POST['friend_id'];

	    mysql_query("update member set favorite_member = concat(favorite_member, '$friend_id') where m_id = '$m_id';");
	}
	mysql_close($con);
	   
?>