public abstract class GameObject implements Cloneable{
	protected String name;
	protected int health;
	protected int damage;

	public GameObject(String name, int health, int damage){
            this.name = name;
            this.health = health;
            this.damage = damage;
	}

	public abstract void attack(GameObject target);

	@Override 
	public GameObject clone(){
            try {
		return(GameObject) super.clone();
            } catch (CloneNotSupportedException e){
		throw new AssertionError();
            }
	
	}

	public boolean isAlive(){
            return health > 0;
	}

	public void takeDamage(int amount){
            health -= amount;
	}

	@Override
	public String toString(){
            return name + "[HP=" + health + "]";
	}

}






