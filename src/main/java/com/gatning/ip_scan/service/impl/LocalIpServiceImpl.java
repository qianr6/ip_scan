package com.gatning.ip_scan.service.impl;

import com.gatning.ip_scan.entity.LocalIp;
import com.gatning.ip_scan.dao.LocalIpDao;
import com.gatning.ip_scan.service.LocalIpService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * ip记录表(LocalIp)表服务实现类
 *
 * @author makejava
 * @since 2022-10-07 20:17:13
 */
@Service("localIpService")
public class LocalIpServiceImpl implements LocalIpService {
    @Resource
    private LocalIpDao localIpDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public LocalIp queryById(Integer id) {
        return this.localIpDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param localIp 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<LocalIp> queryByPage(LocalIp localIp, PageRequest pageRequest) {
        long total = this.localIpDao.count(localIp);
        return new PageImpl<>(this.localIpDao.queryAllByLimit(localIp, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param localIp 实例对象
     * @return 实例对象
     */
    @Override
    public LocalIp insert(LocalIp localIp) {
        this.localIpDao.insert(localIp);
        return localIp;
    }


    /**
     * 批量插入
     * @param localIps
     * @return
     */
    public int insertBatch(List<LocalIp> localIps) {
        return localIpDao.insertBatch(localIps);
    }

    /**
     * 修改数据
     *
     * @param localIp 实例对象
     * @return 实例对象
     */
    @Override
    public LocalIp update(LocalIp localIp) {
        this.localIpDao.update(localIp);
        return this.queryById(localIp.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.localIpDao.deleteById(id) > 0;
    }


    /**
     * 根据地址状态获取IP
     * @param ipStatus 状态
     * @return IP
     */
    @Override
    public LocalIp getByStatus(Boolean ipStatus,String flag) {
        return localIpDao.getByStatus(ipStatus,flag);
    }
}
