<?php
	include "connect_db.php";
//	include "/class/ImageControl.class.php";
//	require_once $dir."/class/ImageControl.class.php";
//	echo 'haha';
	// Path to move uploaded files
	$target_path = "uploads/";
	 
	// array for final json respone
	$response = array();
	 
	// getting server ip address
	$server_ip = '218.150.182.58:2041';
	 
	// final file url that is being uploaded
	$file_upload_url = 'http://' . $server_ip . '/' . 'mas' . '/' . $target_path;
	 
	if (isset($_FILES['image']['name'])) {
		$id = isset($_POST['id']) ? $_POST['id'] : '';
		$target_path = 'uploads/' . $id . '/profile/';

		if(is_dir($target_path)){} 
		else {
                        umask(0);
                        mkdir($target_path, 0777);
                }

	    $target_path = $target_path . basename($_FILES['image']['name']);
	 
	    try {
	        // Throws exception incase file is not being moved
	        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
	            // make error flag true
	            $response['error'] = true;
	            $response['message'] = 'Could not move the file!';
	        }
	 
	        // File successfully uploaded
	        $result_url = $file_upload_url . $id . '/profile/' . basename($_FILES['image']['name']);
	    } catch (Exception $e) {
	        // Exception occurred. Make error flag true
	        $response['error'] = true;
	        $response['message'] = $e->getMessage();
	    }
	} else {
		    // File parameter is missing
	    $response['error'] = true;
	    $response['message'] = 'Not received any file!F';
	}
	$result_url = $result_url;
	 
// Echo final json response to client
	mysql_query("update member set profile_img_url = '$result_url' where m_id = '$id';");			//답변수 숫자 출력
	echo $result_url;
/*
	$imgSize = getimagesize($target_path);
	$imgWidth = $imgSize[0];
	$imgHeight=$imgSize[1];
	 
	//이미지 리사이징
	if($imgWidth>800 || $imgHeight > 800){
		$thumb = new Image($target_path);
		$thumb->width(800);
		$thumb->height(800);
		$thumb->name(basename($_FILES['image']['name']));
		$thumb->save();
	} else {
		if($imgWidth > $imgHeight) {
			$size = $imgHeight;
		} else {
			$size = $imgWidth;
		}
		$thumb = new Image($target_path);
		$thumb->width($size);
		$thumb->height($size);
		$thumb->name(basename($_FILES['image']['name']));
		$thumb->save();
	}
*/
?>