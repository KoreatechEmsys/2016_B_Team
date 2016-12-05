<?php
        include "connect_db.php";
        require_once("recommend.php");
        $re = new Recommend();
$m_id = $_POST['m_id'];


$sql = "select * from question_list order by reg_date DESC";
$json = array();
$result = mysql_query($sql);

if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){
$json['question_list'][]=$row;
}
}
$result = mysql_query("select * from my_level where m_id = '$m_id';");

if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){
$json['map_list'][]=$row;
}
}

                $m_id = $_POST['m_id'];

                $user_score = array();

                $score_result = mysql_query("select * from my_map");

                if(mysql_num_rows($score_result)){                                                                              //사용자 관심도를 인출하여 각 유저당 키워드에 
                        while($row=mysql_fetch_assoc($score_result)){
                                $user_id = $row[m_id];
                                $user_keyword = $row[keyword];
                                $user_rate = $row[rate];
                                $user_score[$user_id][$user_keyword] = $user_rate;
                        }
                }
                $score_result = $re->getRecommendations($user_score, $m_id);

                $correct_keyword = key($score_result);                                                                  //가장 관심도가 높은 키워드 추출

                $member_level = mysql_query("select * from my_level where keyword = '$correct_keyword' and m_id = '$m_id';");

                if(mysql_num_rows($member_level)){                                                                              //사용자 관심도를 인출하여 각 유저당 키워드에 
                        while($row=mysql_fetch_assoc($member_level)){
                                $user_level = $row[grade];
                        }
                }
                $user_level = $user_level + 1;

                $result = mysql_query("select * from question_list where keyword like '%$correct_keyword%' and difficulty < $user_level order by difficulty desc;");

        if(mysql_num_rows($result)){
            while($row=mysql_fetch_assoc($result)){
                $json['best'][]=$row;
            }
        }else {
            $result = mysql_query("select * from question_list where keyword like '%$correct_keyword%' and difficulty > $user_level order by difficulty desc;");
            if(mysql_num_rows($result)){
                while($row=mysql_fetch_assoc($result)){
                    $json['best'][]=$row;
                }
            }else {
                $json['best'][]=1;
            }
        }
mysql_close($connect);
echo json_encode($json);
?>

