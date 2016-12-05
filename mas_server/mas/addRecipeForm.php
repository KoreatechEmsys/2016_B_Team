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
					<li><a href="http://1.214.224.104/bob/addRecipeForm.php">밥상 차리기</a></li>
				</ul>
			</div>
				<div id="food_name" style="padding-left:15px;padding-top:20px;padding-bottom:20px;border-bottom:1px solid;border-top:3px solid;border-right:3px solid;border-left:3px solid;border-color:#D8D8D8;">
					<img id="bob" src="/img/bob/rice_icon.jpg" border="0" align="absmiddle">
					<a style="color:black;text-shadow: 0 1px 0 #f3f3f3;">어떤 레시피인가요?</a>
				</div>
				<div style="border-bottom:1px solid;border-right:3px solid;border-left:3px solid;border-color:#D8D8D8;padding-top:0.1px;">
					<form id="add_recipe_form" action="addRecipeForm_1.php" method="post">
						<li data-role="fieldcontain" style="width:80%;padding-left: 10%;border-top:none;">
							<input type="text" id="recipe_name" name="recipe_name" style="color:#D8D8D8;text-shadow: 0 1px 0 #f3f3f3;" placeholder="레시피의 이름을 적어주세요." data-clear-btn="true"/>
						</li>
						<div id="food_name" style="padding-left:15px;padding-top:20px;padding-bottom:20px;border-bottom:1px solid;border-top:1px solid;border-color:#D8D8D8;">
							<img id="bob" src="/img/bob/rice_icon.jpg" border="0" align="absmiddle">
							<a style="color:black;text-shadow: 0 1px 0 #f3f3f3;">레시피의 종류가 무엇인가요?</a>
						</div>
						<li data-role="fieldcontain" style="width:80%;padding-left: 10%;">
						 <fieldset data-role="controlgroup">
							<label for="rice" style="background-color:white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">밥</label>
					        <input type="radio" name="re_type" id="rice" value="밥">

					        <label for="soup" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">국/찌개</label>
					        <input type="radio" name="re_type" id="soup" value="국/찌개">

					        <label for="sub_menu" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">밑반찬</label>
					        <input type="radio" name="re_type" id="sub_menu" value="및반찬">
							
							<label for="main_menu" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">메인반찬</label>
					        <input type="radio" name="re_type" id="main_menu" value="메인반찬">

					        <label for="simple_food" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">일품요리</label>
					        <input type="radio" name="re_type" id="simple_food" value="일품요리">

					        <label for="dessert" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">간식</label>
					        <input type="radio" name="re_type" id="dessert" value="간식">

					        <label for="etc" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;display: block;">기타</label>
					        <input type="radio" name="re_type" id="etc" value="기타">
					        </fieldset>
						</li>
						<li data-role="fieldcontain" style="padding-left: 43%;">
							<button data-theme="b" id="submit" type="submit" style="background-color:white;white;color:black;text-shadow: 0 1px 0 #f3f3f3;">다음</button>
						</li>
					</form>
				</div>
				

		</div>
	</div>

</body>   
</html>