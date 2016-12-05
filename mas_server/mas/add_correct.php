<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
include "connect_db.php"; 
// check for required fields

// if ( isset($_POST['rid']) ) {                                                                   //power = (before_power + correct_rate(문제난이도)) / x + 1
    $rid = $_POST['rid'];
    $is_correct = $_POST['is_correct'];
    $r_m_id = $_POST['r_m_id'];

 	mysql_query("set names utf8", $con);
    if($is_correct == 0) {
        mysql_query("update response set is_correct = 1 where r_id = $rid");
        $q_result = mysql_query("select q_id from response where r_id = $rid");

        if(mysql_num_rows($q_result)){                                      //사용자 관심도를 인출하여 각 유저당 키워드에 해당하는 관심도 할당
            while($row=mysql_fetch_assoc($q_result)){
                $qid = $row[q_id];
            }
        }

        mysql_query("update question_list set success_count = success_count + 1 where q_id = $qid");

        $rate_result = mysql_query("select * from question_list where q_id = $qid");

        if(mysql_num_rows($rate_result)){                                      //사용자 관심도를 인출하여 각 유저당 키워드에 해당하는 관심도 할당
            while($row=mysql_fetch_assoc($rate_result)){
                $response_count = $row[response_count];
                $success_count = $row[success_count];
                $keyword = $row[keyword];
                $question_level = $row[difficulty];
            }
        }

        $correct_rate = $success_count / $response_count;           //문제 난이도

        $strTok = explode(',' , $keyword);

        //배열 크기 가져오기 
        $cnt = count($strTok);
        for($i = 0; $i < $cnt; $i++){
            $type_result = mysql_query("select * from my_level where m_id = '$r_m_id' and keyword = '$strTok[$i]';");        //내 아이디의 키워드 점수 뽑기
            if(mysql_num_rows($type_result)){
                while($row=mysql_fetch_assoc($type_result)){
                    $solve_count = $row[solve_count];                     //before_power;
                    $grade = $row[grade];
                }
            }
            $solve_count = $solve_count + 1;

            $new_grade = ($grage * ($solve_count - 1) + $question_level) / $solve_count;

            mysql_query("update my_level set solve_count = $solve_count where keyword = '$strTok[$i]' AND m_id = '$r_m_id';");
            mysql_query("update my_level set grade = $new_grade where keyword = '$strTok[$i]' AND m_id = '$r_m_id';");





            mysql_query("update my_map set correct_time = correct_time + 1 where keyword = '$strTok[$i]' AND m_id = '$r_m_id';");
            $type_result = mysql_query("select my_power, correct_time from my_map where keyword = '$strTok[$i]' AND m_id = '$r_m_id';");        //내 아이디의 키워드 점수 뽑기
            if(mysql_num_rows($type_result)){
                while($row=mysql_fetch_assoc($type_result)){
                    $my_power[$i] = $row[my_power];                     //before_power;
                    $correct_time[$i] = $row[correct_time];
                }
            }
            $new_rate[$i] = ($my_power[$i] + $correct_rate) / ($correct_time[$i] + 1);          //갱신된 내 실력0.42857142857143  $new_rate[$i]      
            mysql_query("update my_map set my_power = $new_rate[$i] where m_id = '$r_m_id' AND keyword = '$strTok[$i]';");
        }
    } else {
        mysql_query("update question_list set is_correct = 0 where r_id = $rid");
    }
// } 
mysql_close($con);
?>
