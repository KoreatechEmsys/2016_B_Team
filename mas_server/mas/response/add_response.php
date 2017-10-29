<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
include "connect_db.php"; 
// check for required fields

if ( isset($_POST['email']) && isset($_POST['img_url']) && isset($_POST['title']) && isset($_POST['content']) && isset($_POST['qid']) ) {
    $qid = $_POST['qid'];
    $email = $_POST['email'];
    $title = $_POST['title'];
    $content = $_POST['content'];
    $img_url = $_POST['img_url'];
    $m_id = $email;
 	mysql_query("set names utf8", $con);
    mysql_query("update question_list set response_count=response_count+1 where q_id = $qid");

    $update_result = mysql_query("select title, m_id, q_content, q_img_url, keyword, view_count, difficulty, response_count from question_list WHERE q_id = $qid");

        if(mysql_num_rows($update_result)){
            while($row=mysql_fetch_assoc($update_result)){
                $response['question_list'][]=$row;
                $String = $row[keyword];
            }
        }
        //문자열 자르기 : 배열로 저장된다.
        $strTok =explode(',' , $String);

        //배열 크기 가져오기 
        $cnt = count($strTok);
        for($i = 0; $i < $cnt; $i++){
            $type_result = mysql_query("select type from knowledge_map where name = '$strTok[$i]';");
            if(mysql_num_rows($type_result)){
                while($row=mysql_fetch_assoc($type_result)){
                    $type_num = $row[type];
                }
            }
            $temp_result = mysql_query("select * from my_map where keyword = '$strTok[$i]' and m_id = '$m_id'");
            if(mysql_num_rows($temp_result)){
                mysql_query("update my_map set rate=rate+1 where keyword = '$strTok[$i]' and m_id = '$m_id'");
            } else {
                mysql_query("insert into my_map(m_id, keyword,type) values ('$m_id', '$strTok[$i]', $type_num);");
            }
        }

    $result = mysql_query("insert into response (q_id, r_m_id, r_title, r_content, r_img_url) values ($qid, '$email', '$title', '$content', '$img_url');");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo 'hahaha';
}
?>
