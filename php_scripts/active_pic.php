<?php
 
/*
 * Following code will get the number of pictures being used for evalutaion
*/
 
// array for JSON response
$response = array();
 
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql getting the number of active pictures
    $result = mysql_query("SELECT * FROM picture WHERE is_used = 1 ORDER BY name ASC");
	 
	
	
	if($result){
		$response["success"] = 1;
		$response["message"] = "Active pictures succesfully fetched.";
		
		
		while($row = mysql_fetch_assoc($result)) $response[] = $row;
		
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "An error occurred.";
		
		echo json_encode($response);
	}
   
?>