package mock;




import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.codehaus.jackson.map.Module.SetupContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.hltc.dao.impl.UserDaoImpl;
import com.hltc.entity.User;
import com.hltc.service.IUserService;
import com.hltc.service.impl.UserServiceImpl;

public class UserMock{
	private IUserService userService;
	private UserDaoImpl userDao;
	
	public UserMock(){
		userService = new UserServiceImpl();
		
        userDao = new UserDaoImpl();
        
	}
	
	
	public void sendVerifyCode(){
		String suffix = "";
		for(int i = 0 ; i < 100; i++){
			if(i <10){
				suffix = "00"+i;
			}else if(i < 100){
				suffix = "0" + i;
			}
			System.out.println(suffix);
			userService.sendVerifyCode("10000000"+suffix, null, null);
		}
	}
	
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
		UserMock um = new UserMock();
		um.sendVerifyCode();
	}
	

}
