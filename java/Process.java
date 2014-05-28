import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.net.Socket;

class Process implements Runnable
{
        Random r = null;
        Socket sock = null;
        String path = "";
        String id = "";
        boolean use_sqr;
        boolean use_sqr_root;
        boolean use_sin;
        boolean use_cos;
        double constant_lower_bound;
        double constant_upper_bound;

        public Process (String path, String id, boolean use_sqr, boolean use_sqr_root, boolean use_sin, boolean use_cos, double constant_lower_bound, double constant_upper_bound)
        {
              try
              {
                r = new Random();
                sock = new Socket ("127.0.0.1", 8100);
                this.path = path;
                this.id = id;
                this.use_sqr = use_sqr;
                this.use_sqr_root = use_sqr_root;
                this.use_sin = use_sin;
                this.use_cos = use_cos;
                this.constant_lower_bound = constant_lower_bound;
                this.constant_upper_bound = constant_upper_bound;
              }
              catch (Exception e){e.printStackTrace();}
        }

	public void run ()
	{
		ArrayList <String> input_ids = new ArrayList <String> ();
		ArrayList <HashMap <String, Double>> inputs_training = new ArrayList<HashMap <String, Double>> ();
		ArrayList <Double> outputs_training = new ArrayList <Double> ();
		ArrayList <HashMap <String, Double>> inputs_test = new ArrayList<HashMap <String, Double>> ();
		ArrayList <Double> outputs_test = new ArrayList <Double> ();
		
		String output_name = "";
		
		try
		{
			BufferedReader br = new BufferedReader (new FileReader (path));
			String[] tokens;
			String nxtline = "";
			
			nxtline = br.readLine();
			
			tokens = nxtline.split(",");
			
			for (int i = 1; i < tokens.length; i++)
				input_ids.add(tokens[i]);
			
			output_name = tokens[0];
			
			while((nxtline=br.readLine())!=null)
			{
				HashMap <String, Double> an_input_set = new HashMap <String, Double> ();
				tokens = nxtline.split(",");
				
				for (int i = 1; i < tokens.length; i++)
					an_input_set.put(input_ids.get(i-1), Double.parseDouble(tokens[i]));
				
				if (r.nextDouble() < .8)
				{
					outputs_training.add(Double.parseDouble(tokens[0]));
					inputs_training.add(an_input_set);
				}
				else
				{
					outputs_test.add(Double.parseDouble(tokens[0]));
					inputs_test.add(an_input_set);
				}
			}
			
			br.close();
		}
		catch (Exception e){e.printStackTrace();}
		
		ArrayList <Tree> old_population = new ArrayList <Tree> ();
		ArrayList <Tree> new_population = new ArrayList <Tree> ();
		
		for (int i = 0; i < 100000; i++)
		{
			Tree new_tree = new Tree ();
			new_tree.SetRandomTree(r, input_ids, use_sqr, use_sqr_root, use_sin, use_cos, constant_lower_bound, constant_upper_bound);
			
			new_tree.CalculateFitness(inputs_training, outputs_training);
			
			old_population.add(new_tree);
		}
		
		Collections.sort(old_population);
		
		for (int i = 0; i < 1000; i++)
			new_population.add(old_population.get(i));
		
		for (int i = 0; i < 5000; i++)
		{
			Tree[] best_new = new Tree[100];
			
			for (int k = 0; k < 2000; k++)
			{
				Tree new_tree = new Tree ();
				new_tree.SetRandomTree(r, input_ids, use_sqr, use_sqr_root, use_sin, use_cos, constant_lower_bound, constant_upper_bound);
				
				new_tree.CalculateFitness(inputs_training, outputs_training);
				
				new_population.add(new_tree);
				for (int a = 0; a < best_new.length; a++)
				{
					if (best_new[a] == null)
					{
						best_new[a] = new_tree;
						break;
					}
					else if (new_tree.fitness < best_new[a].fitness)
					{
						for (int b = best_new.length - 1; b > a; b--)
							best_new[b] = best_new[b-1];
						break;
					}
				}
			}
			
			for (int k = 0; k < best_new.length; k++)
				new_population.add(best_new[k]);
			
			for (int a = 0; a < 2000; a++)
			{
				int j = (int)((double)new_population.size()*r.nextDouble());
				int k = (int)((double)new_population.size()*r.nextDouble());
				Tree new_individual = new_population.get(j).CrossOver(new_population.get(k), r);
				new_individual.CalculateFitness(inputs_training, outputs_training);
				new_population.add(new_individual);
			}
			
			old_population = new_population;
			new_population = new ArrayList <Tree> ();
			
			double num_data = (double)(old_population.size());
			double[] associate_dominate = new double[(int)num_data];
			
			for (int a = 0; a < old_population.size(); a++)
			{
				double num_dominate = 0.0;
				Tree one = old_population.get(a);
				
				for (int b = 0; b < old_population.size(); b++)
				{
					if (a != b)
					{
						Tree two = old_population.get(b);
						
						if (one.fitness >= two.fitness && one.age >= two.age && one.root.num_children >= two.root.num_children)
							num_dominate += 1.0;
						if (num_dominate >= 4)
							break;
					}
				}
				
				associate_dominate[a] = num_dominate;
			}
			
			for (int a = 0; a < associate_dominate.length; a++)
			{
				if (associate_dominate[a] < 4.0)
					new_population.add(old_population.get(a));
			}
                        if (i % 10 == 0 && new_population.size() > 5)
                        {
                                try
                                {
                                   sock = new Socket ("127.0.0.1", 8100);
                                   PrintWriter out = new PrintWriter (sock.getOutputStream(), true);
                                   String msg = "{\"id\": \"" + id + "\", \"results\": [";
                                   for (int b = 0; b < 5; b++)
                                      msg += "\"" + new_population.get(b).toString() + "\",";
                                  msg = msg.substring(0, msg.length()-1);
                                  msg += "]}";

                                  out.println(msg);
                                  sock.close();
                                }
                                catch (Exception e){e.printStackTrace();}
                        }
			
		}
		
	}
	
}






