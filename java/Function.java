import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Function 
{
	final static int ADDITION = 1;
	final static int SUBTRACTION = 2;
	final static int MULTIPLICATION = 3;
	final static int DIVISION = 4;
	final static int SQUARE = 5;
	final static int SQUAREROOT = 6;
	final static int COSINE = 7;
	final static int SINE = 8;

	final static int CONSTANT = -1;
	final static int DATA = -2;
	
	int identity = 0;
	
	double constant_value = 0.0;
	
	String input_id = "";
	
	double num_function = 8;
	double num_numerical = 2;
	double constant_range = 1.0;
	
	public Function DeepCopy ()
	{
		Function f = new Function ();
		
		f.input_id = input_id;
		f.constant_value = constant_value;
		f.identity = identity;
		
		return f;
	}
	
	public void SetRandomFunction (Random r)
	{
		identity = (int)(r.nextDouble() * num_function + 1.0);
	}
	
	public void SetRandomNumerical (Random r, ArrayList <String> input_ids)
	{
		identity = (int)((r.nextDouble() * num_numerical + 1.0)*-1);
		
		if (identity == CONSTANT)
		{
			constant_value = (r.nextDouble() * 2 * constant_range) - constant_range;
		}
		else if (identity == DATA)
		{
			int index = (int)(((double)input_ids.size()) * r.nextDouble());
			
			input_id = input_ids.get(index);
		}
	}
	
	public Double EvaluateFunction (double one, double two, HashMap <String, Double> data)
	{
		Double return_val = null;
		
		try
		{
			switch (identity)
			{
				case CONSTANT: return new Double (constant_value);
				case ADDITION: return new Double (one + two);
				case SUBTRACTION: return new Double (one - two);
				case MULTIPLICATION: return new Double (one * two);
				case DIVISION: return new Double (one / two);
				case SQUARE: return new Double (Math.pow(one, 2.0));
				case SQUAREROOT: return new Double (Math.pow(one, 0.5));
				case DATA: return new Double (data.get(input_id));
				case COSINE: return new Double (Math.cos(one));
				case SINE: return new Double (Math.sin(one));
			}
		}
		catch (Exception e){return null;}
		
		return return_val;
	}
	
	public int MaxNumDirectChildren ()
	{
		
		switch (identity)
		{
			case ADDITION: return 2;
			case SUBTRACTION: return 2;
			case MULTIPLICATION: return 2;
			case DIVISION: return 2;
			case SQUARE: return 1;
			case SQUAREROOT: return 1;
			case COSINE: return 1;
			case SINE: return 1;
		}
		
		return -1;
	}
	
	public boolean IsNumerical ()
	{
		
		switch (identity)
		{
			case CONSTANT: return true;
			case DATA: return true;
		}
		
		return false;
	}
}

