package dev.e23.dashstar.handler;

import dev.e23.dashstar.model.Article;
import dev.e23.dashstar.model.Comment;
import dev.e23.dashstar.model.User;
import dev.e23.dashstar.repository.ArticleRepository;
import dev.e23.dashstar.repository.CommentRepository;
import dev.e23.dashstar.repository.UserRepository;
import dev.e23.dashstar.security.Secured;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.HashMap;
import java.util.Map;

@Path("/comments")
public class CommentHandler {

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ArticleRepository articleRepository;

    @POST
    @Path("/")
    @Secured({"user", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createComment(Comment comment , @Context SecurityContext securityContext /* 从请求上下文中获得在 AuthenticationFilter 中的 SecurityContext */) {
        User user = userRepository.findByID(Integer.valueOf(securityContext.getUserPrincipal().getName()));  // 从 SecurityContext 中获得当前登录的用户
        comment.setUser(user);
        Article article = articleRepository.findByID(comment.getArticleId());
        comment.setArticle(article);
        comment.setCreatedAt(System.currentTimeMillis() / 1000);
        commentRepository.create(comment);
        Map<String, Object> res = new HashMap<>();
        res.put("code", Response.Status.OK);
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam("id") Integer id) {
        commentRepository.delete(id);
        return Response.status(Response.Status.OK).build();
    }

}
