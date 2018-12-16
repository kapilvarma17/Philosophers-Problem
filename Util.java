//printing for threads
public class Util {
	public static void print(String type, String direction, Integer param) {

		StringBuilder sb = new StringBuilder();
		sb.append("Philosopher no: ").append(param + 1).append(" ").append(type).append(" ").append(direction).append(" ")
				.append("chopstick");

		System.out.println(sb.toString());

	}
}
