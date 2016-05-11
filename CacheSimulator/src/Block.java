
public class Block {
	
	private int block_size;					//Blocks have these properties.
	private int tag;
	private boolean valid;
	private boolean dirty;

	private int recency;					//We need this variable to decide Least Recently Used data.
	
	public Block(int block_size){
		
		this.block_size=block_size;
		valid=false;
		dirty=false;
		recency=0;
		
		
	}
	
	public boolean getValid(){
		return valid;
	}
	
	public boolean getDirty(){
		return dirty;
	}

	public int getRecency(){
		return recency;
	}
	
	public int getTag(){
		return tag;
	}
	
	public void setTag(int tagOfBlock){
		this.tag=tagOfBlock;
	}
	
	public void read(){							//When we read a Block its recency is incremented by 1
		valid=true;								//unless it is 3s and the block will be "valid".
		if(recency<3)
		recency++;
	}
	
	public void write(int tagOfBlock){			
		this.tag=tagOfBlock;
		if(recency<3)
			recency++;
		valid=true;
		dirty=true;
	}
	
	public void resetRecency(){
		if(recency!=0){
			recency--;
		}
	}
}
