package com.gatning.ip_scan.controller;

import com.gatning.ip_scan.entity.LocalIp;
import com.gatning.ip_scan.service.LocalIpService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ip记录表(LocalIp)表控制层
 *
 * @author makejava
 * @since 2022-10-07 20:17:10
 */
@RestController
@RequestMapping("localIp")
public class LocalIpController {
    /**
     * 服务对象
     */
    @Resource
    private LocalIpService localIpService;

    /**
     * 分页查询
     *
     * @param localIp 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<LocalIp>> queryByPage(LocalIp localIp, PageRequest pageRequest) {
        return ResponseEntity.ok(this.localIpService.queryByPage(localIp, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<LocalIp> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.localIpService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param localIp 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<LocalIp> add(LocalIp localIp) {
        return ResponseEntity.ok(this.localIpService.insert(localIp));
    }

    /**
     * 编辑数据
     *
     * @param localIp 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<LocalIp> edit(LocalIp localIp) {
        return ResponseEntity.ok(this.localIpService.update(localIp));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.localIpService.deleteById(id));
    }

}

