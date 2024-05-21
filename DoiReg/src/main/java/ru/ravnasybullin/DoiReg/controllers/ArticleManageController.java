//package ru.ravnasybullin.DoiReg.controllers;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import ru.ravnasybullin.DoiReg.PageConstants.PageConstants;
//import ru.ravnasybullin.DoiReg.models.Article;
//import ru.ravnasybullin.DoiReg.services.ArticleService;
//
//import java.util.List;
//
//@Controller
//@SessionAttributes("article")
//public class ArticleManageController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleManageController.class);
//
//    @Autowired
//    private ArticleService articleService;
//
//
//
//
//    @GetMapping(PageConstants.VIEW_ARTICLES_PAGE)
//    public String getAllArticles(Model model
//                                 ) {
//        List<Article> articleList = articleService.getArticles();
//        model.addAttribute("articles", articleList);
//        model.addAttribute("title", "Управление статьями");
//        return PageConstants.ARTICLES;
//    }
//
//
////    @GetMapping(PageConstants.EDIT_ARTICLE_PAGE)
////    public String getEditArticlePage(Model model,
////                                     @PathVariable("articleGUID") UUID guid, SessionStatus sessionStatus) {
////        try {
////            model.addAttribute("article", articleService.getArticle(guid));
////            if (".md".equals(articleService.getArticleTextFormat(guid))) {
////                model.addAttribute("articleText", articleService.getArticleText(guid, null, null));
////            }
////            if (".md".equals(articleService.getArticlePreviewTextFormat(guid))) {
////                model.addAttribute("articlePreviewText", articleService.getArticlePreviewText(guid));
////            }
////        } catch (Exception e) {
////            LOGGER.warn(e.getMessage(), e);
////            sessionStatus.setComplete();
////            return "redirect:" + PageConstants.ARTICLE_LIST_PAGE;
////        }
////        model.addAttribute("title", "Карточка статьи");
////        return PageConstants.ARTICLE_TEMPLATE_NAME;
////    }
////
////    @PostMapping(PageConstants.CREATE_ARTICLE_PAGE)
////    @Transactional
////    public String createArticle(@ModelAttribute("article") Article article,
////                                @ModelAttribute("articleText") String articleText,
////                                @ModelAttribute("articlePreviewText") String articlePreviewText,
////                                @RequestParam(value = "file", required = false) MultipartFile file,
////                                @RequestParam(value = "preview-file", required = false) MultipartFile previewFile,
////                                @RequestParam(value = "image", required = false) MultipartFile image,
////                                SessionStatus status) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                Article savedArticle = articleService.createArticle(article, file, previewFile, image, articleText, articlePreviewText);
////                status.setComplete();
////                return "redirect:" + PageConstants.CREATE_ARTICLE_PAGE + "/" + savedArticle.getGuid();
////            }
////        } catch (Exception e) {
////            LOGGER.warn(e.getMessage(), e);
////        }
////        status.setComplete();
////        return "redirect:" + PageConstants.ARTICLE_LIST_PAGE;
////    }
////
////
////    @PostMapping(PageConstants.EDIT_ARTICLE_PAGE)
////    @Transactional
////    public String saveArticle(@ModelAttribute("article") Article article,
////                              @ModelAttribute("articleText") String articleText,
////                              @ModelAttribute("articlePreviewText") String articlePreviewText,
////                              @PathVariable("articleGUID") UUID guid,
////                              @RequestParam(value = "file", required = false) MultipartFile file,
////                              @RequestParam(value = "preview-file", required = false) MultipartFile previewFile,
////                              @RequestParam(value = "image", required = false) MultipartFile image,
////                              SessionStatus sessionStatus) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                if (article.getGuid().equals(guid)) {
////                    Article savedArticle = articleService.saveArticle(article, file, previewFile, image, articleText, articlePreviewText);
////                    sessionStatus.setComplete();
////                    return "redirect:" + PageConstants.CREATE_ARTICLE_PAGE + "/" + savedArticle.getGuid();
////                }
////            }
////        } catch (Exception e) {
////            LOGGER.warn(e.getMessage(), e);
////        }
////        sessionStatus.setComplete();
////        return "redirect:" + PageConstants.ARTICLE_LIST_PAGE;
////    }
////
////    @DeleteMapping(PageConstants.TOGGLE_ARTICLE_TAG)
////    @Transactional
////    public ResponseEntity deleteArticleTag(@PathVariable("articleGUID") UUID guid,
////                                           @PathVariable("tag") String tag) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                articleService.removeTagFromArticle(guid, tag);
////            }
////        } catch (Exception e) {
////            LOGGER.warn(e.getMessage(), e);
////            return ResponseEntity.status(500).build();
////        }
////        return ResponseEntity.ok().build();
////    }
////
////    @PutMapping(PageConstants.TOGGLE_ARTICLE_TAG)
////    @Transactional
////    public ResponseEntity addArticleTag(@PathVariable("articleGUID") UUID guid,
////                                        @PathVariable("tag") String tag) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                articleService.addTagToArticle(guid, tag);
////            }
////        } catch (Exception e) {
////            LOGGER.warn(e.getMessage(), e);
////            return ResponseEntity.status(500).build();
////        }
////        return ResponseEntity.ok().build();
////    }
////
////    @DeleteMapping(PageConstants.DELETE_ARTICLE_IMAGE)
////    @Transactional
////    public ResponseEntity deleteArticleImage(@PathVariable("articleGUID") UUID guid) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                articleService.deleteImageFromArticle(articleService.getArticle(guid));
////            }
////        } catch (Exception e) {
////            LOGGER.warn(e.getMessage(), e);
////            return ResponseEntity.status(500).build();
////        }
////        return ResponseEntity.ok().build();
////    }
////
////    @GetMapping(PageConstants.DOWNLOAD_ARTICLE_TEXT)
////    @Transactional
////    public ResponseEntity downloadArticleText(@PathVariable("articleGUID") UUID guid) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                String result = articleService.getArticleText(guid, null, true);
////                String fileFormat = articleService.getArticleTextFormat(guid);
////                HttpHeaders h = new HttpHeaders();
////                h.setContentDisposition(ContentDisposition.builder("attachment; filename=\"article-text" + fileFormat + "\"").build());
////                return ResponseEntity.ok().headers(h).body(result);
////            }
////        } catch (IOException e) {
////            return ResponseEntity.status(500).build();
////        }
////        return ResponseEntity.ok().build();
////    }
////
////    @DeleteMapping(PageConstants.EDIT_ARTICLE_PAGE)
////    @Transactional
////    public ResponseEntity deleteArticle(@PathVariable("articleGUID") UUID guid, SessionStatus sessionStatus) {
////        try {
////            if (customSecurityService.hasRole(customSecurityService.getUserFromAuthentication(), RoleConstants.ADMIN)) {
////                articleService.deleteArticle(guid);
////            }
////        } catch (Exception e) {
////            return ResponseEntity.status(500).build();
////        }
////        sessionStatus.setComplete();
////        return ResponseEntity.ok().build();
////    }
//
//}
