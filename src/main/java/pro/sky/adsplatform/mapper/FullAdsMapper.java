package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pro.sky.adsplatform.dto.FullAdsDto;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.util.Arrays;

@Mapper(componentModel = "spring")
public interface FullAdsMapper {
    @Mapping(target = "authorFirstName", source = "entity.author.firstName")
    @Mapping(target = "authorLastName", source = "entity.author.lastName")
    @Mapping(target = "email", source = "entity.author.email")
    @Mapping(target = "phone", source = "entity.author.phone")
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "image", source = "entity", qualifiedByName = "getLastImageString")
    FullAdsDto adsToFullAdsDto(AdsEntity entity);

    @Named("getLastImageString")
    default String getLastImageString(AdsEntity entity) {
        AdsImageEntity lastImage = entity.getLastImage();
        return (lastImage == null) ? null : Arrays.toString(lastImage.getImage());
    }
}
