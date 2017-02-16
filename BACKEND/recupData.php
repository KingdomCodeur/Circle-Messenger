<form action='' method='POST'>
	<input hidden name='hid' value='OK'/>
	<input type='submit' value='GETALLCSV'></input>
</form>

<?php
	set_time_limit(0);
	$dirData = getenv("OPENSHIFT_DATA_DIR");
	if(isset($_POST['hid']) && $_POST['hid']=="OK"){
		$files = scandir($dirData."/recup/");
		$resultCsv = $dirData."/recup/dataset_result.csv";
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
	}

	if(isset($_POST["id"]) && isset($_POST["data"])){
		$unique = $_POST["id"];
		$file = $dirData."/recup/dataset_".$unique.".csv";
		file_put_contents($file, $_POST["data"]);
	}

	echo "<pre>";
		print_r(scandir($dirData."/recup/"));
	echo "</pre>";


	$dirData = getenv("OPENSHIFT_DATA_DIR");
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

	$cmd = "cd ".$dirData."data/;../R-3.0.2/bin/Rscript train_circle_rf.r > /dev/null &";

	exec($cmd);
?>