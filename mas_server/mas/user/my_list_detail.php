<?php
  	include "connect_db.php"; 


$sql = "select count(*) as cc from question_list;"; 
$result = mysql_query($sql);
$json = array();
 
if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){
$json['question_list'][]=$row;
}
}

$sql = "select count(*) as cnt from question_list where m_id = 'test@aa';"; 
$result = mysql_query($sql);
 
if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){
$json['question_list'][]=$row;
}
}

mysql_close($con);
echo json_encode($json); 
?>