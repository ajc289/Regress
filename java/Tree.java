import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Tree implements Comparable
{
	public Node root = null;
	
	double new_tree_min_func_nodes = 2.0;
	double new_tree_max_func_nodes = 5.0;
	
	public double fitness = 0.0;
	
	public int age = 0;
	
	double sort_val = 0;
	
	boolean sort_by_sort_val = false;
	
	public Tree (Node root)
	{
		this.root = root;
	}
	
	public Tree () {}
	
	public void CalculateFitness (ArrayList <HashMap <String, Double>> inputs, ArrayList <Double> outputs)
	{
		fitness = 0.0;
		
		for (int i = 0; i < inputs.size(); i++)
		{
			Double result = root.Evaluate(inputs.get(i));
			
			//if (result == null || result.doubleValue() == Double.NaN || result.doubleValue() == Double.POSITIVE_INFINITY || result.doubleValue() == Double.NEGATIVE_INFINITY)
			if (result == null || Double.isInfinite(result.doubleValue()) || Double.isNaN(result.doubleValue()))
			{
				fitness = Double.MAX_VALUE;
				break;
			}
			
			double abs_diff = Math.abs(outputs.get(i).doubleValue() - result.doubleValue());
			
			if (abs_diff < 1.0)
				fitness += Math.pow(abs_diff, .5);
			else
				fitness += Math.pow(abs_diff, 2.0);
			
			//fitness += Math.abs(outputs.get(i).doubleValue() - result.doubleValue());
		}
		
		sort_val = age*1000000.0 + ((double) root.num_children);
	}
	
	public String CalculateFitnessOutput (ArrayList <HashMap <String, Double>> inputs, ArrayList <Double> outputs)
	{
		String return_val = "";
		
		for (int i = 0; i < inputs.size(); i++)
		{
			Double result = root.Evaluate(inputs.get(i));
			
			//if (result == null || result.doubleValue() == Double.NaN || result.doubleValue() == Double.POSITIVE_INFINITY || result.doubleValue() == Double.NEGATIVE_INFINITY)
			if (result == null || Double.isInfinite(result.doubleValue()) || Double.isNaN(result.doubleValue()))
			{
				fitness = Double.MAX_VALUE;
				return_val += Double.toString(outputs.get(i).doubleValue()) + "," + Double.toString(Double.MAX_VALUE) + ",";
				continue;
			}
			
			return_val += Double.toString(outputs.get(i).doubleValue()) + "," + Double.toString(result.doubleValue()) + ",";
		}
		
		return return_val;
	}
	

	public Node RandomNode (Node cur_node, Random r, double prob, boolean can_be_numerical)
	{
		if (r.nextDouble() < prob)
		{
			if (cur_node.f.IsNumerical() && !can_be_numerical)
				return null;
			else
				return cur_node;
		}
		else
		{
			Node chosen = null;
			
			if (cur_node.left != null)
				chosen = RandomNode (cur_node.left, r, prob, can_be_numerical);
			
			if (chosen != null)
				return chosen;
			
			else if (cur_node.right != null)
				chosen = RandomNode (cur_node.right, r, prob, can_be_numerical);
			
			return chosen;
		}
	}
	
	public Tree CrossOver (Tree other, Random r)
	{
		double one_prob = 1.0 / (((double)(root.num_children) + 1.0));
		double two_prob = 1.0 / (((double)(other.root.num_children) + 1.0));
		
		Tree this_clone = new Tree (root.DeepCopy());
		Tree other_clone = new Tree (other.root.DeepCopy());
		
		Node this_clone_swap = null;
		Node other_clone_swap = null;
		
		do
		{
			this_clone_swap = RandomNode (this_clone.root, r, one_prob, false);
		}
		while (this_clone_swap == null);
		
		do
		{
			other_clone_swap = RandomNode (other_clone.root, r, two_prob, true);
		}
		while (other_clone_swap == null);
		
		if (this_clone_swap.f.MaxNumDirectChildren() == 1)
		{
			this_clone_swap.left = other_clone_swap;
		}
		else
		{
			if (r.nextDouble() < .5)
				this_clone_swap.left = other_clone_swap;
			else
				this_clone_swap.right = other_clone_swap;
		}
		
		this_clone.root.UpdateNumberChildren();
		this_clone.age = age + 1;
		
		return this_clone;
	}
	
	public void AddFunctionToTree (Node cur, Node to_add)
	{
		if (cur.left != null && cur.right != null)
		{
			cur.num_children++;
			
			if (cur.left.num_children <= cur.right.num_children)
				AddFunctionToTree (cur.left, to_add);
			else
				AddFunctionToTree (cur.right, to_add);
		}
		else if (cur.left == null)
		{
			cur.left = to_add;
			cur.num_children++;
		}
		else
		{
			if (cur.f.MaxNumDirectChildren() == 1)
			{
				cur.num_children++;
				AddFunctionToTree (cur.left, to_add);
			}
			else
			{
				cur.right = to_add;
				cur.num_children++;
			}
		}
	}
	
	public boolean AddNumericalToTree (Node cur, Node to_add)
	{
		if (cur.f.IsNumerical())
			return false;
		else if (cur.left != null && cur.right != null)
		{
			boolean left_result = AddNumericalToTree (cur.left, to_add);
			
			if (!left_result)
			{
				boolean right_result = AddNumericalToTree (cur.right, to_add);
				
				if (right_result)
					cur.num_children++;
				
				return right_result;
			}
			else
			{
				cur.num_children++;
				return true;
			}
		}
		else if (cur.f.MaxNumDirectChildren() == 1 && cur.left != null)
		{
			boolean left_result = AddNumericalToTree (cur.left, to_add);
			
			if (left_result)
				cur.num_children++;
			
			return left_result;
		}
		else if (cur.left == null)
		{
			cur.left = to_add;
			
			cur.num_children++;
			
			return true;
		}
		else
		{
			cur.right = to_add;
			
			cur.num_children++;
			
			return true;
		}
	}
	
	public void SetRandomTree (Random r, ArrayList <String> input_ids)
	{
		int num_func = (int)((new_tree_max_func_nodes - new_tree_min_func_nodes)*r.nextDouble() + new_tree_min_func_nodes);
		
		Function[] functions = new Function[num_func];
		
		for (int i = 0; i < functions.length; i++)
		{
			functions[i] = new Function();
			functions[i].SetRandomFunction(r);
		}
		
		root = new Node (functions[0]);
		
		for (int i = 1; i < functions.length; i++)
			AddFunctionToTree (root, new Node (functions[i]));
		
		boolean successful_addition = false;
		
		do
		{
			Function new_numerical = new Function ();
			new_numerical.SetRandomNumerical(r, input_ids);
			
			successful_addition = AddNumericalToTree (root, new Node(new_numerical));
		}
		while (successful_addition);
	}

	public int compareTo(Object arg0) 
	{
		Tree other = (Tree) arg0;
		
		//if (sort_by_sort_val)
			//return Double.compare(sort_val, other.sort_val);
		//else
			return Double.compare(fitness, other.fitness);
		//return Double.compare(other.fitness, fitness);
			
	}
	
	public String GenerateString (Node cur_node)
	{
		switch (cur_node.f.identity)
		{
			case Function.CONSTANT: return Double.toString(cur_node.f.constant_value);
			case Function.ADDITION: return "(" + GenerateString(cur_node.left) + ")" + " + " + "(" + GenerateString(cur_node.right) + ")";
			case Function.SUBTRACTION: return "(" + GenerateString(cur_node.left) + ")" + " - " + "(" + GenerateString(cur_node.right) + ")";
			case Function.MULTIPLICATION: return "(" + GenerateString(cur_node.left) + ")" + " * " + "(" + GenerateString(cur_node.right) + ")";
			case Function.DIVISION: return "(" + GenerateString(cur_node.left) + ")" + " / " + "(" + GenerateString(cur_node.right) + ")";
			case Function.SQUARE: return "(" + GenerateString(cur_node.left) + ") ^2";
			case Function.SQUAREROOT: return "sqrt (" + GenerateString(cur_node.left) + ")";
			case Function.DATA: return cur_node.f.input_id;
			case Function.COSINE: return "cos (" + GenerateString(cur_node.left) + ")";
			case Function.SINE: return "sin (" + GenerateString(cur_node.left) + ")";
		}
		
		return "";
	}
	
	public String toString ()
	{
		return GenerateString (root);
	}
}

