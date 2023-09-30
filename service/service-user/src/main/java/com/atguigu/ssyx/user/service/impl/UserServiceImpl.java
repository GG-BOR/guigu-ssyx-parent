package com.atguigu.ssyx.user.service.impl;

import com.atguigu.ssyx.enums.user.Leader;
import com.atguigu.ssyx.enums.user.User;
import com.atguigu.ssyx.enums.user.UserDelivery;
import com.atguigu.ssyx.user.mapper.LeaderMapper;
import com.atguigu.ssyx.user.mapper.UserDeliveryMapper;
import com.atguigu.ssyx.user.mapper.UserMapper;
import com.atguigu.ssyx.user.service.UserService;
import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import com.atguigu.ssyx.vo.user.UserLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-16 16:23
 * @Description:
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private LeaderMapper leaderMapper;

    @Autowired
    private UserDeliveryMapper userDeliveryMapper;
    /**
     * 通过openId查询数据库中的用户信息
     * @param openId
     * @return
     */
    @Override
    public User getUserByOpenId(String openId) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
        return user;
    }

    /**
     * 根据userId查询提货点和团长信息
     * @param id
     * @return
     */
    @Override
    public LeaderAddressVo getLeaderAddressByUserId(Long id) {
        //根据userId查询用户默认的团长id
        UserDelivery userDelivery = userDeliveryMapper.selectOne(
                new LambdaQueryWrapper<UserDelivery>()
                        .eq(UserDelivery::getUserId, id)
                        .eq(UserDelivery::getIsDefault, 1));
        if(userDelivery==null){
            return null;
        }
        //拿着上面查询团长id查询leader表查询团长其他信息
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());

        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtils.copyProperties(leader,leaderAddressVo);
        leaderAddressVo.setUserId(id);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    /**
     * 获取当前登录用户信息
     * @param id
     * @return
     */
    @Override
    public UserLoginVo getUserLoginVo(Long id) {
        //先获取用户信息
        User user = baseMapper.selectById(id);
        //将用户id拷贝过去
        UserLoginVo userLoginVo = new UserLoginVo();
        BeanUtils.copyProperties(user,userLoginVo);
        UserDelivery userDelivery = userDeliveryMapper.selectOne(
                new LambdaQueryWrapper<UserDelivery>()
                        .eq(UserDelivery::getUserId, id)
                        .eq(UserDelivery::getIsDefault, 1));
        if(userDelivery!=null){
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
            userLoginVo.setWareId(userDelivery.getWareId());
        }else {
            userLoginVo.setLeaderId(1L);
            userLoginVo.setWareId(1L);
        }
        return userLoginVo;
    }
}
