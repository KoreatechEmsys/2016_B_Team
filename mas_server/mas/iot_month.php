<?php
    include "connect_db.php"; 


$sql = "select * from iot_project where topic = 'environment/user1' and DATE_FORMAT(recent_time, '%Y-%m') = date_format(now(), '%Y-%m');"; 
$result = mysql_query($sql);
$json = array();
 echo 'qwdqwd';
if(mysql_num_rows($result)){
    echo 'qwdqwd';
while($row=mysql_fetch_assoc($result)){
$json['question_list'][]=$row;
echo 'qwdqwd';
}
}
mysql_close($connect);
echo json_encode($json); 
?>