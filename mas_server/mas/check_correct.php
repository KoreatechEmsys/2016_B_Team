<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
    $response = array();
     
    include "connect_db.php"; 
    // check for required fields
    $e_id = $_POST['e_id'];
    $m_id = $_POST['m_id'];
    $q_id = $_POST['q_id'];
    $is_correct = $_POST['is_correct'];
 	mysql_query("set names utf8", $con);

    if($is_correct == '1') {                //정답
        mysql_query("update exam_response set correct=1 where ori_q_id = $q_id and m_id = '$m_id' and e_id = $e_id;");
    } else if($is_correct == '2') {         //틀림
        mysql_query("update exam_response set correct=2 where ori_q_id = $q_id and m_id = '$m_id' and e_id = $e_id;");
    }

    mysql_close($connect);
?>