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
import java.util.List;
import java.util.Map;

@Path("/articles")
public class ArticleHandler {

    @Inject
    private ArticleRepository articleRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private UserRepository userRepository;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        Map<String, Object> res= new HashMap<>();
        res.put("code", Response.Status.OK);
        res.put("data", articles);
        return Response.status(Response.Status.OK).entity(res).build();
    }
//aaa
    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticlesByPage(@PathParam("page") int page) {
        List<Article> articles = articleRepository.findAll();
        int begin = (page - 1) * 3;
        int end = begin + 3;
        Map<String, Object> res= new HashMap<>();
        res.put("code", Response.Status.OK);
        res.put("data", articles.subList(begin, end));
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticleById(@PathParam("id") Integer id) {
        Article article = articleRepository.findByID(id);
        Map<String, Object> res = new HashMap<>();
        res.put("code", Response.Status.OK);
        res.put("data", article);
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @GET
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@PathParam("id") Integer id) {
        List<Comment> comments = commentRepository.findByArticleId(id);
        Map<String, Object> res = new HashMap<>();
        res.put("code", Response.Status.OK);
        res.put("data", comments);
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @POST
    @Path("/")
    @Secured({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createArticle(Article article , @Context SecurityContext securityContext /* 从请求上下文中获得在 AuthenticationFilter 中的 SecurityContext */) {
        User author = userRepository.findByID(Integer.valueOf(securityContext.getUserPrincipal().getName()));  // 从 SecurityContext 中获得当前登录的用户
        article.setAuthor(author);
        article.setCreatedAt(System.currentTimeMillis() / 1000);
        articleRepository.create(article);
        Map<String, Object> res = new HashMap<>();
        res.put("code", Response.Status.OK);
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @PUT
    @Path("/")
    @Secured({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArticle(Article article , @Context SecurityContext securityContext ) {
        articleRepository.update(article);
        Map<String, Object> res = new HashMap<>();
        res.put("code", Response.Status.OK);
        return Response.status(Response.Status.OK).entity(res).build();
    }
}
