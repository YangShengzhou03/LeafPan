package com.yangshengzhou.backend.controller.user;

import com.yangshengzhou.backend.dto.ApiResponse;
import com.yangshengzhou.backend.entity.Favorite;
import com.yangshengzhou.backend.entity.User;
import com.yangshengzhou.backend.service.AuthService;
import com.yangshengzhou.backend.service.FavoriteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/file/{fileId}")
    public ResponseEntity<ApiResponse<Favorite>> addFileFavorite(@PathVariable Long fileId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Favorite favorite = favoriteService.addFileFavorite(currentUser.getId(), fileId);
            return ResponseEntity.ok(ApiResponse.success("收藏成功", favorite));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/folder/{folderId}")
    public ResponseEntity<ApiResponse<Favorite>> addFolderFavorite(@PathVariable Long folderId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            Favorite favorite = favoriteService.addFolderFavorite(currentUser.getId(), folderId);
            return ResponseEntity.ok(ApiResponse.success("收藏成功", favorite));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/file/{fileId}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> removeFileFavorite(@PathVariable Long fileId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            favoriteService.removeFileFavorite(currentUser.getId(), fileId);
            return ResponseEntity.ok(ApiResponse.success(null, "取消收藏成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/folder/{folderId}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> removeFolderFavorite(@PathVariable Long folderId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            favoriteService.removeFolderFavorite(currentUser.getId(), folderId);
            return ResponseEntity.ok(ApiResponse.success(null, "取消收藏成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Favorite>>> getUserFavorites() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            List<Favorite> favorites = favoriteService.getUserFavorites(currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success(favorites));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/file/{fileId}")
    public ResponseEntity<ApiResponse<Boolean>> checkFileFavorite(@PathVariable Long fileId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            boolean isFavorited = favoriteService.isFileFavorited(currentUser.getId(), fileId);
            return ResponseEntity.ok(ApiResponse.success(isFavorited));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<ApiResponse<Boolean>> checkFolderFavorite(@PathVariable Long folderId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
            }
            
            boolean isFavorited = favoriteService.isFolderFavorited(currentUser.getId(), folderId);
            return ResponseEntity.ok(ApiResponse.success(isFavorited));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
