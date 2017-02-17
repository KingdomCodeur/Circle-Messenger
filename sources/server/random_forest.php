<?php
	/* Class for individual tree branches */
	
	class branch
	{
		public $split_var;
		public $status;
		public $prediction;
		
		private $left;
		private $right;
		private $split_point;
		
		public function __construct($line) {
			$this -> left = $line[1] - 1; /* Because R output is 1-indexed, not 0-indexed */
			$this -> right = $line[2] - 1;
			$this -> split_var = $line[3] - 1;
			$this -> split_point = $line[4];
			$this -> status = $line[5];
			$this -> prediction = $line[6];
		}
		
		public function get_next($value) {
			if ($value <= $this -> split_point) return($this -> left);
			else return($this -> right);
		}
	}
	
	/* Class for the trees themselves */
	
	class tree
	{
		private $branches = array();
		
		public function add_branch($line) {
			$this -> branches[] = new branch($line);
		}
		
		public function evaluate($data)
		{
			$decision = null;
			$current_branch = 0;
			
			while ($decision === null)
			{
				if ($this -> branches[$current_branch] -> status == -1) $decision = $this -> branches[$current_branch] -> prediction;
				else $current_branch = $this -> branches[$current_branch] -> get_next($data[$this -> branches[$current_branch] -> split_var]);
			}
			
			return($decision);
		}
	}
	/* And for the forest */
	
	class forest
	{
		private $trees = array();
		
		public function __construct($file_loc)
		{
			$model_file = fopen($file_loc, 'r');
			fgetcsv($model_file); /* Discard the header */
			
			$current_tree_index = -1;
			
			while (($line = fgetcsv($model_file)) !== false)
			{
				if ($line[0] - 1 > $current_tree_index) /* Again, adjusting for R output being 1-indexed */
				{
					$this -> trees[] = new tree;
					$current_tree_index ++;
				}
				$this -> trees[$current_tree_index] -> add_branch($line);
			}
			
			fclose($model_file);
		}
		
		public function evaluate($data)
		{
			$votes = array();
			
			foreach($this -> trees as $current)
			{
				$current_vote = $current -> evaluate($data);
				
				if (! array_key_exists($current_vote, $votes)) $votes[$current_vote] = 0;
				$votes[$current_vote] ++;
			}
			
			return(array_keys($votes, max($votes))[0]);
		}
	}
?>