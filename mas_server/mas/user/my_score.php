<?php
    include "connect_db.php"; 
    $response = array();
    $e_idArray = array();
    $i = 0;
 
    // check for required fields
    if (isset($_POST['m_id'])) {
     
        $m_id = $_POST['m_id'];

        $result = mysql_query("select distinct e_id from exam_response where m_id = '$m_id';");         //답변수 숫자 출력
        
        if(mysql_num_rows($result)){
            while($row=mysql_fetch_assoc($result)){
                $e_idArray[$i] = $row[e_id];
                $i++;
            }
        }

        for($j = 0; $j < $i; $j++) {
            $result = mysql_query("select * from exam where e_id = '$e_idArray[$j]';");         //답변수 숫자 출력
        
            if(mysql_num_rows($result)){
                while($row=mysql_fetch_assoc($result)){
                    $response['exam_list'][]=$row;
                }
            }
        }
    }

    echo json_encode($response);
    mysql_close($con);
       
?>
