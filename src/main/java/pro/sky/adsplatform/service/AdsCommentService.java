package pro.sky.adsplatform.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.AdsCommentRepository;

import javax.naming.NotContextException;
import java.util.List;

/**
 * Сервис для работы с отзывами.
 */
@Service
public class AdsCommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsCommentService.class);

    private final AdsCommentRepository adsCommentRepository;

    public AdsCommentService(AdsCommentRepository adsCommentRepository) {
        this.adsCommentRepository = adsCommentRepository;
    }

    /**
     * Возвращает отзыв для объявления по указанному ID.
     *
     * @param id ID отзыва.
     * @param idAds ID объявления.
     * @return отзыв.
     * @throws NotFoundException отзыв с указанным ID отсутствует в базе.
     */
    public AdsCommentEntity findAdsComment(long id, long idAds) {
        return adsCommentRepository.findFirstByIdAndAdsId(id, idAds).orElseThrow(
                () -> new NotFoundException("Comment not found"));
    }

    /**
     * Возвращает отзыв для объявления по указанному ID.
     *
     * @param id ID отзыва.
     * @param idAds ID объявления.
     * @return отзыв.
     */
    @SneakyThrows
    public AdsCommentEntity findAdsCommentContent(long id, long idAds) {
        return adsCommentRepository.findFirstByIdAndAdsId(id, idAds).orElseThrow(
                () -> new NotContextException("No content for comment"));
    }

    /**
     * Возвращает все отзывы для объявления.
     *
     * @param idAds ID объявления.
     * @return список всех отзывов для объявления.
     */
    public List<AdsCommentEntity> findAllAdsComments(long idAds) {
        return adsCommentRepository.findAllByAdsId(idAds);
    }

    /**
     * Создает новый отзыв.
     *
     * @param adsComment новый отзыв.
     * @return созданный отзыв.
     */
    public AdsCommentEntity createAdsComment(AdsCommentEntity adsComment) {
        adsComment.setId(null);
        return adsCommentRepository.save(adsComment);
    }

    /**
     * Обновляет содержание отзыва (поле text).
     *
     * @param adsComment обновленные данные отзыва.
     * @param id ID отзыва.
     * @param idAds ID объявления.
     * @return обновленный отзыв.
     */
    @SneakyThrows
    public AdsCommentEntity updateAdsComment(AdsCommentEntity adsComment, long id, long idAds) {
        AdsCommentEntity adsCommentBD = findAdsCommentContent(id, idAds);
        adsCommentBD.setText(adsComment.getText());

        return adsCommentRepository.save(adsCommentBD);
    }

    /**
     * Удаляет отзыв.
     *
     * @param id ID отзыва, который должен быть удален.
     */
    public void deleteAdsComment(long id) {
        adsCommentRepository.deleteById(id);
    }
}
