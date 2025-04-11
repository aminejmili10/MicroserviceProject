package com.example.blogs.Services;

import com.example.blogs.Entities.Post;
import com.example.blogs.Repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class PostServiceImp implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        post.setLikeCount(0);
        post.setViewCount(0);
        post.setDate(new Date());
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long postId, Post postDetails) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            throw new EntityNotFoundException("Post Not Found");
        }

        Post post = optionalPost.get();
        post.setName(postDetails.getName());
        post.setContent(postDetails.getContent());
        post.setImg(postDetails.getImg());

        post.setPostedBy(postDetails.getPostedBy());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            try {
                postRepository.deleteById(postId);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la suppression du post", e);
            }
        } else {
            throw new EntityNotFoundException("Post Not Found");
        }



    }
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post getPostById(Long postId) {
        Optional<Post> optionlPost = postRepository.findById(postId);
        if (optionlPost.isPresent()) {
            Post post = optionlPost.get();
            post.setViewCount(post.getViewCount() + 1);
            return postRepository.save(post);
        } else {
            throw new EntityNotFoundException("Post Not Found");

        }

    }

}
