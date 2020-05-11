package com.hz.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.ums.entity.Member;
import com.hz.gmall.ums.entity.MemberReceiveAddress;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */

public interface MemberService extends IService<Member> {

	Member login(String username,String password);

	Member getMemberByAccessToken(String accessToken);

	List<MemberReceiveAddress> getMemberAddress(Long id);

	MemberReceiveAddress getMemberAddressByAddressId(Long addressId);
}
