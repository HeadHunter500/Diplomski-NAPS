<?php
 
/*
 * Following code will create a new user row
*/
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_REQUEST['id_person']) && isset($_REQUEST['id_picture']) && isset($_REQUEST['valence'] && isset($_REQUEST['arousal'])) {
 
    $id_person = $_REQUEST['id_person'];
    $id_picture = $_REQUEST['id_picture'];
    $valence = $_REQUEST['valence'];
	$arousal = $_REQUEST['arousal'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO rating(id_person, id_picture, valence, arousal, date) VALUES('$id_person', '$id_picture ', '$valence', '$arousal', NOW())");
 
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
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>