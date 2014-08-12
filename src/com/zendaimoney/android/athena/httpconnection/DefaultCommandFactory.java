package com.zendaimoney.android.athena.httpconnection;

public class DefaultCommandFactory extends CommandFactory {
	
	public BaseCommand getLogin(String user, String password){
		Login login = new Login();
		login.setUser(user);
		login.setPasswd(password);
		
		return login;
	}

	@Override
	public BaseCommand getValidateAccount(String user, String dimeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseCommand getRelevanceCustomer(String cardId, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseCommand getRemindInfos(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseCommand getCreditList(String infoType, String pageNum,
			String pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseCommand getBirthList(String infoType, String pageNum,
			String pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
}
