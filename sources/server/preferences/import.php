<?php
	$dirData = "/var/www/html/data/";//getenv("OPENSHIFT_DATA_DIR");

	if(isset($_GET["id"])){
		$unique = $_GET["id"];
		$content = file_get_contents($dirData."preferences/preference_".$unique.".dat");
		if($content==""){
			echo "[]";
		}
		else{
			echo $content;
		}
	}
	else{
		echo "please enter an id in GET";
	}

?>