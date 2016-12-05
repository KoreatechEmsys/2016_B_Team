<?php
	
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
		$target_path = 'uploads/' . $id . '/';

		if(is_dir($target_path)){} 
		else {
			umask(0);
			mkdir($target_path, 0777);
		}
		$file_name = basename($_FILES['image']['name']);

	    $target_path = $target_path . basename($_FILES['image']['name']);
	 
	    try {
	        // Throws exception incase file is not being moved
	        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
	            // make error flag true
	            $response['error'] = true;
	            $response['message'] = 'Could not move the file!';
	        }
	 
	        // File successfully uploaded
	        $result_url = $file_upload_url . $id . '/' . basename($_FILES['image']['name']);
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
	 
// Echo final json response to client
	echo $result_url;
?>
