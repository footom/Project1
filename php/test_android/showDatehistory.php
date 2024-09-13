<?php
if(isset($_POST['updatetime'])){
	header("content-type:text/json;charset=utf-8");
	$con = mysqli_connect('localhost', 'root', '12345678') or die('無法連接資料庫');
	$db_selected = mysqli_select_db($con, 'gas_data') or die('無法使用資料庫') ; 
	$time = $_POST['updatetime'];
	$time1 = $_POST['updatetime1'];
	$sql = "select id, temperature, humidity, lpg, co, fire, updatetime from data where Date_Format(updatetime, '%Y-%m-%d') >= '$time' and Date_Format(updatetime, '%Y-%m-%d')<='$time1'";
	$result = mysqli_query($con, $sql);
	$data="";
	$array= array();
	class history{
		public $id;
		public $humidity;
	    public $temperature;
		public $lpg;
		public $co;
		public $fire;
		public $updatetime;
	}
	while ($row = mysqli_fetch_row($result)){ 
		list($id, $humidity, $temperature, $lpg, $co, $fire, $updatetime) = $row;
		$d = new history();
		$d->id = $id;
		$d->humidity = $humidity;
		$d->temperature = $temperature;
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
}
?>