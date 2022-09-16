import common.exception.FileValidationException;
import common.utils.FileIdentifier;

public class FileIdentifierTest {
	public static void main(String[] args) {
		System.out.println(new FileIdentifier("modid:test"));
		System.out.println(new FileIdentifier("modid:test/string"));
		System.out.println(new FileIdentifier("modid:test+string"));
		System.out.println(new FileIdentifier("modid:test-string"));
		try {
			System.out.println(new FileIdentifier("modid:test:string"));
			throw new RuntimeException();
		} catch (FileValidationException err) {
		}
		try {
			System.out.println(new FileIdentifier("modid:test string"));
			throw new RuntimeException();
		} catch (FileValidationException err) {
		}
	}
}
