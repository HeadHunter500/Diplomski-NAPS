<?php
 
/*
 * Following code will create a new rate row
*/
 
// array for JSON response
$response = array();
 
 
    $url = $_REQUEST['url'];
    $valence = $_REQUEST['valence'];
	$arousal = $_REQUEST['arousal'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
	
	$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die(mysqli_error());
	
	$result_id = mysqli_query($con,"SELECT id AS id_person FROM person ORDER BY date DESC LIMIT 1");
			$row_id = mysqli_fetch_assoc($result_id); 
			$id_person = $row_id['id_person'];
			
	$result_pic = mysqli_query($con,"SELECT id AS id_picture FROM picture WHERE url = '$url'");
			$row_pic = mysqli_fetch_assoc($result_pic);
			$id_picture = $row_pic['id_picture'];
			
			
 
    // mysql inserting a new row
    $result = mysqli_query($con,"INSERT INTO rating(id_person, id_picture, valence, arousal, date) VALUES('$id_person', '$id_picture ', '$valence', '$arousal', NOW())");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Picture successfully rated.";
	
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
?>