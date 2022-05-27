package com.github.soohea.blog.service;

import com.github.soohea.blog.dao.BlogDao;
import com.github.soohea.blog.entity.Blog;
import com.github.soohea.blog.entity.BlogResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private BlogDao blogDao;
    private UserService userService;

    @Inject
    public BlogService(BlogDao blogDao,UserService userService) {
        this.blogDao = blogDao;
        this.userService = userService;
    }

    public BlogResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try{
            List<Blog> blogs = blogDao.getBlogs(page,pageSize,userId);
            blogs.forEach(blog -> blog.setUser(userService.getUserById(blog.getUserId())));
            int count = blogDao.count(userId);
            int pageCount = (count + pageSize-1)/pageSize;
            return BlogResult.newResults(blogs,count,page,pageCount);
        }catch (Exception e){
            return BlogResult.failure("系统异常");

        }


    }
}
