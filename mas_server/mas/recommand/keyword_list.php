<?php
  	include "connect_db.php"; 

$sql = "select name from knowledge_map"; 
$result = mysql_query($sql);
$json = array();
 
if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){
$json['keyword_list'][]=$row;
}
}
mysql_close($connect);
echo json_encode($json); 
?>