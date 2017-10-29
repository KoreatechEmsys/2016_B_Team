<?php
 
    // array for JSON response
    $response = array();

    mysql_query("set names utf8", $connect);

    include "connect_db.php"; 
    $size = $_POST['size'];
    
    for($i = 0; $i < $size; $i++) {
        $temp = "arrayNum" . $i;
        $target = $_POST[$temp];
        $targetTok =explode(',' , $target);                    //문제를 파싱해서 questionTok에 넣음

        //배열 크기 가져오기 
        $cnt = count($targetTok);
        $sql = "insert into exam_response(content, e_id, img_url, m_id, ori_q_id, q_id) values ('$targetTok[0]', $targetTok[1], '$targetTok[2]', '$targetTok[3]', $targetTok[4], $targetTok[5]);";
        echo $i . "번째 " . $sql;
        mysql_query("insert into exam_response(content, e_id, img_url, m_id, ori_q_id, q_id) values ('$targetTok[0]', $targetTok[1], '$targetTok[2]', '$targetTok[3]', $targetTok[4], $targetTok[5]);");
    }
    
    mysql_close($connect);
?>