<?php
$server="localhost";
$username="root";
$password="12345678";
$database="gas_data";

$conn=new mysqli($server, $username, $password, $database);
if($conn->connect_error) {
	die("connection failed:" . $conn->connect_error);
}
?>