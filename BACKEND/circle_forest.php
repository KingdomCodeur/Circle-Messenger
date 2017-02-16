<?php
	ini_set('error_reporting', E_ALL);
	set_time_limit(0);
	
	require_once('random_forest.php');
	
	$dirData = getenv("OPENSHIFT_DATA_DIR");

	if(isset($_POST["JSON"])){
		//echo "JSOOOOOOONNNNNNNNN : ".$_POST["JSON"]."\n";

		$classifier = new forest($dirData.'data/circleMessenger_forest.csv');
		//0,2,2913,1456,1,2
		//{"+33684178024":"[[1,0,0,3.0,1,0]]"}
		// Format du JSON : {"+33666633339":[[2,0,0,3,1,2],[3,0,0,3,1,2],[5,0,0,3,1,0],[1,0,0,3,0,1],[2,0,0,3,0,0]],"+33666666666":[[2,0,0,3,1,2],[3,0,0,3,1,2],[5,0,0,3,1,0],[1,0,0,3,0,1],[2,0,0,3,0,0]]}
		$nums = json_decode($_POST["JSON"]);

		$retourJSON = "[";
		foreach ($nums as $num => $vectors) {
			$nbAmi = 0;
			$nbFamille = 0;
			$nbCollegue = 0;
			foreach ($vectors as $key => $value) {				
				$test_data = $value;//array(0,2,2913,1456,1,2);
				$result = $classifier -> evaluate($test_data);

				//echo "RESULT : ".$result."<br>";
				switch ($result) {
					case 'AMI':
						$nbAmi++;
						break;
					case 'FAMILLE':
						$nbFamille++;
						break;
					case 'COLLEGUE':
						$nbCollegue++;
						break;
					default:
						//nothing
						break;
				}
			}

			$max = max($nbCollegue,$nbAmi,$nbFamille);
			//echo "<br>famille : $nbFamille<br>ami : $nbAmi<br>colegue : $nbCollegue<br>MAX:$max";
			$nbVecteurs = count($vectors);
			//echo "Nb de vecteurs : ".$nbVecteurs;
			//echo "Pourcentage : ".number_format(($max/$nbVecteurs),2)."%";
			$confiancePercent = number_format(($max/$nbVecteurs),4)*100;
			if($nbCollegue == $max){
				$retourJSON .= "{\"num\" : \"$num\",\"type\":\"collegue\", \"confiance\":".$confiancePercent."},";
			}
			elseif($nbAmi == $max){
				$retourJSON .= "{\"num\" :\"$num\",\"type\":\"ami\", \"confiance\":".$confiancePercent."},";
			}
			elseif($nbFamille == $max){
				$retourJSON .= "{\"num\" :\"$num\",\"type\":\"famille\", \"confiance\":".$confiancePercent."},";
			}
		}
		$retourJSON = substr($retourJSON, 0,-1);
		$retourJSON .= "]";

		echo $retourJSON;		
	}
	else{
		echo "{\"error\":\"please enter a JSON in the POST http query\"}";
	}
?>