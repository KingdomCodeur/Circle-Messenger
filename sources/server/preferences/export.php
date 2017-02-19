<?php

	$dirData = "/var/www/html/data/";//getenv("OPENSHIFT_DATA_DIR");

	if(isset($_POST["JSON"]) && isset($_POST["id"])){
		$unique = $_POST["id"];
		$file = $dirData."preferences/preference_".$unique.".dat";
		file_put_contents($file, $_POST["JSON"]);
	}
	else{
		echo "Veuillez rentrer un JSON valide et un id...";
	}

?>