package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.mapper.*;
import pro.sky.adsplatform.service.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);

    private final AdsMapper adsMapper;
    private final FullAdsMapper fullAdsMapper;
    private final CreateAdsMapper createAdsMapper;
    private final AdsCommentMapper adsCommentMapper;
    private final ResponseWrapperAdsMapper responseWrapperAdsMapper;
    private final ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper;
    private final UserService userService;
    private final AdsService adsService;
    private final AdsCommentService adsCommentService;
    private final AdsImageService adsImageService;
    private final AuthService authService;

    @Autowired
    public AdsController(AdsMapper adsMapper,
                         FullAdsMapper fullAdsMapper,
                         AdsCommentMapper adsCommentMapper,
                         CreateAdsMapper createAdsMapper,
                         ResponseWrapperAdsMapper responseWrapperAdsMapper,
                         ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper,
                         UserService userService,
                         AdsService adsService,
                         AdsCommentService adsCommentService,
                         AdsImageService adsImageService,
                         AuthService authService) {
        this.adsMapper = adsMapper;
        this.fullAdsMapper = fullAdsMapper;
        this.adsCommentMapper = adsCommentMapper;
        this.createAdsMapper = createAdsMapper;
        this.responseWrapperAdsMapper = responseWrapperAdsMapper;
        this.responseWrapperAdsCommentMapper = responseWrapperAdsCommentMapper;
        this.userService = userService;
        this.adsService = adsService;
        this.adsCommentService = adsCommentService;
        this.adsImageService = adsImageService;
        this.authService = authService;
    }

    @Operation(
            summary = "Добавить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно добавлено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Автор и/или содержимое изображения не найдено")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AdsDto> addAds(
            Authentication authentication,
            @Parameter(description = "Параметры объявления")
            @RequestPart("properties") @Valid CreateAdsDto createAdsDto,
            @Parameter(description = "Изображение")
            @RequestPart("image") MultipartFile file
    ) {
        LOGGER.info("Добавление объявления: {}", createAdsDto);

        UserEntity author = userService.findUserByName(authentication.getName());
        AdsEntity ads = createAdsMapper.createAdsDtoToAds(createAdsDto);
        ads.setAuthor(author);
        AdsEntity adsCreated = adsService.createAds(ads);

        String imageId = adsImageService.createImage(adsCreated, file);
        AdsDto adsDto = adsMapper.adsToAdsDto(adsCreated);
        adsDto.setImage("/ads/image/" + imageId);

        return ResponseEntity.ok(adsDto);
    }

    @Operation(
            summary = "Добавить отзыв",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно добавлен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Автор и/или объявление не найдены")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("{ad_pk}/comments")
    public ResponseEntity<AdsCommentDto> addAdsComments(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры отзыва")
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        LOGGER.info("Добавление отзыва: {}", adsCommentDto);

        UserEntity author = userService.findUserByName(authentication.getName());
        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));
        AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(adsCommentDto);
        adsComment.setAuthor(author);
        adsComment.setAds(ads);
        adsComment.setDateTime(LocalDateTime.now());
        AdsCommentEntity adsCommentCreated = adsCommentService.createAdsComment(adsComment);

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentCreated));
    }

    @Operation(
            summary = "Получить изображение по ID",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно получено"),
                    @ApiResponse(responseCode = "404", description = "Изображение не найдено")
            }
    )
    @GetMapping(value = "/image/{pk}")
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "ID изображения")
            @PathVariable("pk") Integer pk
    ) {
        LOGGER.info("Получение изображения {}", pk);

        AdsImageEntity adsImage = adsImageService.findImage(Long.valueOf(pk));

        return ResponseEntity.status(HttpStatus.OK).body(adsImage.getImage());
    }

    @Operation(
            summary = "Удалить отзыв",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно удален"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<Void> deleteAdsComment(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @Parameter(description = "ID отзыва")
            @PathVariable("id") Integer id
    ) {
        LOGGER.info("Удаление отзыва {}", id);

        UserEntity authorComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk)).getAuthor();
        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(authorComment.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        adsCommentService.deleteAdsComment(id);

        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "Получить список всех объявлений",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @GetMapping("")
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        LOGGER.info("Получение списка объявлений");

        List<AdsEntity> adsList = adsService.findAllAds();

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "Получить отзыв по ID",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
            }
    )
    @GetMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @Parameter(description = "ID отзыва")
            @PathVariable("id") Integer id
    ) {
        LOGGER.info("Получение отзыва {}", id);

        AdsCommentEntity adsComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk));

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsComment));
    }

    @Operation(
            summary = "Получить список отзывов",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @GetMapping("{ad_pk}/comments")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllAdsComments(
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk
    ) {
        LOGGER.info("Получение списка отзывов для объявления {}", adPk);

        List<AdsCommentEntity> adsCommentList = adsCommentService.findAllAdsComments(Long.parseLong(adPk));

        return ResponseEntity.ok(responseWrapperAdsCommentMapper
                .adsCommentListToResponseWrapperAdsCommentDto(adsCommentList.size(), adsCommentList));
    }

    @Operation(
            summary = "Получить список объявлений авторизованного пользователя",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(
            Authentication authentication,
            @RequestParam(required = false) Boolean authenticated,
            @RequestParam(required = false) String authorities0Authority,
            @RequestParam(required = false) Object credentials,
            @RequestParam(required = false) Object details,
            @RequestParam(required = false) Object principal
    ) {
        LOGGER.info("Получение списка объявлений авторизованного пользователя");

        UserEntity user = userService.findUserByName(authentication.getName());
        List<AdsEntity> adsList = adsService.findAllAdsByAuthor(user);

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "Получить объявление по ID",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно получено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<FullAdsDto> getAds(
            @Parameter(description = "ID объявления")
            @PathVariable("id") Integer id
    ) {
        LOGGER.info("Получение объявления {}", id);

        AdsEntity ads = adsService.findAds(id);

        return ResponseEntity.ok(fullAdsMapper.adsToFullAdsDto(ads));
    }

    @Operation(
            summary = "Удалить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно удалено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeAds(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("id") Integer id
    ) {
        LOGGER.info("Удаление объявления {}", id);

        UserEntity author = adsService.findAds(id).getAuthor();
        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(author.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        adsService.deleteAds(id);

        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "Обновить отзыв",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping("{ad_pk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @Parameter(description = "ID отзыва")
            @PathVariable("id") Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры отзыва")
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        LOGGER.info("Обновление отзыва {} : {}", id, adsCommentDto);

        UserEntity authorComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk)).getAuthor();
        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(authorComment.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        AdsCommentEntity adsCommentUpdated = adsCommentService
                .updateAdsComment(adsCommentMapper.adsCommentDtoToAdsComment(adsCommentDto), id, Long.parseLong(adPk));

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentUpdated));
    }

    @Operation(
            summary = "Обновить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{id}")
    public ResponseEntity<AdsDto> updateAds(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("id") Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры объявления")
            @RequestBody @Valid CreateAdsDto createAdsDto
    ) {
        LOGGER.info("Обновление объявления {} : {}", id, createAdsDto);

        AdsEntity ads = adsService.findAds(id);

        UserEntity author = ads.getAuthor();
        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(author.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        AdsEntity adsUpdated = adsService.updateAds(createAdsMapper.createAdsDtoToAds(createAdsDto), id);
        return ResponseEntity.ok(adsMapper.adsToAdsDto(adsUpdated));
    }

    @Operation(
            summary = "Получить список объявлений, совпадающих с шаблоном",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен")
            }
    )
    @GetMapping("search{title}")
    public ResponseEntity<ResponseWrapperAdsDto> findAllAdsByTitleLike(
            @Parameter(description = "Шаблон")
            @PathVariable("title") String title
    ) {
        LOGGER.info("Получение списка объявлений, совпадающих с шаблоном {}", title);

        List<AdsEntity> adsList = adsService.findAllAdsByTitleLike(title);

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "Добавить изображение",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно добавлено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Объявление и/или содержимое изображения не найдено"),
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdsImage(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable Integer id,
            @Parameter(description = "Изображение")
            @RequestParam MultipartFile image
    ) {
        LOGGER.info("Добавление изображения для объявления {}", id);

        AdsEntity ads = adsService.findAds(id);

        UserEntity author = ads.getAuthor();
        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(author.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        adsImageService.createImage(ads, image);

        return ResponseEntity.ok(null);
    }
}
