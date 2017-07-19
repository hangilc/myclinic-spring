package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.intraclinic.Post;
import jp.chang.myclinic.db.intraclinic.PostRepository;
import jp.chang.myclinic.dto.IntraclinicPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private IntraclinicPostDTO toPostDTO(Post post){
        IntraclinicPostDTO postDTO = new IntraclinicPostDTO();
        postDTO.id = post.getId();
        postDTO.content = post.getContent();
        postDTO.createdAt = post.getCreatedAt();
        return postDTO;
    }
}
