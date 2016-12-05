<?php
    $response = array();
     
    include "connect_db.php"; 
    $m_id = $_POST['m_id'];
    $q_id = $_POST['q_id'];
 	mysql_query("set names utf8", $con);

    mysql_query("delete from response where q_id = $q_id;");
    mysql_query("delete from question_list where q_id = $q_id;");
	mysql_query("delete from exam_response where ori_q_id = $q_id;");
	mysql_query("delete from exam where question_list like %$q_id%;");


    mysql_close($connect);
?>
