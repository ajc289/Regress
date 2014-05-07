import java.util.HashMap;

public class Node 
{
	Node left;
	Node right;
	
	Function f;
	
	public int num_children = 0;
	
	public Node (Function f)
	{
		this.f = f;
	}
	
	public void UpdateNumberChildren ()
	{
		num_children = 0;
		
		if (left != null)
		{
			left.UpdateNumberChildren();
			
			num_children = left.num_children + 1;
		}
		if (right != null)
		{
			right.UpdateNumberChildren();
			
			num_children = right.num_children + 1;
		}
	}
	
	public Node DeepCopy ()
	{
		Node new_node = new Node (f.DeepCopy());
		
		new_node.num_children = num_children;
		
		if (left != null)
			new_node.left = left.DeepCopy();
		if (right != null)
			new_node.right = right.DeepCopy();
		
		return new_node;
	}
	
	public Double Evaluate (HashMap <String, Double> data)
	{
		Double left_eval = new Double (0.0);
		Double right_eval = new Double (0.0);
		
		if (left != null)
			left_eval = left.Evaluate(data);
		if (right != null)
			right_eval = right.Evaluate(data);
		
		if (left_eval == null || right_eval == null)
			return null;
		
		return f.EvaluateFunction(left_eval.doubleValue(), right_eval.doubleValue(), data);
	}
}

