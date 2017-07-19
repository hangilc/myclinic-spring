package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.intraclinic.Post;
import jp.chang.myclinic.db.intraclinic.PostRepository;
import jp.chang.myclinic.dto.IntraclinicPostDTO;
import jp.chang.myclinic.dto.IntraclinicPostPageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json/intraclinic")
@Transactional("intraclinicTransactionManager")
class IntraclinicController {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping(value="/get-post", method= RequestMethod.GET)
    public IntraclinicPostDTO getPost(@RequestParam("post-id") int id){
        return toPostDTO(postRepository.findOne(id));
    }

    @RequestMapping(value="/list-recent", method=RequestMethod.GET)
    public List<IntraclinicPostDTO> listRecent(){
        return postRepository.findTop7ByOrderByIdDesc().stream().map(this::toPostDTO).collect(Collectors.toList());
    }

    @RequestMapping(value="/enter-post", method=RequestMethod.POST)
    public int enterPost(IntraclinicPostDTO postDTO){
        Post post = fromPostDTO(postDTO);
        post.setId(0);
        post = postRepository.save(post);
        return post.getId();
    }

    @RequestMapping(value="/update-post", method=RequestMethod.POST)
    public boolean updatePost(IntraclinicPostDTO postDTO){
        Post post = fromPostDTO(postDTO);
        postRepository.save(post);
        return true;
    }

    @RequestMapping(value="/list-post", method=RequestMethod.GET)
    public IntraclinicPostPageDTO listPost(@RequestParam("page") int page){
        Page<Post> postPage = postRepository.findAll(new PageRequest(page, 7, Sort.Direction.DESC, "id"));
        IntraclinicPostPageDTO pageDTO = new IntraclinicPostPageDTO();
        pageDTO.totalPages = postPage.getTotalPages();
        pageDTO.posts = postPage.map(this::toPostDTO).getContent();
        return pageDTO;
    }

    @RequestMapping(value="/delete-post", method=RequestMethod.POST)
    public boolean deletePost(@RequestParam("post-id") int postId){
        postRepository.delete(postId);
        return true;
    }

    private IntraclinicPostDTO toPostDTO(Post post){
        IntraclinicPostDTO postDTO = new IntraclinicPostDTO();
        postDTO.id = post.getId();
        postDTO.content = post.getContent();
        postDTO.createdAt = post.getCreatedAt();
        return postDTO;
    }

    private Post fromPostDTO(IntraclinicPostDTO postDTO){
        Post post = new Post();
        post.setId(postDTO.id);
        post.setContent(postDTO.content);
        post.setCreatedAt(postDTO.createdAt);
        return post;
    }
}
