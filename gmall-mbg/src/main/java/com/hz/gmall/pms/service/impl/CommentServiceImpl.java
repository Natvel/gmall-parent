package com.hz.gmall.pms.service.impl;

import com.hz.gmall.pms.entity.Comment;
import com.hz.gmall.pms.mapper.CommentMapper;
import com.hz.gmall.pms.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author Enzo
 * @since 2020-03-30
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
