public class Plant extends GameObject{
	public Plant(String name, int health, int damage){
		super(name, health, damage);
	}

	@Override
	public void attack(GameObject target){
		System.out.println(name + " atacou " + target.name + ", causando " + damage + " de dano!");
		target.takeDamage(damage);
	}

	public boolean isDefensive(){
		// Exemplo simples: plantas com dano 0 são defensivas
		return name.equalsIgnoreCase("WallNut");
	}


}



