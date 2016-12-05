<?php
  	include "connect_db.php"; 

if (isset($_POST['is_id']) && isset($_POST['is_title']) && isset($_POST['is_keyword']) && isset($_POST['is_content']) && isset($_POST['sorting']) && isset($_POST['keyword'])) {
	$is_id = $_POST['is_id'];
    $is_title = $_POST['is_title'];
    $is_keyword = $_POST['is_keyword'];
    $is_content = $_POST['is_content'];
    $sorting = $_POST['sorting'];
    $keyword = $_POST['keyword'];

    $sql = "select * from question_list where "; 

    if($is_id == "true")
    	$sql .= "m_id like '%$keyword%' or ";
    if($is_title == "true")
    	$sql .= "title like '%$keyword%' or ";
    if($is_content == "true")
    	$sql .= "q_content like '%$keyword%' or ";
    if($is_keyword == "true"){
    	$sql .= "keyword like '%$keyword%'";
    }
    else {
    	$sql = substr($sql,0,-4);
    }

	$result = mysql_query($sql);
	$json = array();
	 
	if(mysql_num_rows($result)){
		while($row=mysql_fetch_assoc($result)){
			$json['question_list'][]=$row;
		}
	}

	mysql_close($connect);
	echo json_encode($json); 
}
?>