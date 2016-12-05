<?php 
require_once $dir."/class/ImageControl.class.php";
include "connect_db.php"; 
$imgSize = getimagesize('uploads/test@aa/99.jpg');
$imgWidth = $imgSize[0];
$imgHeight=$imgSize[1];
 
 $result = mysql_query("select title, m_id, q_content, q_img_url, keyword, view_count, difficulty, response_count from question_list WHERE q_id = 48");

if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$response['question_list'][]=$row;
				echo $row[m_id];
			}
		}
//이미지 리사이징
if($imgWidth>500){
	$thumb = new Image('uploads/test@aa/99.jpg');				//파일 이름
	$thumb->width(100);
	$thumb->name('gaga');
	$thumb->save();
	} 
?>