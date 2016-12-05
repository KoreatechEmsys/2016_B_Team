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
	<div data-role="page" id="pageone">
		<div data-role="header" data-theme="b">
			<a style="top:27.5%;left:1%;border-style:none;background-color:#FF9867;" href="리스트 주소" class="ui-btn ui-icon-bars ui-corner-all ui-btn-icon-notext "></a>
			<h1 style="font-family:'Malgun Gothic'; font-weight: bold;">오늘의 밥상</h1>
			<a style="top:27.5%;right:1%;border-style:none;background-color:#FF9867;"href="검색주소" class="ui-btn ui-icon-search ui-corner-all ui-btn-icon-notext "></a>

			<div data-role="navbar">
				<ul>
					<li style="background-color: #FF9867;"><a href="http://1.214.224.104/bob/getList.php" action="getList.php" class="ui-btn-active">최신순</a></li>
					<li><a href="#">인기순</a></li>
					<li><a href="#">가격순</a></li>
					<li><a href="#">테마</a></li>
					<li><a href="#">게시판</a></li>
				</ul>
			</div>
			<div data-role="navbar">
				<ul>
					<li style="background-color: #FF9867;"><a href="#" action="getList.php" class="ui-btn-active">밥상검색</a></li>
					<li><a href="http://localhost/bob/addRecipeForm.php">밥상 차리기</a></li>
				</ul>
			</div>
			<div id="food_name" style="padding-left:15px;padding-top:20px;padding-bottom:20px;border-bottom:1px solid;border-top:3px solid;border-right:3px solid;border-left:3px solid;border-color:#D8D8D8;">
				<img id="bob" src="/img/bob/rice_icon.jpg" border="0" align="absmiddle">
				<a style="color:black;text-shadow: 0 1px 0 #f3f3f3;">레시피 사진을 추가해주세요.</a>
			</div>
				<div style="padding-top:20px; padding-left:20px;">
					<img id="addImage" src="/img/bob/ori_addImage.png" style="width:40%;">
					<?php
						$key1 = addslashes($_POST['recipe_name']); 
						$key2 = addslashes($_POST['re_type']); 
						echo $key1; //1
						echo $key2; //1
					?>
				</div>
		</div>
	</div>

</body>   
</html>