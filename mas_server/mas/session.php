<?
session_start();

$m_id = $_POST['m_id'];
$m_pass = $_POST['m_pass'];

$_SESSION['ses_userid'] = $m_id;
$_SESSION['ses_pass'] = $m_pass;

?>