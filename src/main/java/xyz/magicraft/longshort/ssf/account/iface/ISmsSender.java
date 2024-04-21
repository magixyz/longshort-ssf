package xyz.magicraft.longshort.ssf.account.iface;

public interface ISmsSender {
	
	boolean sendCode(String mobile,String code);

}
