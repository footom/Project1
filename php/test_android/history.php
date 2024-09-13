<?php
	header("content-type:text/json;charset=utf-8");
	$con = mysqli_connect('localhost', 'root', '12345678') or die('無法連接資料庫');
	$db_selected = mysqli_select_db($con, 'gas_data') or die('無法使用資料庫') ; 
	
	$sql = "select lpg, co, fire, updatetime from data where lpg>180 or co>150";
	$result = mysqli_query($con, $sql);
	$data="";
	$array= array();
	class history{
		public $lpg;
		public $co;
		public $fire;
		public $updatetime;
	}
	while ($row = mysqli_fetch_row($result)){ 
		list($lpg, $co, $fire, $updatetime) = $row;
		$d = new history();
		$updatetime = substr($updatetime, 5, 11); //substr(字串, 開始位置, 長度)
		$d->updatetime = $updatetime;
		$d->lpg = $lpg;
		$d->co = $co;

		if($fire==1) $fire="yes";
		else $fire="no";
		$d->fire = $fire;
		$array[] = $d;
	}
	$data = json_encode($array);
	echo $data;
?>