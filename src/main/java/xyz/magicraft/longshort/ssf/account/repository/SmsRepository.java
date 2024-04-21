package xyz.magicraft.longshort.ssf.account.repository;



import xyz.magicraft.longshort.ssf.account.model.Sms;
import xyz.magicraft.longshort.ssf.generic.IGenericRepository;

public interface SmsRepository extends IGenericRepository<Sms> {

  Sms findByMobile(String mobile);


}