<?php
	header("content-type:text/json;charset=utf-8");
	$con = mysqli_connect('localhost', 'root', '12345678') or die('無法連接資料庫');
	$db_selected = mysqli_select_db($con, 'gas_data') or die('無法使用資料庫') ; 
	
	$sql = "SELECT * FROM user";
	$result = mysqli_query($con, $sql);
	$data="";
	$array= array();
	class dht11{
		public $id;
		public $email;
		public $password;
		public $username;
	}
	
	while ($row = mysqli_fetch_row($result)){ 
		list($id, $username, $password, $email) = $row;
		$d = new dht11();
		$d->id = $id;
		$d->email = $email;
		$d->password = $password;
		$d->username = $username;
		$array[] = $d;
	}
	$data = json_encode($array);
	echo $data;
?>