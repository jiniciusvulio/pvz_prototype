public class Plant extends GameObject{
	public Plant(String name, int health, int damage){
		super(name, health, damage);
	}

	@Override
	public void attack(GameObject target){
		System.out.println(name + " shoots " + target.name + " for " + damage + " damage!");
		target.takeDamage(damage);
	}


}



