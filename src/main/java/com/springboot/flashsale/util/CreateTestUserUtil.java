package com.springboot.flashsale.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springboot.flashsale.pojo.User;

@Service
public class CreateTestUserUtil {
	/**
	 * 创建count个用户
	 * 
	 * @param count
	 * @return
	 * @throws IOException 
	 */
	public List<User> createTestUser(int count) throws IOException {
		List<User> userList = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			User flashSaleUser = new User();
			flashSaleUser.setId(19970000000L + i);
			flashSaleUser.setNickname("user" + i);
			flashSaleUser.setPassword("b7797cce01b4b131b433b6acf4add449");
			flashSaleUser.setSalt("1a2b3c4d");
			flashSaleUser.setLogin_count(0);
			userList.add(flashSaleUser);
		}
		System.out.println("共生成"+count+"个用户");
		return userList;
	}

	public boolean createTestUserToken(List<User> userList) throws IOException {
		String urlString = "http://localhost:8080/flashsale/do_Login";
		File file = new File("D:/tokens.txt");
		if (file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		file.createNewFile();
		raf.seek(0);
		for (int i = 0; i <= userList.size()-1; i++) {
			User user = userList.get(i);
			URL url = new URL(urlString);
			HttpURLConnection co = (HttpURLConnection) url.openConnection();
			co.setRequestMethod("POST");
			//POST方式需要向服务器发送输出流，所以要添加一个参数设置：
			co.setDoOutput(true);
			
			OutputStream out = co.getOutputStream();
			String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
			out.write(params.getBytes());
			out.flush();
			
			
			InputStream inputStream = co.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0, len);
			}
			inputStream.close();
			bout.close();
			
			String response = new String(bout.toByteArray());
			JSONObject jo = JSON.parseObject(response);
			String token = jo.getString("data");
			System.out.println("create token : " + user.getId());

			String row = user.getId() + "," + token;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\r\n".getBytes());
			System.out.println("write to file : " + user.getId());
		}
		raf.close();

		System.out.println("登陆token写入完成!");

		return true;
	}

}
