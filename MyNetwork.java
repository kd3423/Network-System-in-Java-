//@ Author : Gurpreet (2015140) & Karan(2015141)

import java.util.*;
import java.io.*;

class MyNetwork
{
	private	static ArrayList <Person> usrList = new ArrayList <>();
	private static Scanner input = new Scanner( System.in );

	public static void main(String [] args)
	{
		read_file();

		System.out.printf("Reading database file... \n");
		System.out.printf("Network is ready.\n\n");

		while(true)
		{
			System.out.printf("1 Sign up\n");
			System.out.printf("2 Login\n\n");
			int x = input.nextInt();

			if(x==1) sign_up();
			else if(x==2) user_menu();
			else System.out.printf("Wrong input Try again!\n");

			// to clear contents of input.txt file

			try
			{
				PrintWriter bw = new PrintWriter("input.txt");
				bw.close();

				usrList.forEach( Person::write_to_file );
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void sign_up()
	{
		String usr_name,disp_name,password;
		input.nextLine();

		System.out.printf("Enter username: ");
		usr_name = input.nextLine();

		System.out.printf("Enter display name: ");
		disp_name = input.nextLine();

		System.out.printf("Enter password: ");
		password = input.nextLine();

		// check for same username
		try
		{
			check_user(usr_name);

			Person obj = new Person(usr_name,disp_name,password);
			System.out.printf("\nRegistration is successful User " + usr_name +" created\n\n");
			usrList.add(obj);
		}
		catch( Exception e ) { e.printStackTrace(); }
	}

	private static void user_menu()
	{
		String usr_name,password;
		boolean ch = true;
		input.nextLine();

		System.out.printf("\nPlease enter your username: ");
		usr_name = input.nextLine();

		System.out.printf("Please enter your password: ");
		password = input.nextLine();

		// check for wrong username or password
		try 
		{ 
			Person usr = check_user_pass(usr_name,password);
		
			System.out.print("\n" + usr.getdisplayName() + " logged in now.\n" + usr.getstatus() + "\n\n");
			while(ch)
			{
				int userMenu;
				System.out.printf("\n\t1.List Friends\n");
				System.out.printf("\t2.Search\n");
				System.out.printf("\t3.Update status\n");
				System.out.printf("\t4.Pending request\n");
				System.out.printf("\t5.Logout\n\n");

				userMenu = input.nextInt();

				// list friends
				if(userMenu == 1)
				{
					try{ usr.printFriends(); }
					catch( Exception e ){ e.printStackTrace(); }
				}
				// search
				else if(userMenu == 2)
				{
					//search is here
					String find;

					System.out.printf("Enter name: ");
					input.nextLine();
					find = input.nextLine();

					try
					{
						search(usr,find);
						System.out.println();
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}

				}

				// update
				else if(userMenu == 3)
				{
					String stat;
					System.out.printf("Enter Status: ");

					input.nextLine();
					stat = input.nextLine();

					usr.setStatus(stat);
					
					System.out.println("Status Updated!!");
				}

				// peding request
				else if(userMenu == 4)
				{
					try { usr.pendingRequest(input); }
					catch( Exception e ) { e.printStackTrace(); }
				}

				else if(userMenu == 5)
				{
					System.out.printf("User "+(usr.getuserName())+" logged out successfully.\n\n");
					ch = false;
				}

				else System.out.printf("\nInvalid input!!\n\n");
			}
		}
		catch( Exception e ) { e.printStackTrace(); }
	}

	private static void search(Person usr, String find) throws Exception
	{
		boolean flag = true;
		for(Person obj:usrList)
			if((obj.getuserName()).equals(find))
			{
				flag = false;
				if( usr.getnoOfFriends()!=0  && usr.getfriends().contains(find) )
				{
					//usr and find are friends
					System.out.printf("\nYou and "+find+" are friends\n\n");
					obj.printDetails();
					usr.printMutualFriends(obj);
					System.out.printf("\n\tb. back\n");
					input.next().charAt(0);
				}
				else // not friends
				{
					System.out.printf(find+" is not a friend\n");
					usr.printMutualFriends(obj);
					usr.printShortestPath(obj);

					if( obj.getNoOfRequest()>0 && obj.getrequest().contains(usr.getuserName()) ) // but request is pending
					{
						System.out.printf("Request Pending.\n");
						System.out.printf("\t1. Cancel Request\n");
						System.out.printf("\tb. back\n");

						char x = input.next().charAt(0);
						if(Character.getNumericValue(x) == 1)
						{
							usr.cancelRequest(obj);
							System.out.println("Your request has been cancelled\n");
						}
					}
					else // option to send request
					{
						System.out.printf("\t1. Send Request\n");
						System.out.printf("\tb. back\n");

						char x = input.next().charAt(0);
						if(Character.getNumericValue(x) ==1)
						{
							usr.sendRequest(obj);
							System.out.println("Your request has been sent\n");
						}
					}

				}
			}

		if (flag)
		{
			throw new Exception("User not found!!\n\n");
		}
	}

	// Check for username and password for login and then return obj from arraylist for that user
	private static Person check_user_pass(String username ,String pass) throws Exception
	{
		for(Person obj:usrList)
			if(((obj.getuserName()).equals(username))&&((obj.getpassword()).equals(pass)))
				return obj;

		throw new Exception(" Invalid Username or Password Try again!\n ");
	}

	// this is required for calculating shortest path
	protected static Person find( String user_name )
	{
		for(Person obj:usrList)
			if(obj.getuserName().equals(user_name))
				return obj;
		return null;
	}

	private static void check_user( String user_name ) throws Exception
	{
		for(Person obj:usrList)
			if(obj.getuserName().equals(user_name))
				throw new Exception("Username not Avaliable!!\n");
	}

	private static void read_file()
	{
		BufferedReader br;
		String line;
		try
		{
			int i;
			br = new BufferedReader(new FileReader("input.txt"));
			while ((line = br.readLine()) != null)
			{
				String userData[] = line.split(",");
				String userName = userData[0];
				String password = userData[1];
				String displayName = userData[2];

				int noOfFriends = Integer.parseInt(userData[3]);
				ArrayList<String> friends = null;
				if( noOfFriends > 0 )
				{
					friends = new ArrayList<>();
					for (i = 1; i <= noOfFriends; i++)
						friends.add(userData[i+3]);
				}

				int noOfRequests = Integer.parseInt(userData[4+noOfFriends]);
				ArrayList<String> requests = null;
				if( noOfRequests > 0 )
				{
					requests = new ArrayList <>();
					for(i=1;i<=noOfRequests;i++)
						requests.add(userData[i+4+noOfFriends]);
				}

				String status = userData[5+noOfRequests+noOfFriends];
				usrList.add(new Person(userName,displayName,password,noOfFriends,friends,noOfRequests,requests,status));
			}

			br.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}