public class Zombie extends GameObject{

	public Zombie(String name, int healt, int damage){

		super(name, health, damage);
	}

	@Override
	public void attack(GameObject target){

		System.out.println(name + " bites " + target.name + " for " + damage + " damage!");
		target.takeDamage(damage);
	}
}



