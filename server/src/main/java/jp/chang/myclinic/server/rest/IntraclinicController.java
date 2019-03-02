package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbmysql.intraclinic.Comment;
import jp.chang.myclinic.dbmysql.intraclinic.CommentRepository;
import jp.chang.myclinic.dbmysql.intraclinic.Post;
import jp.chang.myclinic.dbmysql.intraclinic.PostRepository;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.server.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json/intraclinic")
@Transactional
class IntraclinicController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRegistry userRegistry;

    @RequestMapping(value = "/get-post-full", method = RequestMethod.GET)
    public IntraclinicPostFullDTO getPostFull(@RequestParam("post-id") int id) {
        return toPostFullDTO(postRepository.findById(id));
    }

    @RequestMapping(value = "/list-recent-full", method = RequestMethod.GET)
    public List<IntraclinicPostFullDTO> listRecentFull() {
        return postRepository.findTop7ByOrderByIdDesc().stream().map(this::toPostFullDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = "/list-post-full", method = RequestMethod.GET)
    public IntraclinicPostFullPageDTO listPostFull(@RequestParam("page") int page) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page, 7, Sort.Direction.DESC, "id"));
        IntraclinicPostFullPageDTO pageDTO = new IntraclinicPostFullPageDTO();
        pageDTO.totalPages = postPage.getTotalPages();
        pageDTO.posts = postPage.map(this::toPostFullDTO).getContent();
        return pageDTO;
    }

    @RequestMapping(value = "/enter-post", method = RequestMethod.POST)
    public int enterPost(@RequestBody IntraclinicPostDTO postDTO) {
        Post post = fromPostDTO(postDTO);
        post.setId(null);
        post = postRepository.save(post);
        return post.getId();
    }

    @RequestMapping(value = "/update-post", method = RequestMethod.POST)
    public boolean updatePost(@RequestBody IntraclinicPostDTO postDTO) {
        Post post = fromPostDTO(postDTO);
        postRepository.save(post);
        return true;
    }

    @RequestMapping(value = "/delete-post", method = RequestMethod.POST)
    public boolean deletePost(@RequestParam("post-id") int postId) {
        postRepository.deleteById(postId);
        return true;
    }

    @RequestMapping(value = "/enter-comment", method = RequestMethod.POST)
    public int enterComment(@RequestBody IntraclinicCommentDTO commentDTO) {
        Comment comment = fromCommentDTO(commentDTO);
        comment.setId(null);
        comment = commentRepository.save(comment);
        return comment.getId();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserInfoDTO login(@RequestParam("user") String user, @RequestParam("password") String password) {
        UserRegistry.UserInfo userInfo = userRegistry.getInfo(user);
        if (userInfo.getPassword().equals(password)) {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.user = user;
            userInfoDTO.name = userInfo.getName();
            userInfoDTO.roles = userInfo.getRoles();
            return userInfoDTO;
        } else {
            throw new RuntimeException("invalid password");
        }
    }

    private IntraclinicPostDTO toPostDTO(Post post) {
        IntraclinicPostDTO postDTO = new IntraclinicPostDTO();
        postDTO.id = post.getId();
        postDTO.content = post.getContent();
        postDTO.createdAt = post.getCreatedAt();
        return postDTO;
    }

    private Post fromPostDTO(IntraclinicPostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.id);
        post.setContent(postDTO.content);
        post.setCreatedAt(postDTO.createdAt);
        return post;
    }

    private IntraclinicPostFullDTO toPostFullDTO(Post post) {
        IntraclinicPostFullDTO postFullDTO = new IntraclinicPostFullDTO();
        postFullDTO.post = toPostDTO(post);
        postFullDTO.comments = commentRepository.findByPostId(post.getId(), Sort.by("id")).stream()
                .map(this::toCommentDTO).collect(Collectors.toList());
        return postFullDTO;
    }

    private IntraclinicCommentDTO toCommentDTO(Comment comment) {
        IntraclinicCommentDTO commentDTO = new IntraclinicCommentDTO();
        commentDTO.id = comment.getId();
        commentDTO.name = comment.getName();
        commentDTO.content = comment.getContent();
        commentDTO.postId = comment.getPostId();
        commentDTO.createdAt = comment.getCreatedAt();
        return commentDTO;
    }

    private Comment fromCommentDTO(IntraclinicCommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.id);
        comment.setName(commentDTO.name);
        comment.setContent(commentDTO.content);
        comment.setPostId(commentDTO.postId);
        comment.setCreatedAt(commentDTO.createdAt);
        return comment;
    }
}
