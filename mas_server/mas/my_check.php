<?php
    include "connect_db.php"; 
    $response = array();
 
    // check for required fields
    if (isset($_POST['m_id'])) {
     
        $m_id = $_POST['m_id'];

        $result = mysql_query("select * from exam where add_user = '$m_id';");         //답변수 숫자 출력
        
        if(mysql_num_rows($result)){
            while($row=mysql_fetch_assoc($result)){
                $response['question_list'][]=$row;
            }
        }
    }

    echo json_encode($response);
    mysql_close($con);
       
?>