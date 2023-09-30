package com.atguigu.ssyx.client.user;

import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-user")//要调用的包名
public interface UserFeignClient {

    @ApiOperation("提货点地址信息")
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    LeaderAddressVo getUserAddressByUserId(@PathVariable Long userId);
}
