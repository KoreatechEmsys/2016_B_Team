<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>

<style>
	#gg {
        width:100%;
        height: 100%;
        border: 1px solid #bcbcbc;
    }
    .gg {
        width:100%;
        height: 100%;
        border: 1px solid #bcbcbc;
    }

	p{
		color:black;
	}

	.ui-header {
		background:#FF9867;
		height:55px
	}

	.ui-header .ui-title, .ui-footer .ui-title {
	    font-size: 20px;
	    min-height: 1.1em;
	    text-align: center;
	    display: block;
	    margin: 0 30%;
	    padding: .7em 0;
	    text-overflow: ellipsis;
	    overflow: hidden;
	    white-space: nowrap;
	    outline: 0!important;
	}
	
	.ui-btn-icon-left:after, .ui-btn-icon-right:after, .ui-btn-icon-top:after, .ui-btn-icon-bottom:after, .ui-btn-icon-notext:after{

		background-position: center center;
   		background-repeat: no-repeat;
   		border-radius: 1em
	}



	.ui-page-theme-b .ui-body-inherit, html .ui-bar-b .ui-body-inherit, html .ui-body-b .ui-body-inherit, html body .ui-group-theme-b .ui-body-inherit, html .ui-panel-page-container-b {
		background:white;
	}
	.ui-body-b {
		text-shadow: 0 1px 0 #f3f3f3;
	}
	h1{
		font-weight: normal;
		font-family:'Malgun Gothic';
	}
	.bob{
		float:left;
	}
	.food_name {
		width: 100%;
		height:auto;
	}
	.ui-field-contain {
	    border-bottom-color: none;
	    border-bottom-color: none;
	    border-bottom-width: none;
	    border-bottom-style: none;
	}
</style>
<script>

		function open_in_frame() {
			for(var i = 0; i<4; i++) {
				document.write('');
			}
		}
	</script>
</head>
<body>

<?php

// A list of permitted file extensions
$allowed = array('png', 'jpg', 'gif','zip');

if(isset($_FILES['upl']) && $_FILES['upl']['error'] == 0){

	$extension = pathinfo($_FILES['upl']['name'], PATHINFO_EXTENSION);

	if(!in_array(strtolower($extension), $allowed)){
		echo '{"status":"error"}';
		exit;
	}

	if(move_uploaded_file($_FILES['upl']['tmp_name'], 'uploads/'.$_FILES['upl']['name'])){
		echo '{"status":"success"}';
		exit;
	}
}

echo '{"status":"error"}';
exit;
?>

</body>   
</html>