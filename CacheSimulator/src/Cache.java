import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Cache {

	int associativity;					//Cache has these properties
	int cache_size;
	int block_size;
	Set[] sets;
	String filename;					//Names of the txt files
	String logFileName;
	int recency;
	int tagBits;						
	int indexBits;
	int numOfHits;
	int numOfMisses;
	
	
	public Cache(int associativity, int cache_size, int block_size, String filename, String logFileName, int recency){
		
		this.block_size=block_size; 			
		this.cache_size=cache_size;			
		this.associativity=associativity;
		numOfHits=0;
		numOfMisses=0;
		this.filename=filename;
		this.logFileName=logFileName;
		sets = new Set[cache_size/associativity/block_size];
		for(int i=0; i<sets.length; i++){
			sets[i] = new Set(associativity, block_size, i);
		}
		
		int byteOffset=(int)(Math.log10(block_size)/Math.log10(2));	
		indexBits = (int)(Math.log10(sets.length)/Math.log10(2));
		tagBits=32-indexBits-byteOffset;
		
	}
	
	public void run(){							//This is where we decide hits and misses and perform
												//the read and write operations.
		BufferedReader in;
		BufferedWriter writer;
		try {
			in = new BufferedReader(new FileReader(filename));
			writer = new BufferedWriter(new FileWriter (logFileName, false));
			
			String line;
			int lineCounter =0;
			while((line = in.readLine()) != null)
			{
				lineCounter++;
				Integer intLine = Integer.parseInt(line);
				String bitLine = intLine.toBinaryString(intLine);
				while(bitLine.length()<32){
					bitLine="0"+bitLine;
				}
				String indexOfLine = bitLine.substring(tagBits, tagBits+indexBits);
				String tagOfLine = bitLine.substring(0, tagBits);
				int indexOfSet = Integer.parseUnsignedInt(indexOfLine, 2);
				int tagOfBlock = Integer.parseUnsignedInt(tagOfLine, 2);
				
				if(lineCounter%5==0){								//If the count of line is multiple of 5
					switch (sets[indexOfSet].write(tagOfBlock)) {	//we perform a write operation for that line.
					case 0:											//We have 3 cases that are numbered by us to
						numOfHits++;								//decide whether it is a hit, miss with a dirty bit or
						System.out.println(""+intLine+" is hit.");	//miss with a clean bit."0" indicates that it is a hit.
						writer.write(""+intLine+" is hit."+"\r\n");	//"1" tell us that it is a miss and the block is not dirty.
						break;										//"2" shows that it is a miss and the block is dirty.
					case 1:											//We do this operation by taking random places.
						numOfMisses++;
						System.out.println(""+intLine+" is miss and it is not dirty.");
						writer.write(""+intLine+" is miss and it is not dirty."+"\r\n");
						break;
					case 2:
						numOfMisses++;
						System.out.println(""+intLine+" is miss and it is dirty. Hence write back is needed.");
						writer.write(""+intLine+" is miss and it is dirty. Hence write back is needed."+"\r\n");
						break;

					default:
						break;
					}
					
					
				}else{
					switch (sets[indexOfSet].read(tagOfBlock)) {			//If the count of line is not multiple of 5,
					case 0:													//then we perform read operation. The cases are
						numOfHits++;										//the same with the write cases above.
						System.out.println(""+intLine+" is hit.");
						writer.write(""+intLine+" is hit."+"\r\n");
						break;
					case 1:
						numOfMisses++;
						System.out.println(""+intLine+" is miss and it is not dirty.");
						writer.write(""+intLine+" is miss and it is not dirty."+"\r\n");
						break;
					case 2:
						numOfMisses++;
						System.out.println(""+intLine+" is miss and it is dirty. Hence write back is needed.");
						writer.write(""+intLine+" is miss and it is dirty. Hence write back is needed."+"\r\n");
						break;

					default:
						break;
					}
					
					
				}
				

			}
			in.close();
			
			System.out.println("Number of hits: "+numOfHits);
			System.out.println("Number of misses: "+numOfMisses);
			writer.write("Number of misses: "+numOfMisses+"\r\n");
			writer.write("Number of hits: "+numOfHits+"\r\n");
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


