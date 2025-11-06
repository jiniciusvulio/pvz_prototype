public class Game{

	public static void main(String[] args){

		// registro dos prototipos
		registry.addPrototype("peaShooter", new Plant("Pea Shooter", 100, 20));
		registry.addPrototype("basicZombie", new Zombie("Basic Shooter", 80, 10));

		// criacao dos clones
		Plant p1 = (Plant) registry.getClone("peaShooter");
		Zombie z1 = (Zombie) registry.getClone("basicZombie");

		// simulacao de batalha
		System.out.println("Battle Starts!");
		while(p1.isAlive() && z1.isAlive()){
			p1.attack(z1);
			if(z1.isAlive()) z1.attack(p1);
		}

		if(p1.isAlive()){
			System.out.println(p1.name + "wins");
		} else {
			System.out.println(z1.name + "wins");
		}

	}

}


