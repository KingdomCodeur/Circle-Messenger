<?php
	echo set_time_limit(0);

	$dirData = "/var/www/html/data/";//getenv("OPENSHIFT_DATA_DIR");
	$files = scandir($dirData."/recup/");
	$resultCsv = $dirData."/data/dataset/learning.csv";
	if(file_exists($resultCsv)){
		unlink($resultCsv); // on suppr le fichier si il existe deja...
	}

	file_put_contents($resultCsv, "nbSms,nbCalls,cumulativeCallDuration,averageCallDuration,isWeekDay,timeInDay,contactType\n", FILE_APPEND);
	foreach ($files as $key => $value) {
		$exp = explode(".",$value);
		if($exp[sizeof($exp) - 1] == "csv" && $value!="dataset_result.csv"){
			file_put_contents($resultCsv, file_get_contents($dirData."/recup/".$value), FILE_APPEND);
		}
	}


	$dirData = "/var/www/html/data/";//getenv("OPENSHIFT_DATA_DIR");

	$cmd = "cd ".$dirData."data/;Rscript train_circle_rf.r";

	echo "EXEC DE : ".$cmd."<br>";
	echo "EXEC : ".exec($cmd);
	

	echo $tmp = file_get_contents($dirData."data/circleMessenger_forest.csv");

	echo "File : <br>";
	echo $tmp;
?>