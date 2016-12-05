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
                    <li style="background-color: #FF9867;"><a href="http://localhost/bob/getList.php" action="getList.php" class="ui-btn-active">최신순</a></li>
                    <li><a href="#">인기순</a></li>
                    <li><a href="#">가격순</a></li>
                    <li><a href="#">테마</a></li>
                    <li><a href="#">게시판</a></li>
                </ul>
            </div>
        </div>
        
        <div data-role="navbar">
            <ul>
                <li style="background-color: #FF9867;"><a href="#" action="getList.php" class="ui-btn-active">밥상검색</a></li>
                <li><a href="#">밥상 차리기</a></li>
            </ul>
        </div>
        <ul data-role="listview" data-inset="true">
        <script>
            var arra = [];
<?php
    $conn = mysql_connect("localhost", "root", "682435");
    mysql_query('SET NAMES utf8');
    if (!$conn) {
        echo "Unable to connect to DB: " . mysql_error();
        exit;
    }
 
    if (!mysql_select_db("obobsang")) {
        echo "Unable to select mydbname: " . mysql_error();
        exit;
    }
     
    $sql = "SELECT * 
            FROM  Member";
     
    $result = mysql_query($sql);
     
    if (!$result) {
        echo "Could not successfully run query ($sql) from DB: " . mysql_error();
        exit;
    }
     
    if (mysql_num_rows($result) == 0) {
        echo "No rows found, nothing to print so am exiting";
        exit;
    }
     
    // While a row of data exists, put that row in $row as an associative array
    // Note: If you're expecting just one row, no need to use a loop
    // Note: If you put extract($row); inside the following loop, you'll
    //       then create $userid, $fullname, and $userstatus

    while ($row = mysql_fetch_assoc($result)) {
        echo "arra.push('/img/bob/{$row['m_id']}_main.jpg');";
    //    $arr[num] = '{$row['type_name']}';
     //   num++;
    }

    mysql_free_result($result);
    /*
    텍스트만 쭉쭉 뽑아내기
    while ($row = mysql_fetch_assoc($result)) {
        echo "{$row['type_num']}{$row['type_name']}";
    }
    */
?>
            </script>
<?php
    for($count = 0; $count<10; $count++) {
        echo '<li><img id ="gg" name="gs" src="" /><p>치킨은</p><p>언제나</p></li>';
    }
?>

            <script>       
                for(var i = 0; i < 10; i++) {
                    img = document.getElementsByTagName("img")[i];
                    img.src = arra.pop();
                }
                    
            </script>
        </ul>
    </div>
</div>
</body>
</body>
</html>