package com.iotinall.canteen.common.security;

public interface IUserService {

    String openIdKey = "-openid-";

    SecurityUserDetails loadDataByOpenId(String openid);
}
