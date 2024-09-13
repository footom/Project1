<?php
	header("content-type:text/json;charset=utf-8");
	$con = mysqli_connect('localhost', 'root', '12345678') or die('無法連接資料庫');
	$db_selected = mysqli_select_db($con, 'gas_data') or die('無法使用資料庫') ; 
	
	$sql = "select * from data";
	$result = mysqli_query($con, $sql);
	$data="";
	$array= array();
	class dht11{
		public $id;
		public $humidity;
	    public $temperature;
		public $lpg;
		public $co;
		public $smoke;
		public $fire;
		public $updatetime;
	}
	
	while ($row = mysqli_fetch_row($result)){ 
		list($id ,$humidity, $temperature, $lpg, $co, $smoke, $fire, $updatetime) = $row;
		$d = new dht11();
		$d->id = $id;
		$d->humidity = $humidity;
		$d->temperature = $temperature;
		$d->lpg = $lpg;
		$d->co = $co;
		$d->smoke = $smoke;
		$d->fire = $fire;
		$updatetime = substr($updatetime, 5, 11); //substr(字串, 開始位置, 長度)
		$d->updatetime = $updatetime;
		$array[] = $d;
	}
	$data = json_encode($array);
	echo $data;
?>