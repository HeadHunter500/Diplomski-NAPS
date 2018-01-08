<?php
 
/*
 * Following code will create a new user row
*/
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_REQUEST['name']) && isset($_REQUEST['age']) && isset($_REQUEST['sex'])) {
 
    $name = $_REQUEST['name'];
    $age = $_REQUEST['age'];
    $sex = $_REQUEST['sex'];
	
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
	
	$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die(mysqli_error());
 
    // mysql inserting a new row
    $result = mysqli_query($con,"INSERT INTO person(name, age, gender, date) VALUES('$name', '$age', '$sex', NOW())");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "User successfully created.";
		
		    // mysql getting the number of active pictures
			$result_sum = mysqli_query($con,"SELECT SUM(is_used) AS pic FROM picture");
			$row = mysqli_fetch_assoc($result_sum); 
			$sum = $row['pic'];
			
			$response["num"] = $sum;
				
			   
		
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>