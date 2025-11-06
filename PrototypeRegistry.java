import java.util.HashMap;
import java.util.Map;


public class PrototypeRegistry{

	private Map<String, GameObject> prototypes = new HashMap<>();

	public void addPrototype(String key, GameObject prototype){

		prototypes.put(key, prototype);
	}

	
	public GameObject getClone(String key){

		GameObject protoype = prototypes.get(key);

		if (prototype != null){
			return prototype.clone();
		}
		throw new IllegalArgumentException("Prototype not found: " + key);
	}

}



