
public class CacheRandom_Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a=1;								//Default values of the cache properties.
		int s=512;
		int b=4;
		String f="addresses.txt";
		String d="logFile.txt";					//We select the "logFile" to write the results in txt.
		int n=100;
		
		for(int i=0; i<args.length; i++){		//This part is essential for taking the args from console.
			switch (args[i]) {
			case "-a":
				a=Integer.parseInt(args[i+1]);
				break;
			case "-s":
				s=Integer.parseInt(args[i+1]);
				break;
			case "-b":
				b=Integer.parseInt(args[i+1]);
				break;
			case "-f":
				f=args[i+1];
				break;
			case "-d":
				d=args[i+1];
				break;
			case "-n":
				n=Integer.parseInt(args[i+1]);
				break;
			default:
				break;
			}
		}
		
		Cache cash = new Cache(a,s,b,f,d,n);
		cash.run();

	}

}
