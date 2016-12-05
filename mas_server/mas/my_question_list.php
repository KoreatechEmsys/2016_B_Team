<?php
  	include "connect_db.php"; 

$email = $_POST['ID'];
$sql = "select * from question_list where user_addr = $title"; 
$result = mysql_query($sql);
$json = array();
 
if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){
$json['question_list'][]=$row;
}
}
mysql_close($con);
echo json_encode($json); 
?>