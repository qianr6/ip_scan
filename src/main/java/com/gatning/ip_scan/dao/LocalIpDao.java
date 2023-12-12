package com.gatning.ip_scan.dao;

import com.gatning.ip_scan.entity.LocalIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * ip记录表(LocalIp)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-07 20:17:12
 */
@Mapper
public interface LocalIpDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    LocalIp queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param localIp 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<LocalIp> queryAllByLimit(LocalIp localIp, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param localIp 查询条件
     * @return 总行数
     */
    long count(LocalIp localIp);

    /**
     * 新增数据
     *
     * @param localIp 实例对象
     * @return 影响行数
     */
    int insert(LocalIp localIp);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LocalIp> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LocalIp> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LocalIp> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<LocalIp> entities);

    /**
     * 修改数据
     *
     * @param localIp 实例对象
     * @return 影响行数
     */
    int update(LocalIp localIp);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据地址状态获取IP
     * @param ipStatus 状态
     * @return IP
     */
    LocalIp getByStatus(@Param("ipStatus")Boolean ipStatus,@Param("flag") String flag);

}

