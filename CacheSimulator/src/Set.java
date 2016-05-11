import java.util.Random;

public class Set {

	int associativity;					//Sets have these properties.
	int block_size;
	Block[] blockOfSet;
	int index;
	
	
	public Set(int associativity, int block_size, int index){
		this.associativity=associativity;
		this.block_size=block_size;
		this.index=index;
		
		blockOfSet = new Block[associativity];			//We create blocks in the set. If a set is a 2-way set associative
		for(int i=0; i<associativity; i++){				//then a set has 2 blocks in it.
			blockOfSet[i]= new Block(block_size);
		}
	}
	
	public int getIndex(){
		return index;
	}
	
	public int write(int tagOfBlock){			//0=hit 1=miss and clean 2=miss and dirty
		int isHit=1;
		for(int i=0; i<blockOfSet.length; i++){
			if(blockOfSet[i].getTag()==tagOfBlock){			//We have two different write operations; one for
				isHit=0;									//random and the other is for LRU.
				blockOfSet[i].write(tagOfBlock);			//Random write selects a random block among the
			}												//blocks in a set and write the tag there.
		}
		if(isHit==1){
		Random rand = new Random();
		int randomBlock = rand.nextInt(associativity);
		if(blockOfSet[randomBlock].getDirty()){
			isHit=2;
		}
		blockOfSet[randomBlock].write(tagOfBlock);
		}
		return isHit;
	}
	
	public int writeLRU(int tagOfBlock){				//WriteLRU is the write operation of LRU. It checks
		int isHit=1;									//all the blocks in a set to find the minimum recency value
		for(int i=0; i<blockOfSet.length; i++){			//between them. If there are more than one blocks that has
			if(blockOfSet[i].getTag()==tagOfBlock){		//minimum recency, then it does the selection randomly amongst them.
				isHit=0;
				blockOfSet[i].write(tagOfBlock);
			}
		}
		if(isHit==1){
		int minRecency=10;		//it is not important, we assigned a random number to minRecency
		int numOfTieRecencies=0;
		for(int i=0; i<blockOfSet.length; i++){
			if(blockOfSet[i].getRecency()<minRecency){
				minRecency=blockOfSet[i].getRecency();
				numOfTieRecencies=1;
			}
			if(blockOfSet[i].getRecency()==minRecency){
				numOfTieRecencies++;
			}
		}
		Random rand = new Random();
		int randomBlock = rand.nextInt(numOfTieRecencies);
		for(int i=0; i<blockOfSet.length; i++){
			if(blockOfSet[i].getRecency()==minRecency){
				if(randomBlock==0){
					randomBlock = i;
					break;
				}else{
					randomBlock--;
				}
			}
		}
		
		if(blockOfSet[randomBlock].getDirty()){
			isHit=2;
		}
		blockOfSet[randomBlock].write(tagOfBlock);
		}
		return isHit;
	}
	
	public void resetRecencies(){						
		for(int i=0; i<blockOfSet.length; i++){
			blockOfSet[i].resetRecency();
		}
	}
	
	public int read(int tagOfBlock){					//We have two different read operations; one for random
		int isHit=1;									//and the one is for LRU. Random read finds the random-indexed block
		for(int i=0; i<blockOfSet.length; i++){			//and checks its dirty bit if it is hit or not.
			if(blockOfSet[i].getTag()==tagOfBlock){
				isHit=0;
				blockOfSet[i].read();
			}
		}
		if(isHit==1){
			Random rand = new Random();
			int randomBlock = rand.nextInt(associativity);
			if(blockOfSet[randomBlock].getDirty()){
				isHit=2;
			}
			blockOfSet[randomBlock].setTag(tagOfBlock);
			blockOfSet[randomBlock].read();
			}
		return isHit;
		
	}
	
	public int readLRU(int tagOfBlock){					//ReadLRU it checks all the blocks in a set to find the minimum recency value
		int isHit=1;									//between them. If there are more than one blocks that has
		for(int i=0; i<blockOfSet.length; i++){			//minimum recency, then it does the selection randomly amongst them.
			if(blockOfSet[i].getTag()==tagOfBlock){		//Then performs reading operation.
				isHit=0;
				blockOfSet[i].read();
			}
		}
		if(isHit==1){
			int minRecency=10;		//it is not important, we assigned a random number to minRecency
			int numOfTieRecencies=0;
			for(int i=0; i<blockOfSet.length; i++){
				if(blockOfSet[i].getRecency()<minRecency){
					minRecency=blockOfSet[i].getRecency();
					numOfTieRecencies=1;
				}
				if(blockOfSet[i].getRecency()==minRecency){
					numOfTieRecencies++;
				}
			}
			Random rand = new Random();
			int randomBlock = rand.nextInt(numOfTieRecencies);
			for(int i=0; i<blockOfSet.length; i++){
				if(blockOfSet[i].getRecency()==minRecency){
					if(randomBlock==0){
						randomBlock = i;
						break;
					}else{
						randomBlock--;
					}
				}
			}
			
			if(blockOfSet[randomBlock].getDirty()){
				isHit=2;
			}
			blockOfSet[randomBlock].setTag(tagOfBlock);
			blockOfSet[randomBlock].read();
			}
		return isHit;
	}
	

	
}
