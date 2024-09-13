<?php
if(isset($_POST['phone'])) {
	require_once 'connect.php';
	require_once 'validate.php';
	$phone = validate($_POST['phone']);
	$sql = "select * from user where phone='$phone'";
	$result=$conn->query($sql);
	if($result->num_rows > 0) {
		echo "success";
	} else {
		echo "failure";
	}
}

?>