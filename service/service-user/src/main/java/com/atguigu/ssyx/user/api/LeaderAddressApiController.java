package com.atguigu.ssyx.user.api;

import com.atguigu.ssyx.enums.user.User;
import com.atguigu.ssyx.user.service.UserService;
import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-18 13:44
 * @Description:
 **/
@Api("团长接口")
@RestController
@RequestMapping("/api/user/leader")
public class LeaderAddressApiController {

    @Autowired
    private UserService userService;

    /**
     * 根据userId查询提货点和团长信息
     * @param userId
     * @return
     */
    @ApiOperation("提货点地址信息")
    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable Long userId){
        //调用userService中已经写过的getLeaderAddressByUserId方法
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressByUserId(userId);
        return leaderAddressVo;
    }
}
