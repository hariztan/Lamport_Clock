import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


public class lamport2 {


	static int ev[]=new int[25];
	static int lc[][]= new int[5][25];
	static int p,slocation;
	static String evin[][]=new String[5][25];
	public static void main(String args[]) throws FileNotFoundException
	{
		int i,j,k,rseq,lctemp;		
		Scanner sc=new Scanner(System.in);
		PrintStream console = System.out;
		File file = new File("output.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.out.println("Enter the number of process:");
		p=sc.nextInt();
		System.out.println("Enter the no of events per process:");
		for(i=1;i<=p;i++)
		{
		  ev[i]=sc.nextInt();
		}
		System.out.println("Enter internal events as chars other than 's' and 'r' unless it's a send or receive:");
		for(i=1;i<=p;i++)
		{
			System.out.println("For process:"+i);
			for(j=1;j<=ev[i];j++)
			{
				System.out.println("For event:"+j);
				evin[i][j]=sc.next();
				lc[i][j]=-2; //Initialized all values of logical clocks as -2 for validation purpose
			}
		}
		
		// print input enter by user
		System.out.println("Below is the entered Input:");
		for(i=1;i<=p;i++)
		{
			System.out.print("P"+i+" : ");
			for(j=1;j<=ev[i];j++)
		    {
			System.out.print(evin[i][j]+" ");
		    }
		  System.out.println();
		}
		


		// Initialization of Main Logic of Logical Clock 
		for(i=1;i<=p;i++)
		{
			for(j=1;j<=ev[i];j++)
			{
				if((j==1)&&(evin[i][j].charAt(0)!='r'))
					lc[i][j]=1;  
				else if(evin[i][j].charAt(0)!='r')
				{
					lc[i][j]=lc[i][j-1]+1;
				}
				else
				{
					rseq=Character.getNumericValue(evin[i][j].charAt(1));
					lctemp=findlcs(rseq);
					if (lctemp==-5)
					System.out.println("There is some problem in lctemp value shall not be -5");
					if(lctemp<lc[i][j-1])
						lc[i][j]=lc[i][j-1]+1;
					else
						lc[i][j]=lctemp+1;
				}
			}
		}
		
		//It will print logical clock value 
		System.setOut(ps);
		for(i=1;i<=p;i++)
		{
			System.out.print("P"+i+" : ");
			for(j=1;j<=ev[i];j++)
		    {
				System.out.print(lc[i][j]+" ");
		    }
		  System.out.println();
		}
		System.setOut(console);
		System.out.println();
		//Print final output on console
		System.out.println("Logical clock value for the above input is as below");
		for(i=1;i<=p;i++)
		{
			System.out.print("P"+i+" : ");
			for(j=1;j<=ev[i];j++)
		    {
				System.out.print(lc[i][j]+" ");
		    }
		  System.out.println();
		}
}
	
//Recursive function which find logical clock of the Send event 
static int findlcs(int rseq)		
	{
		int i,j,slc=-5;		//slog : Logical clock value of corresponding s
		for(i=1;i<=p;i++)	//find lc(s) matches -2 then pass process id to logclock() to calculate the value of of s
		{
			for(j=1;j<=ev[i];j++)
			{
				//if(lc[i][j]==-2)		//Calculate LC if it has a default value
				//	logclock(i);				
				if(evin[i][j].charAt(0)=='s'&& Character.getNumericValue(evin[i][j].charAt(1))==rseq)
				{
					if(lc[i][j]!=-2)
						return lc[i][j];		//return the corresponding sender logical clock
					else
					{
						slocation=j;
						slc=logclock(i);
					}
				}
			}
		}			
		return slc;
	}
	
	static int logclock(int pr)
	{
		int i,j,rseq,lctemp,slc=-1;
		for(j=1;j<=ev[pr];j++)
		{
			if((j==1)&&(evin[pr][j].charAt(0)!='r'))
			{
				//if(evin[i][j].charAt(0)=='s')
				lc[pr][j]=1;
			}
			else if(evin[pr][j].charAt(0)!='r')
			{
				lc[pr][j]=lc[pr][j-1]+1;
			}
			else
			{
				rseq=Character.getNumericValue(evin[pr][j].charAt(1));
				lctemp=findlcs(rseq);
				if(lctemp<lc[pr][j-1])
					lc[pr][j]=lc[pr][j-1]+1;
				else
					lc[pr][j]=lctemp+1;
			}
			if(j==slocation && lc[pr][j]!=-2)
			{
				return lc[pr][j];
			}
		}
		return slc;
	}
}
