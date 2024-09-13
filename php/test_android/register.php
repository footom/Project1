<?php
if(isset($_POST['username'])  && isset($_POST['password']) && isset($_POST['account'])) {
	require_once 'connect.php';
	require_once 'validate.php';
	$username = validate($_POST['username']);
	$account = validate($_POST['account']);
	$password = validate($_POST['password']);
	$sql = "insert into user values('', '$username', '$password', '$account')";
	if(!$conn->query($sql)) {
		echo "failure";

	} else {
		echo "success";
	
	}
}

?>