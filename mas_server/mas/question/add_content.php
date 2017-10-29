<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
include "connect_db.php"; 

// check for required fields

if (isset($_POST['email']) && isset($_POST['title']) && isset($_POST['img_url']) && isset($_POST['content']) && isset($_POST['keyword']) && isset($_POST['level'])) {
    $email = $_POST['email'];
    $title = $_POST['title'];
    $keyword = $_POST['keyword'];
    $content = $_POST['content'];
    $img_url = $_POST['img_url'];
    $level = $_POST['level'];
 	mysql_query("set names utf8", $connect);

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO question_list(m_id, title, q_content, q_img_url, keyword, difficulty) values ('$email', '$title', '$content', '$img_url', '$keyword', $level);");

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