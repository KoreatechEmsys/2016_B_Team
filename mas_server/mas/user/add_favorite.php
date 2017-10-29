<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
include "connect_db.php"; 
// check for required fields

if ( isset($_POST['email']) && isset($_POST['my_email']) ) {
    $favorite = $_POST['email'];
    $my_email = $_POST['my_email'];

 	mysql_query("set names utf8", $con);
    mysql_query("update member set favorite_member = concat(favorite_member, ',', '$favorite') where m_id = '$my_email';");
}
?>