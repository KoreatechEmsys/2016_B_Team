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
	.ui-bar-b{
	    background-color: #FF9867;
	    border-color: #FF9867;
	    color: #fff;
	    /* text-shadow: 0 1px 0 #111; */
	    font-weight: 700;
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
		text-shadow: none;
	}
	h1{
		font-weight: normal;
		font-family:'Malgun Gothic';
	}
	#demo-page * {
	    -webkit-user-select: none;
	    -moz-user-select: none;
	    -ms-user-select: none;
	    -o-user-select: none;
	    user-select: none;
	}

	.ui-panel-inner {
	    padding: 0px;
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
	$( document ).on( "pageinit", "#demo-page", function() {
    $( document ).on( "swipeleft swiperight", "#demo-page", function( e ) {
        // We check if there is no open panel on the page because otherwise
        // a swipe to close the left panel would also open the right panel (and v.v.).
        // We do this by checking the data that the framework stores on the page element (panel: open).
        if ( $.mobile.activePage.jqmData( "panel" ) !== "open" ) {
            if ( e.type === "swipeleft"  ) {
                $( "#right-panel" ).panel( "open" );
            } else if ( e.type === "swiperight" ) {
                $( "#left-panel" ).panel( "open" );
            }
        }
    });
});
</script>
</head>
<body>
	
	<?php
		session_start();

		$m_id = $_POST['m_id'];
		$m_pass = $_POST['m_pass'];

		$_SESSION['ses_userid'] = $m_id;
		$_SESSION['ses_pass'] = $m_pass;

		include "connect_db.php";
		$sql = "select *from member where m_id = '$m_id' and m_pass = '$m_pass' ";
		$res = mysql_query($sql, $connect);
		$list = mysql_num_rows($res);

		if($list)
		{
			$row = mysql_fetch_array($res);
			$ses_userid = $row[userid];
			$ses_pass = $row[passwd];
			echo "로그인을 성공하였습니다. ";
			echo "<script>
			location.replace('main.html');
			</script>";

		}
		else{
			echo "비밀번호가 틀립니다.<br />";
			echo "<script>
			alert('[로그인 실패]\\r\\n 로그인 화면으로 이동할까요?');
			location.replace('login.php');
			</script>";
		}

		mysql_close($connect);
	?>

</body>   
</html>