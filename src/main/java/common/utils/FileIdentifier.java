package common.utils;

import common.exception.FileValidationException;

import java.util.Locale;
import java.util.Objects;

public class FileIdentifier {
	String modid;
	String name;
	
	// TODO: default to the mod loading context
	protected static String defaultId = "modid";
	
	private static final char[] validCharacters;
	
	static {
		String validCharactersStr = "abcdefghijklmnopqrstuvwxyz";
		validCharactersStr += validCharactersStr.toUpperCase(Locale.ROOT);
		validCharactersStr += "_+-/"; // TODO: what else is valid?
		validCharacters = validCharactersStr.toCharArray();
	}
	
	public FileIdentifier(String modid, String name) {
		this.modid = modid;
		this.name = name;
		validate();
	}
	
	public FileIdentifier(String identifier) {
		if (identifier.contains(":")) {
			String[] split = identifier.split(":", 2);
			modid = split[0];
			name = split[1];
		} else {
			modid = defaultId;
			name = identifier;
		}
		validate();
	}
	
	protected void validate() {
		boolean valid = validateString(modid);
		valid = valid && validateString(name);
		if (!valid)
			throw new FileValidationException("Invalid character (TODO: put the character here) in file identifier: " + modid + ":" + name);
	}
	
	// pretty sure regex is slower than this
	protected boolean validateString(String text) {
		for (int i = 0; i < text.toCharArray().length; i++) {
			boolean hit = false;
			char c = text.charAt(i);
			for (char validCharacter : validCharacters) {
				if (validCharacter == c) {
					hit = true;
					break;
				}
			}
			if (!hit) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return modid + ":" + name;
	}
	
	public String getModId() {
		return modid;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FileIdentifier that = (FileIdentifier) o;
		return Objects.equals(modid, that.modid) && Objects.equals(name, that.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(modid, name);
	}
}
