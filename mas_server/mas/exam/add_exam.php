<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
include "connect_db.php"; 

// check for required fields

if (isset($_POST['m_id']) && isset($_POST['target']) && isset($_POST['start_time']) && isset($_POST['end_time']) && isset($_POST['question_list'])) {
    $m_id = $_POST['m_id'];
    $target = $_POST['target'];                             //이건 파싱
    $start_time = $_POST['start_time'];
    $end_time = $_POST['end_time'];
    $question_list = $_POST['question_list'];
    $e_title = $_POST['e_title'];

    $targetTok =explode(',' , $target);                    //문제를 파싱해서 questionTok에 넣음

    //배열 크기 가져오기 
    $cnt = count($targetTok);

    mysql_query("set names utf8", $connect);

    for($i = 0 ; $i < $cnt ; $i++){
        mysql_query("insert into exam(start_time, end_time, question_list, target, add_user, e_title) values ('$start_time', '$end_time', '$question_list', '$targetTok[$i]', '$m_id', '$e_title');");
    }
}

echo "sibal";
    // mysql inserting a new row
?>