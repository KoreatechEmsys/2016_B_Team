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
 	mysql_query("set names utf8", $con);

    $get_question = mysql_query("select question_list from exam where e_id = $e_id;");


    if(mysql_num_rows($get_question)){
        while($row=mysql_fetch_assoc($get_question)){
            $String = $row[question_list];
        }
    }

    //문자열 자르기 : 배열로 저장된다.
    $strTok =explode(',' , $String);

    //배열 크기 가져오기 
    $cnt = count($strTok);
    for($i = 0; $i < $cnt; $i++){                                                               //해당 eid에 속한 문제 리스트 꺼내오기
        $question_result = mysql_query("select * from question_list where q_id = $strTok[$i];");
        if(mysql_num_rows($question_result)){
            while($row=mysql_fetch_assoc($question_result)){
                $response['question_list'][]=$row;
            }
        }
    }

    $exam_response = mysql_query("select * from exam_response where m_id = '$m_id' and e_id = $e_id;");
    if(mysql_num_rows($exam_response)){
        while($row=mysql_fetch_assoc($exam_response)){
            $response['exam_response'][]=$row;
        }
    }
    echo json_encode($response); 
    mysql_close($connect);
?>