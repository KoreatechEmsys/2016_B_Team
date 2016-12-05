<?php
 
$response = array();
 
include "connect_db.php"; 


	date_default_timezone_set("Asia/Seoul");		//한국시간
		mysql_query("set names utf8", $connect);
		$topic = $_POST['topic'];

		 $sql = "SELECT * FROM iot_test WHERE topic = 'environment/test1';";

		$result = mysql_query($sql);
		$json = array();
		$time_arr = array();
		$spo2_arr = array();
		$spo2_avg = 0;
		$col_count = 0;
		$arr_count = 0;
		
		$today = strtotime("201606170200");
		$tim = date( 'Y-m-d H:i:s', $today );
			

		if(mysql_num_rows($result)){
			while($row=mysql_fetch_assoc($result)){
				$spo2_arr[] = $row['spo2'];
				$time_arr[] = $row['recent_time'];
			}
		}	

	for ($i=0; $i < count($time_arr); $i++) { 
		$temp = $time_arr[$i];
		$timeString = date( 'Y-m-d H:i:s', strtotime($temp));
		echo $tim;
		
		if ( $tim > $timeString ) {					//tim보다 이전 시간대이면 
			$spo2_avg += (int)$spo2_arr[$i];
			$col_count++;
		} else {									//time보다 이후 시간이면
			$spo2_avg = $spo2_avg / $col_count;
			if($spo2_avg == '')
				$spo2_avg = 'null';
			$huehue = strtotime($tim);
			$json['data'][$arr_count]['spo2'] = $spo2_avg;
			$json['data'][$arr_count]['hour'] = date('H', $huehue);
			$json['data'][$arr_count]['minitue'] = date('i', $huehue);

			$arr_count++;

			$tim = date("Y-m-d H:i:s",strtotime($tim) + 60*10);		//
			
		}
	}


		mysql_close($con);
	//	echo $timeString;
		echo json_encode($json);
		

?>