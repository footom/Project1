<?php
class dht11{
	public $link='';
	function __construct($temperature, $humidity, $lpg, $co, $smoke, $fire){
		$this->connect();
		$this->storeInDB($temperature, $humidity, $lpg, $co, $smoke, $fire);
	}
	 
	function connect(){
		$this->link = mysqli_connect('localhost','root','12345678') or die('無法連接資料庫');
		mysqli_select_db($this->link,'gas_data') or die('無此資料庫');
	}
	 
	function storeInDB($temperature, $humidity, $lpg, $co, $smoke, $fire){
		$query = "insert into data set humidity='".$humidity."', temperature='".$temperature."', lpg='".$lpg."', co='".$co."', smoke='".$smoke."', fire='".$fire."'";
		$result = mysqli_query($this->link,$query) or die('Error query:  '.$query);
	}
}
if($_GET['temperature'] != '' and  $_GET['humidity'] != '' and  $_GET['lpg'] != '' and  $_GET['co'] != '' and  $_GET['smoke'] != '' and $_GET['fire'] != ''){
	$dht11=new dht11($_GET['temperature'],$_GET['humidity'], $_GET['lpg'], $_GET['co'], $_GET['smoke'], $_GET['fire']);
}
?>
