import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class Canonical_Form {
	
	static double pc=0.80;
	static double pm=0.75;
	static int pop_size =20;
	static Chromosome[] chromosomes;
	static ArrayList<Chromosome> generated_childs =new ArrayList<Chromosome>();
	static Item [] items ;
	static int max_generation =10;
	static ArrayList<Chromosome>fittest=new ArrayList<Chromosome>();
	
	public static boolean Nodoplication(Chromosome[]ch,int []genes,int num_of_items  , int size)
	{
		boolean find =false;
		int counter;
		for(int i=0;(i<size) &&(find==false);i++)
		{
			counter=0;
			for(int j=0;j<num_of_items;j++)
			{
				if(ch[i].genes[j]==genes[j])
				{
					counter++;
				}
			}
			if(counter==(num_of_items-1))
			{
				find=true;
			}
		}
		return find;
	}
	
	public static Chromosome[] Initialize_population(int num_of_items ,int population_size , Chromosome[]ch )
	{
		ch=new Chromosome[population_size] ;
		double random ;
	
		
		int size=0;
		int[]g=null;
		while(size<population_size)
		{
			g=new int[num_of_items];
			for(int i=0;i<num_of_items;i++)
			{
				random=Math.random();
				if(random<0.5)
				{
					g[i]=0;
				}
				else
				{
					g[i]=1;
				}
			}
			if(Nodoplication(ch, g, num_of_items, size)==false)
			{
				
				ch[size]=new Chromosome();
				ch[size].genes=new int[num_of_items] ;
				for(int i=0;i<num_of_items;i++)
				{
					ch[size].genes[i]=g[i];
				}
				size++;
			}
			
		}
		
		return ch;	
	}
	
	public static void Print( Chromosome[] ch , int num_of_items ,int population_size  )
	{
		for(int i=0;i<population_size;i++)
		{
			System.out.println("chromosome " + i+" : ");
			for(int j=0;j<num_of_items;j++)
			{
				System.out.print(ch[i].genes[j]);
				//System.out.println(ch[i].fittness);
			}
			System.out.println();
		}
	}
	
	public static void Mutation(ArrayList<Chromosome>childs,int num_of_items )
	{
		double random ;
		
		for(int i=0;i<childs.size();i++)
		{
			for(int j=0;j<num_of_items;j++)
			{
				random =Math.random();
				if(random>=pm)
				{
					if(childs.get(i).genes[j]==0)
					{
						childs.get(i).genes[j]=1;
					}
					else
					{
						childs.get(i).genes[j]=0;
					}
				}
			}
		}
		
	}
	
	public static void cross_over(Chromosome[] ch , ArrayList<Chromosome> childs,int population_size , int number_of_items)
	{
		int min=0;
		int max=number_of_items-1;
		int random_crossover_point=(int)( min+ (Math.random()*max) );//////////////////////////////////////// get random between range [0,numbwerof_intems-1]??
		//int n=population_size/2;
		
		for(int i=0 ;i<population_size;i+=2)
		{
			Chromosome ch1=new Chromosome();
			Chromosome ch2=new Chromosome();
			ch1.genes=new int[number_of_items];
			ch2.genes=new int[number_of_items];
			for(int j=0;j<random_crossover_point;j++)
			{
				ch1.genes[j]=ch[i].genes[j];
				ch2.genes[j]=ch[i+1].genes[j];
			}
			for(int j=random_crossover_point;j<number_of_items;j++)
			{
				ch1.genes[j]=ch[i+1].genes[j];
				ch2.genes[j]=ch[i].genes[j];
			}
			childs.add(ch1);
			childs.add(ch2);
		}
	}
	
   public static void evaluate_fitnesses( ArrayList<Chromosome>childs,int num_of_items)
	{
		int fit_i;
		for(int i=0;i<childs.size();i++)
		{
			fit_i=0;
			for(int j=0;j<num_of_items;j++)
			{
				fit_i+=childs.get(i).genes[j]*items[j].benifit;
			}
			childs.get(i).fittness=fit_i;
		}
		
	}
   public static Chromosome Selection(ArrayList<Chromosome>childs,int num_of_items,int knap_size)
   {
	   evaluate_fitnesses(childs, num_of_items);
	   Chromosome selected=new Chromosome();
	   int fittness=0;
	   for(int i=0;i<childs.size();i++)
	   {
		   if(childs.get(i).fittness<=knap_size)
		   {
			   if(childs.get(i).fittness > fittness)
			   {
				   fittness=childs.get(i).fittness;
				   selected=childs.get(i);
			   }
		   }
	   }
	return selected;   
   }
  
   public static Chromosome run (Chromosome[]ch ,int p_size , int max_gen,ArrayList<Chromosome> childs ,int number_of_items ,int knap_size, ArrayList<Chromosome>Fittest)
  {
	  Fittest=new  ArrayList<Chromosome>();
	 for(int i=0;i<max_gen;i++)
	 {
		 childs=new ArrayList<Chromosome>();
		 cross_over( ch , childs, p_size ,  number_of_items) ;// output arrayList<childs>
		 Mutation(childs,number_of_items); ////output modified childs
		 Fittest.add(Selection(childs, number_of_items, knap_size)) ;
		 for(int j=0;j<p_size;j++)
		 {
			 for(int k=0;k<number_of_items;k++)
			 {
				 ch[j].genes[k]=childs.get(j).genes[k];
			 }
		 }
	 }
	 int index_of_max=0;
	 int max=Fittest.get(0).fittness;
	 for(int i=1;i<Fittest.size();i++)
	 {
		 if(Fittest.get(i).fittness>max)
		 {
			 index_of_max=i;
			 max=Fittest.get(i).fittness;
		 }
	 }
	 return Fittest.get(index_of_max);
  }
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		
		
		Scanner input=new Scanner(System.in);
		int C=0 ;   // number of test cases  
		int N=0 ;  // number of items 
		int S=0 ; // Knapsack Size 
/////////////////////////////////////////////////////////////////////////////
		Scanner scanner = new Scanner(new File("input_exp.txt")); 
		C=scanner.nextInt();
		System.out.println(C);
		//while(scanner.hasNextInt())
		//{
			N=scanner.nextInt();
			S=scanner.nextInt();
			items=new Item[N];
			for(int i=0;i<N;i++)
			{	
	        	items[i]=new Item();
	        	items[i].weight=scanner.nextInt();
	       		items[i].benifit=scanner.nextInt();
			}
		//}
		System.out.println(N);
		System.out.println(S);
		for(int i=0;i<N;i++)
		{
			System.out.println(items[i].weight);
			System.out.println(items[i].benifit);
		}

        chromosomes= Initialize_population(N,pop_size,chromosomes);
        Chromosome result=new Chromosome();
        result=run (chromosomes ,pop_size , max_generation ,generated_childs ,N ,S, fittest);
        
        int M=0;
        for(int i=0;i<N;i++)
        {
        	if(result.genes[i]==1)
        	{
        		M++;
        	}
        }
        System.out.println("CaseID : 1");
        System.out.println("Number of items taken : " + M);
        System.out.println("Itemes Selected ");
        for(int i=0;i<N;i++)
        {
        	if(result.genes[i]==1)
        	{
        		System.out.println("Weight and Benifit = "+items[i].weight+" "+items[i].benifit);
        	}
        }
        
        System.out.println("total_fi= "+result.fittness);
        
        
////////////////////////////////////////////////////////////////////////////////
        
       
       
      //  Print(chromosomes,N,pop_size);
        
        
       
        
        	

     }
}