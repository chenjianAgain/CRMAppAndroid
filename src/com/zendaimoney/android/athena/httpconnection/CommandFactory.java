package com.zendaimoney.android.athena.httpconnection;

public abstract class CommandFactory {
	public abstract BaseCommand getLogin(String user, String password);
	public abstract BaseCommand getValidateAccount(String user, String dimeCode);
	public abstract BaseCommand getRelevanceCustomer(String cardId, String id);
	public abstract BaseCommand getRemindInfos(String id);
	public abstract BaseCommand getCreditList(String infoType, String pageNum, String pageSize);
	public abstract BaseCommand getBirthList(String infoType, String pageNum, String pageSize);
}
