package xyz.magicraft.longshort.ssf.module.message.repository;


import xyz.magicraft.longshort.ssf.generic2.Generic2Repository;
import xyz.magicraft.longshort.ssf.module.message.model.Sms;

public interface SmsRepository extends Generic2Repository<Sms> {

  Sms findByMobile(String mobile);


}