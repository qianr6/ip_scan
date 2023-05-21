package com.gatning.ip_scan.service;

import com.gatning.ip_scan.entity.LocalIp;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ip记录表(LocalIp)表服务接口
 *
 * @author makejava
 * @since 2022-10-07 20:17:12
 */
@Service
public interface LocalIpService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    LocalIp queryById(Integer id);

    /**
     * 分页查询
     *
     * @param localIp 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<LocalIp> queryByPage(LocalIp localIp, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param localIp 实例对象
     * @return 实例对象
     */
    LocalIp insert(LocalIp localIp);

    /**
     * 修改数据
     *
     * @param localIp 实例对象
     * @return 实例对象
     */
    LocalIp update(LocalIp localIp);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);


    /**
     * 根据地址状态获取IP
     * @param ipStatus 状态
     * @return IP
     */
    List<LocalIp> getByStatus(@Param("ipStatus") Boolean ipStatus);

}
