public class Zombie extends GameObject{

	public Zombie(String name, int health, int damage){
            super(name, health, damage);
	}

	@Override
	public void attack(GameObject target){
            System.out.println(name + " mordeu " + target.name + ", causando " + damage + " de dano!");
            target.takeDamage(damage);
	}
}



