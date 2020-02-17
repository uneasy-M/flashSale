package com.springboot.flashsale.redis.key;

public class UserKey extends BasePrefix {

	public UserKey(String prefix) {
		super(prefix);
	}

	public static UserKey token = new UserKey("token");
	public static UserKey getById = new UserKey("id");
}
