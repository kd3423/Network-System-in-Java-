//@ Author : Gurpreet (2015140) & Karan(2015141)
import java.util.*;
import java.io.*;

class Person
{
	private String userName,displayName,password,status;
	private int noOfFriends,noOfRequests;
	private ArrayList <String> requests,friends;

	Person(String userName,String displayName,String password)
	{
		this.userName = userName;
		this.displayName = displayName;
		this.password = password;
		this.status = "! Status not entered yet !";
		this.requests = new ArrayList <String>();
		this.friends = new ArrayList <String>();
	}

	Person(String userName, String displayName, String password, int noOfFriends, ArrayList<String> friends, int noOfRequests, ArrayList<String> requests, String status)
	{
		this.userName = userName;
		this.displayName = displayName;
		this.password = password;
		this.noOfFriends = noOfFriends;
		this.noOfRequests = noOfRequests;
		this.requests = requests;
		this.friends = friends;
		this.status = status;
	}

	void printFriends() throws Exception
	{
		int count =0;
		if( noOfFriends != 0 )
		{
			System.out.printf("Your friends are : ");
			for (String name : friends )
			{
				count += 1;
				if (count == 1) System.out.print(name);
				else System.out.print("," + name);
			}
			System.out.println("\n");
		}
		else throw new Exception("\nNo Friends in your list\n");
	}

	void pendingRequest(Scanner input) throws Exception
	{
		int count = 0;
		System.out.println();

		if ( noOfRequests > 0 )
		{	
			for(String name:requests)
			{
				count+=1;
				System.out.print("\t"+count + ". " + name + "\n");
			}

			System.out.println("\tb. Back\n");
			int req; char c;
			c = input.next().charAt(0);

			if(c!='b')
			{
				req = Character.getNumericValue(c);
				System.out.println();
				Person to = MyNetwork.find(requests.get(req-1));

				if(to!=null)
				{
					System.out.printf(to.getdisplayName());

					System.out.printf("\n1.Accept");
					System.out.println("2.Reject\n");

					int acc_rej;
					acc_rej = input.nextInt();

					if(acc_rej == 1)
					{
						acceptRequest(to);
						System.out.printf("\nYou are now Friends with %s\n",to.getuserName());
						pendingRequest(input);
					}

					else
					{
						rejectRequest(to.getuserName());
						System.out.printf("\nFriend request by %s deleted\n",to.getuserName());
						pendingRequest(input);
					}
				}
			}
		}
		else throw new Exception("No Pending Request.\n");
	}


	void write_to_file()
	{
		try
		{
			PrintWriter write = new PrintWriter( new BufferedWriter( new FileWriter ( "input.txt",true) ) );
			write.print(userName + ',' + password + ',' + displayName + ',' + noOfFriends + ',');

			if( noOfFriends != 0 )
			for( String i:friends )
				write.print(i + ",");

			write.print(noOfRequests + ",");

			if( noOfRequests != 0 )
			for( String i:requests )
				write.print(i+',');

			write.println(status);

			write.flush();
			write.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void printDetails()
	{
		System.out.printf("Display Name: ");
		System.out.println(this.displayName);

		System.out.printf("Status: ");
		System.out.println(this.status);

		System.out.printf("Friends: ");
		int count =0;
		for(String i:this.friends)
		{
			count+=1;
			if(count ==1) System.out.printf(i);
			else System.out.printf(","+ i);
		}
		System.out.printf("\n");

	}

	void printMutualFriends(Person p2)
	{
		int count =0;
		System.out.printf("Mutual friends:");

		if( noOfFriends!=0 && p2.getnoOfFriends()!=0 )
		for(String i:this.friends)
			for(String j:p2.friends)
				if(i.equals(j))
				{
					count +=1;
					if(count==1) System.out.printf(i);
					else System.out.printf(","+i);
				}

		if(count == 0) System.out.println("No mutual friends");
		System.out.printf("\n");
	}

	void acceptRequest(Person to)
	{
		this.requests.remove(to.getuserName());
		this.friends.add(to.getuserName());
		this.noOfFriends ++;
		this.noOfRequests --;
		
		to.getfriends().add(this.getuserName());
		to.noOfFriends++;
	}

	void rejectRequest(String to)
	{
		this.requests.remove(to);
		this.noOfRequests --;
	}

	void cancelRequest(Person to)
	{
		to.getrequest().remove(this.getuserName());
		to.noOfRequests --;
	}

	void sendRequest(Person to)
	{
		to.getrequest().add(this.getuserName());
		to.noOfRequests ++;
	}

	void printShortestPath(Person to)
	{
		ArrayList <Person> visited = new ArrayList<>();
		Map <Person, Person> next = new HashMap<Person, Person>();

		System.out.print("Shortest Path: ");
		Queue <Person> q = new LinkedList <Person> ();
	    
	    Person now = this;
	    q.add(now); visited.add(now);

	    while(!q.isEmpty())
	    {
	        now = q.remove();
	        
	        if (now.equals(to)) break;
	        else if( now.getnoOfFriends() != 0 )
		        for (String friend:now.getfriends())
		        {
		            Person frnd_obj = MyNetwork.find(friend);
		            if (!visited.contains(frnd_obj))
		            {
		            	q.add(frnd_obj);
		            	visited.add(frnd_obj);
		            	next.put(now,frnd_obj);
		            }
		        }
	    }

	    if (!now.equals(to)) System.out.print("Could Not Create A Path..\n\n");
	    else 
	    {
	    	for( Person i = this; i!=null ; i = next.get(i) ) 	
		    {
		    	if( i!=this ) System.out.print(" --> ");
		    	System.out.printf( i.getuserName() );
		    }
	    	System.out.println(" --> " + to.getuserName() + "\n");
	    }
	}

	String getuserName(){ return userName; }
	String getpassword(){ return password; }
	String getdisplayName(){ return displayName; }
	String getstatus(){ return status; }
	int getNoOfRequest(){ return noOfRequests; }
	int getnoOfFriends(){ return noOfFriends; }
	ArrayList<String> getfriends(){ return friends; }
	ArrayList<String> getrequest(){ return requests; }

	void setStatus(String status) { this.status = status; }
}